package com.james090500.VelocityWhitelist.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.james090500.VelocityWhitelist.VelocityWhitelist;
import org.geysermc.floodgate.api.FloodgateApi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record MinecraftApi() {

    /**
     * Returns player UUID
     *
     * @param username Username to get UUID from
     * @return Players uuid
     */
    public static CompletableFuture<UUID> getUUID(String username) {
        //Try get online player first
        if (VelocityWhitelist.getInstance().getServer().getPlayer(username).isPresent()) {
            return CompletableFuture.supplyAsync(() -> VelocityWhitelist.getInstance().getServer().getPlayer(username).get().getUniqueId());
        }

        if(username.startsWith(".")) {
            FloodgateApi api = FloodgateApi.getInstance();
            return api.getUuidFor(username.replace(".", ""));
        } else {
            // Fallback to API
            return CompletableFuture.supplyAsync(() -> {
                JsonObject playerElement = getApiData(username);
                if (playerElement != null) {
                    JsonElement playerUUID = playerElement.get("full_uuid");
                    if (playerUUID != null && !playerUUID.isJsonNull()) {
                        return UUID.fromString(playerUUID.getAsString());
                    }
                }
                return  null;
            });
        }
    }

    /**
     * Request API call for user data
     *
     * @param data The username/uuid to send
     * @return The response data
     */
    private static JsonObject getApiData(String data) {
        HttpURLConnection conn = null;
        try {
            URI uri = URI.create("https://api.minecraftapi.net/v3/profile/" + data + "?params=[full_uuid,name]");

            conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestProperty("User-Agent", "velocitywhitelist/1.0.3-SNAPSHOT");

            int code = conn.getResponseCode();
            InputStream stream = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
            if (stream == null || code < 200 || code >= 300) return null;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                char[] buf = new char[2048];
                int n;
                while ((n = reader.read(buf)) != -1) sb.append(buf, 0, n);
                return JsonParser.parseString(sb.toString()).getAsJsonObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}