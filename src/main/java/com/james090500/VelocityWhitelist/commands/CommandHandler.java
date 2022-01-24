package com.james090500.VelocityWhitelist.commands;

import com.james090500.VelocityWhitelist.VelocityWhitelist;
import com.james090500.VelocityWhitelist.config.Configs;
import com.james090500.VelocityWhitelist.helpers.WhitelistHelper;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class CommandHandler {

    private VelocityWhitelist velocityWhitelist;

    public CommandHandler(VelocityWhitelist velocityWhitelist) {
        this.velocityWhitelist = velocityWhitelist;
    }

    /**
     * A bit of basic about information
     * @param commandSourceCommandContext
     * @return
     */
    public int about(CommandContext<CommandSource> commandSourceCommandContext) {
        CommandSource source = commandSourceCommandContext.getSource();
        String status = Configs.getConfig().isEnabled() ? "&2&lON" : "&c&lOFF";
        source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&a" + velocityWhitelist.PREFIX + "Whitelist is " + status));
        source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&a" + velocityWhitelist.PREFIX + "VelocityWhitelist by james090500"));
        return 1;
    }

    /**
     * Turn on the whitelist
     * @param commandSourceCommandContext
     * @return
     */
    public int turnOn(CommandContext<CommandSource> commandSourceCommandContext) {
        CommandSource source = commandSourceCommandContext.getSource();
        if(Configs.getConfig().isEnabled()) {
            source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&c" + velocityWhitelist.PREFIX + "Whitelist is already turned on"));
        } else {
            Configs.getConfig().setEnabled(true);
            Configs.saveConfig(velocityWhitelist);
            source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&a" + velocityWhitelist.PREFIX + "Whitelist turned &2&lON"));
        }
        return 1;
    }

    /**
     * Turn off the whitelist
     * @param commandSourceCommandContext
     * @return
     */
    public int turnOff(CommandContext<CommandSource> commandSourceCommandContext) {
        CommandSource source = commandSourceCommandContext.getSource();
        if(!Configs.getConfig().isEnabled()) {
            source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&c" + velocityWhitelist.PREFIX + "Whitelist is already turned off"));
        } else {
            Configs.getConfig().setEnabled(false);
            Configs.saveConfig(velocityWhitelist);
            source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&a" + velocityWhitelist.PREFIX + "Whitelist turned &c&lOFF"));
        }
        return 1;
    }

    /**
     * The command for /vwhitelist add <username>
     * Handles adding a user to the whitelist
     * @param commandSourceCommandContext
     * @return
     */
    public int add(CommandContext<CommandSource> commandSourceCommandContext) {
        CommandSource source = commandSourceCommandContext.getSource();
        ParsedArgument<CommandSource, ?> username = commandSourceCommandContext.getArguments().get("username");
        if(username == null) {
            source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&c" + velocityWhitelist.PREFIX + "Syntax /vwhitelist add <username>"));
            return 1;
        }

        new WhitelistHelper(velocityWhitelist, source).add((String) username.getResult());
        return 1;
    }

    /**
     * The command for /vwhitelist remove <username>
     * Handles removing a user from the whitelist
     * @param commandSourceCommandContext
     * @return
     */
    public int remove(CommandContext<CommandSource> commandSourceCommandContext) {
        CommandSource source = commandSourceCommandContext.getSource();
        ParsedArgument<CommandSource, ?> username = commandSourceCommandContext.getArguments().get("username");
        if(username == null) {
            source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&c" + velocityWhitelist.PREFIX + "Syntax /vwhitelist remove <username>"));
            return 1;
        }

        new WhitelistHelper(velocityWhitelist, source).remove((String) username.getResult());
        return 1;
    }

    /**
     * Reloads the configs
     * @param commandSourceCommandContext
     * @return
     */
    public int reload(CommandContext<CommandSource> commandSourceCommandContext) {
        Configs.loadConfigs(velocityWhitelist);
        CommandSource source = commandSourceCommandContext.getSource();
        source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&a" + velocityWhitelist.PREFIX + "Reloaded"));
        return 1;
    }
}