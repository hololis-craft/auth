package me.f0reach.holofans.auth;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class Config {
    private final AuthPlugin plugin;
    private String baseUrl;

    // VerifyWorld
    private String verifyWorld;
    private Vector verifySpawnPosition;
    private Vector verifyMessagePosition;
    private double verifySpawnYaw;
    private BoundingBox verifyAllowedArea;

    // Twitter
    private String twitterConsumerKey;
    private String twitterConsumerSecret;
    private String twitterCallbackUrl;

    public Config(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        baseUrl = this.plugin.getConfig().getString("baseUrl");

        var verifySection = this.plugin.getConfig().getConfigurationSection("verifyRoom");
        verifyWorld = verifySection.getString("world");
        verifySpawnPosition = verifySection.getVector("spawnPosition");
        verifyMessagePosition = verifySection.getVector("messagePosition");
        verifySpawnYaw = verifySection.getDouble("spawnYaw");
        var allowedArea1 = verifySection.getVector("bbox1");
        var allowedArea2 = verifySection.getVector("bbox2");
        verifyAllowedArea = BoundingBox.of(allowedArea1, allowedArea2);

        var twitterSection = this.plugin.getConfig().getConfigurationSection("twitter");
        twitterConsumerKey = twitterSection.getString("consumerKey");
        twitterConsumerSecret = twitterSection.getString("consumerSecret");
        twitterCallbackUrl = twitterSection.getString("callbackUrl");
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

    public Vector getVerifyMessagePosition() {
        return verifyMessagePosition;
    }

    public double getVerifySpawnYaw() {
        return verifySpawnYaw;
    }

    public BoundingBox getVerifyAllowedArea() {
        return verifyAllowedArea;
    }

    public String getTwitterConsumerKey() {
        return twitterConsumerKey;
    }

    public String getTwitterConsumerSecret() {
        return twitterConsumerSecret;
    }

    public String getTwitterCallbackUrl() {
        return twitterCallbackUrl;
    }
}
