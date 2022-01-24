package com.james090500.VelocityWhitelist.helpers;

import com.james090500.VelocityWhitelist.VelocityWhitelist;
import com.james090500.VelocityWhitelist.config.Configs;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.UUID;

public class WhitelistHelper {

    private VelocityWhitelist velocityWhitelist;
    private CommandSource source;

    public WhitelistHelper(VelocityWhitelist velocityWhitelist, CommandSource source) {
        this.velocityWhitelist = velocityWhitelist;
        this.source = source;
    }

    /**
     * Check if the player is in the whitelist OR has bypass permissions
     * @param player
     * @return If player is whitelisted or has bypass
     */
    public static boolean check(Player player) {
        return player.hasPermission("vwhitelist.bypass") ? true : Configs.getWhitelist().contains(player.getUniqueId());
    }

    /**
     * Add a player to the whitelist
     * @param username
     */
    public void add(String username) {
        velocityWhitelist.getServer().getScheduler().buildTask(velocityWhitelist, () -> {
            UUID uuid = new MinecraftApi(velocityWhitelist).getUUID(username);
            if(uuid == null) {
                source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&c" + velocityWhitelist.PREFIX + username + " is not a valid username"));
            } else if(Configs.getWhitelist().contains(uuid)) {
                source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&a" + velocityWhitelist.PREFIX + username + " is already in the whitelist"));
            } else {
                Configs.getWhitelist().add(uuid);
                Configs.saveWhitelist(velocityWhitelist);
                source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&a" + velocityWhitelist.PREFIX + username + " has been added to the whitelist"));
            }
        }).schedule();
    }

    /**
     * Remove a player from the whitelist
     * @param username
     */
    public void remove(String username) {
        velocityWhitelist.getServer().getScheduler().buildTask(velocityWhitelist, () -> {
            UUID uuid = new MinecraftApi(velocityWhitelist).getUUID(username);
            if(uuid == null) {
                source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&c" + velocityWhitelist.PREFIX + username + " is not a valid username"));
            } else if(!Configs.getWhitelist().contains(uuid)) {
                source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&a" + velocityWhitelist.PREFIX + username + " is not in the whitelist"));
            } else {
                Configs.getWhitelist().remove(uuid);
                Configs.saveWhitelist(velocityWhitelist);
                source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&a" + velocityWhitelist.PREFIX + username + " has been removed from the whitelist"));
            }
        }).schedule();
    }

}
