package me.f0reach.holofans.auth;

import org.bukkit.plugin.Plugin;

import java.util.List;

public class Config {
    private final AuthPlugin plugin;
    private String guildId;
    private String baseUrl;
    private List<String> adminRoles;

    public Config(AuthPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        baseUrl = this.plugin.getConfig().getString("baseUrl");
        guildId = this.plugin.getConfig().getString("guildId");
        adminRoles = this.plugin.getConfig().getStringList("adminRoles");
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getGuildId() {
        return guildId;
    }

    public List<String> getAdminRoles() {
        return adminRoles;
    }
}
