package me.f0reach.holofans.auth;

import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import org.bukkit.plugin.java.JavaPlugin;

public final class AuthPlugin extends JavaPlugin {
    private static AuthPlugin plugin = null;
    private final Config config = new Config(this);
    private DiscordAddon discordAddon = null;
    private WorldListener worldListener = null;

    public static AuthPlugin getPlugin() {
        return plugin;
    }

    public Config getAppConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        var bootable = false;
        try {
            saveDefaultConfig();
            getAppConfig().load();
            bootable = true;
        } catch (Exception e) {
            getLogger().severe("Failed to load config: " + e.getMessage());
        }

        plugin = this;

        if (bootable) {
            discordAddon = new DiscordAddon(DiscordIntegration.INSTANCE);
            worldListener = new WorldListener(this, discordAddon);

            getServer().getPluginManager().registerEvents(worldListener, this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
