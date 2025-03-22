package me.f0reach.holofans.auth.twitter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import me.f0reach.holofans.auth.AuthPlugin;

import java.io.*;
import java.util.HashMap;

public class TwitterLinkStorage {
    private final HashMap<String, String> twitterLinks = new HashMap<>();
    private final AuthPlugin plugin;

    public TwitterLinkStorage(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    public String getPath() {
        return plugin.getDataFolder().toPath().resolve("twitter-links.json").toAbsolutePath().toString();
    }

    public void save() {
        // Save twitter links
        var json = new JsonArray();
        twitterLinks.forEach((uuid, twitterId) -> {
            var obj = new JsonArray();
            obj.add(uuid);
            obj.add(twitterId);
            json.add(obj);
        });
        try (Writer writer = new FileWriter(getPath())) {
            new Gson().toJson(json, writer);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save twitter links: " + e.getMessage());
        }
    }

    public void load() {
        try (Reader reader = new FileReader(getPath())) {
            var json = new Gson().fromJson(reader, JsonArray.class);
            json.forEach(e -> {
                var arr = e.getAsJsonArray();
                twitterLinks.put(arr.get(0).getAsString(), arr.get(1).getAsString());
            });
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load twitter links: " + e.getMessage());
        }
    }
}
