package me.f0reach.holofans.auth;

import com.discordsrv.api.DiscordSRVApi;
import org.bukkit.plugin.java.JavaPlugin;

public final class AuthPlugin extends JavaPlugin {
    private final DiscordSRVListener discordSRVListener = new DiscordSRVListener(this);
    private final Config config = new Config(this);

    public Config getAppConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getAppConfig().load();

        DiscordSRVApi.get().eventBus().subscribe(discordSRVListener);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
