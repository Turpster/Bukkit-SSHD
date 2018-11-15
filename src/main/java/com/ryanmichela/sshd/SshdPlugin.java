package com.ryanmichela.sshd;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.LogLevel;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.util.logging.Level;

/**
 * Copyright 2013 Ryan Michela
 */
public class SshdPlugin extends PluginBase {

    private SshServer sshd;
    public static SshdPlugin instance;

    private PluginLogger logger;

    public PluginLogger getLogger() {
        return logger;
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        File authorizedKeys = new File(getDataFolder(), "authorized_keys");
        if (!authorizedKeys.exists()) {
            authorizedKeys.mkdirs();
        }

        // TODO Take note:

        // Don't go any lower than INFO or SSHD will cause a stack overflow exception.
        // SSHD will log that it wrote bites to the output stream, which writes
        // bytes to the output stream - ad nauseaum.

        // Nukkit does not use Java Logger's. - Turpster
        // logger.setLevel(Level.INFO);
    }

    @Override
    public void onEnable() {
        instance = this;

        logger = new PluginLogger(this);

        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(getConfig().getInt("port", 22));
        String host = getConfig().getString("listenAddress", "all");
        sshd.setHost(host.equals("all") ? null : host);

        File hostKey = new File(getDataFolder(), "hostkey");
        File authorizedKeys = new File(getDataFolder(), "authorized_keys");

        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(hostKey));
        sshd.setShellFactory(new ConsoleShellFactory());
        sshd.setPasswordAuthenticator(new ConfigPasswordAuthenticator());
        sshd.setPublickeyAuthenticator(new PublicKeyAuthenticator(authorizedKeys));

        if (getConfig().getBoolean("enableSFTP")) {
            sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
            sshd.setFileSystemFactory(new VirtualFileSystemFactory(
                    FileSystems.getDefault().getPath(
                            getDataFolder().getAbsolutePath()
                    ).getParent().getParent()
            ));
        }

        sshd.setCommandFactory(new ConsoleCommandFactory());
        try {
            sshd.start();
        } catch (IOException e) {
            getLogger().log(LogLevel.EMERGENCY, "Failed to start SSH server!", e);
        }
    }

    @Override
    public void onDisable() {
        try {
            sshd.stop();
        } catch (Exception e) {
            // do nothing
        }
    }
}
