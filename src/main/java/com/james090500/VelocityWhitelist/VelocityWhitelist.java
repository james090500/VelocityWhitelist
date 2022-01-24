package com.james090500.VelocityWhitelist;

import com.google.inject.Inject;
import com.james090500.VelocityWhitelist.commands.CommandBuilder;
import com.james090500.VelocityWhitelist.commands.CommandHandler;
import com.james090500.VelocityWhitelist.config.Configs;
import com.james090500.VelocityWhitelist.listeners.JoinListener;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "velocitywhitelist", name = "VelocityWhitelist", version = "1.0.1-SNAPSHOT", description = "A Proxy based whitelist", authors = { "james095000" })
public class VelocityWhitelist {

    public final String PREFIX = "[VelocityWhitelist] ";
    @Getter private final ProxyServer server;
    @Getter private final Logger logger;
    @Getter private final Path dataDirectory;

    @Inject
    public VelocityWhitelist(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        //Load configs
        Configs.loadConfigs(this);

        //Register listeners
        server.getEventManager().register(this, new JoinListener());

        //Setup command flow
        final CommandHandler handler = new CommandHandler(this);
        LiteralCommandNode<CommandSource> rootNode = LiteralArgumentBuilder.<CommandSource>literal("vwhitelist").build();

        //Register commands
        CommandBuilder.register(this);
    }
}