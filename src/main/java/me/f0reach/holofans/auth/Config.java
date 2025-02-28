package me.f0reach.holofans.auth;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class Config {
    private final AuthPlugin plugin;
    private String baseUrl;
    private String verifyWorld;
    private Vector verifySpawnPosition;
    private double verifySpawnYaw;
    private BoundingBox verifyAllowedArea;

    public Config(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        baseUrl = this.plugin.getConfig().getString("baseUrl");

        var verifySection = this.plugin.getConfig().getConfigurationSection("verifyRoom");
        verifyWorld = verifySection.getString("world");
        verifySpawnPosition = verifySection.getVector("spawnPosition");
        verifySpawnYaw = verifySection.getDouble("spawnYaw");
        var allowedArea1 = verifySection.getVector("bbox1");
        var allowedArea2 = verifySection.getVector("bbox2");
        verifyAllowedArea = BoundingBox.of(allowedArea1, allowedArea2);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getVerifyWorld() {
        return verifyWorld;
    }

    public Vector getVerifySpawnPosition() {
        return verifySpawnPosition;
    }

    public double getVerifySpawnYaw() {
        return verifySpawnYaw;
    }

    public BoundingBox getVerifyAllowedArea() {
        return verifyAllowedArea;
    }
}
