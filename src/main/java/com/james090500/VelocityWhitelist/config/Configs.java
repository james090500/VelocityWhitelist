package com.james090500.VelocityWhitelist.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.james090500.VelocityWhitelist.VelocityWhitelist;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Configs {

    @Getter private static Config config;
    @Getter private static Set<UUID> whitelist = new HashSet<>();
    private static Path configFile;
    private static Path whitelistFile;

    /**
     * Loads the config files.
     * @param velocityWhitelist
     */
    public static void loadConfigs(VelocityWhitelist velocityWhitelist) {
        configFile = Path.of(velocityWhitelist.getDataDirectory() + "/config.toml");
        whitelistFile = Path.of(velocityWhitelist.getDataDirectory() + "/whitelist.json");

        //Create data directory
        if(!velocityWhitelist.getDataDirectory().toFile().exists()) {
            velocityWhitelist.getDataDirectory().toFile().mkdir();
        }

        //Load the config.toml to memory
        if(!configFile.toFile().exists()) {
            try (InputStream in = VelocityWhitelist.class.getResourceAsStream("/config.toml")) {
                Files.copy(in, configFile);
            } catch (Exception e) {
                velocityWhitelist.getLogger().error("Error loading config.toml");
                e.printStackTrace();
            }
        }
        config = new Toml().read(configFile.toFile()).to(Config.class);

        //Load whitelist players to memory (if any)
        if(whitelistFile.toFile().exists()) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(whitelistFile.toFile()), "UTF8")) {
                Type whitelistSetType = new TypeToken<HashSet<UUID>>(){}.getType();
                whitelist = new Gson().fromJson(inputStreamReader, whitelistSetType);
            } catch (Exception e) {
                velocityWhitelist.getLogger().error("Error loading whitelist.json");
                e.printStackTrace();
            }
        }
    }

    /**
     * Save the config
     * @param velocityWhitelist
     */
    public static void saveConfig(VelocityWhitelist velocityWhitelist) {
        try {
            new TomlWriter().write(config, configFile.toFile());
        } catch (Exception e) {
            velocityWhitelist.getLogger().error("Error writing config.toml");
            e.printStackTrace();
        }
    }

    /**
     * Save the whitelist file
     * @param velocityWhitelist
     */
    public static void saveWhitelist(VelocityWhitelist velocityWhitelist) {
        try {
            FileWriter fileWriter = new FileWriter(whitelistFile.toFile());
            new Gson().toJson(whitelist, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            velocityWhitelist.getLogger().error("Error writing whitelist.json");
            e.printStackTrace();
        }
    }

    /**
     * The main config
     */
    public class Config {

        @Getter @Setter
        private boolean enabled;
        @Getter
        private String message;

        @Override
        public String toString() {
            return "Panel{" +
                "enabled='" + enabled + '\'' +
                ", message='" + message + '\'' +
            '}';
        }
    }
}