package me.f0reach.holofans.auth;

import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import java.util.Collections;

public class AuthView {
    private final AuthPlugin plugin;

    public AuthView(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    public void ensureMapSet(Player player) {
        var pos = plugin.getAppConfig().getVerifyMessagePosition();
        var world = plugin.getServer().getWorld(plugin.getAppConfig().getVerifyWorld());
        var location = pos.toLocation(world);

        if (!location.isChunkLoaded()) return;

        // Lookup for item frame entity
        var entities = location.getNearbyEntitiesByType(ItemFrame.class, 1);
        if (entities.isEmpty()) {
            throw new IllegalStateException("Item frame not found");
        }

        var itemFrame = (ItemFrame) entities.toArray()[0];

        // Get existing map item
        var item = itemFrame.getItem();
        if (item.getType() != Material.FILLED_MAP) {
            var mapView = plugin.getServer().createMap(player.getWorld());
            item = new ItemStack(Material.FILLED_MAP, 1);
            var mapMeta = (MapMeta) item.getItemMeta();

            mapMeta.setLore(Collections.singletonList("PIN Code Map"));
            mapMeta.setMapView(mapView);

            item.setItemMeta(mapMeta);
            itemFrame.setItem(item);
        }

        var mapMeta = (MapMeta) item.getItemMeta();
        var mapView = mapMeta.getMapView();

        // Create a map item
        mapView.getRenderers().forEach(mapView::removeRenderer);
        mapView.addRenderer(new DiscordCodeMapRenderer());
    }
}
