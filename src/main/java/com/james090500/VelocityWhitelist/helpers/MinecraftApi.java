package com.james090500.VelocityWhitelist.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.james090500.VelocityWhitelist.VelocityWhitelist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public record MinecraftApi(VelocityWhitelist velocityWhitelist) {

    /**
     * Returns player UUID
     *
     * @param username Username to get UUID from
     * @return Players uuid
     */
    public UUID getUUID(String username) {
        //Try get online player first
        if (velocityWhitelist.getServer().getPlayer(username).isPresent()) {
            return velocityWhitelist.getServer().getPlayer(username).get().getUniqueId();
        }

        JsonObject playerElement = getApiData(username);
        if (playerElement != null) {
            JsonElement playerUUID = playerElement.get("full_uuid");
            if (playerUUID != null && !playerUUID.isJsonNull()) {
                return UUID.fromString(playerUUID.getAsString());
            }
        }

        return null;
    }

    /**
     * Request API call for user data
     *
     * @param data The username/uuid to send
     * @return The response data
     */
    private static JsonObject getApiData(String data) {
        try {
            URL url = new URL("https://minecraftapi.net/api/v1/profile/" + data);
            HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();

            if (httpurlconnection.getResponseCode() / 100 == 2) {
                //Create reader
                BufferedReader in = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                //Read response
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);

                //Convert response to JSON
                return JsonParser.parseString(response.toString()).getAsJsonObject();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}