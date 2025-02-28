package me.f0reach.holofans.auth;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WorldListener implements Listener {
    private final AuthPlugin plugin;
    private final DiscordAddon discordAddon;
    private final Set<UUID> notLinkedPlayers;
    private final AuthView authView;

    public WorldListener(AuthPlugin plugin, DiscordAddon discordAddon) {
        this.plugin = plugin;
        this.discordAddon = discordAddon;
        this.notLinkedPlayers = new HashSet<>();
        this.authView = new AuthView(plugin);
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
            player.teleport(teleportLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
            notLinkedPlayers.add(uuid);
        }


        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            authView.ensureMapSet(player);
        }, 20L);
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

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        // notLinkedPlayers cannot teleport using commands / ender pearls / chorus fruits
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) return;
        var player = event.getPlayer();
        var uuid = player.getUniqueId();
        if (notLinkedPlayers.contains(uuid)) {
            player.sendMessage("連携を完了するまでテレポートできません");
            event.setCancelled(true);
        }
    }
}
