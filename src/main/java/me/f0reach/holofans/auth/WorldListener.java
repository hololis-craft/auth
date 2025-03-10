package me.f0reach.holofans.auth;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WorldListener implements Listener {
    private final AuthPlugin plugin;
    private final DiscordAddon discordAddon;
    private final Set<UUID> notLinkedPlayers;

    public WorldListener(AuthPlugin plugin, DiscordAddon discordAddon) {
        this.plugin = plugin;
        this.discordAddon = discordAddon;
        this.notLinkedPlayers = new HashSet<>();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var uuid = player.getUniqueId();
        var config = plugin.getAppConfig();
        if (!discordAddon.hasLinked(uuid)) {
            player.sendMessage("アカウントがリンクされていません。Discordでリンクしてください。");
            var verifyWorld = plugin.getServer().getWorld(plugin.getAppConfig().getVerifyWorld());
            var teleportLocation = config.getVerifySpawnPosition().toLocation(verifyWorld);
            teleportLocation.setYaw((float) config.getVerifySpawnYaw());
            player.teleport(teleportLocation);
            notLinkedPlayers.add(uuid);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        // Check if notLinkedPlayers is in the allowed area
        var player = event.getPlayer();
        var uuid = player.getUniqueId();
        if (!notLinkedPlayers.contains(uuid)) {
            return;
        }

        var config = plugin.getAppConfig();
        var allowedArea = config.getVerifyAllowedArea();
        if (!allowedArea.contains(event.getTo().toVector())) {
            player.sendMessage("連携を完了するまでこのエリアから移動できません");
            event.setTo(event.getFrom());
        }
    }
}
