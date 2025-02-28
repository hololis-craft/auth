package me.f0reach.holofans.auth;

import de.erdbeerbaerlp.dcintegration.common.storage.linking.LinkManager;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.stream.IntStream;

public class DiscordCodeMapRenderer extends MapRenderer {
    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        // Draw code
        var isLinked = LinkManager.isPlayerLinked(player.getUniqueId());

        // Size: 128x128
        // Fill background
        IntStream.range(0, 128).forEach(x -> IntStream.range(0, 128).forEach(y -> mapCanvas.setPixelColor(x, y, Color.WHITE)));
        mapCanvas.drawText(10, 32, MinecraftFont.Font, player.getName());
        if (isLinked) {
            mapCanvas.drawText(10, 48, MinecraftFont.Font, "Linked");
        } else {
            mapCanvas.drawText(10, 48, MinecraftFont.Font, "Not linked");
            var code = Integer.toString(LinkManager.genLinkNumber(player.getUniqueId()));
            mapCanvas.drawText(10, 64, MinecraftFont.Font, code);
        }
    }
}
