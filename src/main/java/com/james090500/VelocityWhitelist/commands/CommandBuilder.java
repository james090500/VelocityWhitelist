package com.james090500.VelocityWhitelist.commands;

import com.james090500.VelocityWhitelist.VelocityWhitelist;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.concurrent.CompletableFuture;

public class CommandBuilder {

    private static ProxyServer server;

    /**
     * Reginster all commands
     * @param velocityWhitelist
     */
    public static void register(VelocityWhitelist velocityWhitelist) {
        server = velocityWhitelist.getServer();
        //Setup command flow
        final CommandHandler handler = new CommandHandler(velocityWhitelist);
        server.getCommandManager().register(server.getCommandManager().metaBuilder("vwhitelist").build(), new BrigadierCommand(
                LiteralArgumentBuilder.<CommandSource>literal("vwhitelist").requires(sender -> sender.hasPermission("vwhitelist.admin")).executes(handler::about)
                        .then(LiteralArgumentBuilder.<CommandSource>literal("on").executes(handler::turnOn))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("off").executes(handler::turnOff))

                        .then(LiteralArgumentBuilder.<CommandSource>literal("add").executes(handler::add))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("add")
                                .then(RequiredArgumentBuilder.<CommandSource, String>argument("username", StringArgumentType.word())
                                .suggests(CommandBuilder::allPlayers)
                                .executes(handler::add)))

                        .then(LiteralArgumentBuilder.<CommandSource>literal("remove").executes(handler::remove))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("remove")
                                .then(RequiredArgumentBuilder.<CommandSource, String>argument("username", StringArgumentType.word())
                                .suggests(CommandBuilder::allPlayers)
                                .executes(handler::remove)))

                        .then(LiteralArgumentBuilder.<CommandSource>literal("reload").requires(source -> source.hasPermission("vgui.admin")).executes(handler::reload))
        ));
    }

    /**
     * An all players tab suggestions
     * @param context
     * @param builder
     * @return
     */
    private static CompletableFuture<Suggestions> allPlayers(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        for (Player player : server.getAllPlayers()) {
            String username = player.getUsername();
            if (username.toLowerCase().startsWith(context.getInput().toLowerCase()) || username.equalsIgnoreCase(context.getInput())) {
                builder.suggest(username);
            }
        }
        return builder.buildFuture();
    }
}
