package me.f0reach.holofans.auth;

import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import org.bukkit.plugin.java.JavaPlugin;

public final class AuthPlugin extends JavaPlugin {
    private static AuthPlugin plugin = null;
    private final Config config = new Config(this);
    private DiscordAddon discordAddon = null;

    public static AuthPlugin getPlugin() {
        return plugin;
    }

    public Config getAppConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getAppConfig().load();

        discordAddon = new DiscordAddon(DiscordIntegration.INSTANCE);

        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
