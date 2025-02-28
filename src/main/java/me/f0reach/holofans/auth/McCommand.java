package me.f0reach.holofans.auth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class McCommand implements CommandExecutor {
    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String commandName, @NotNull String @NotNull [] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.isOp()) {
                return false;
            }

            if (args.length == 0) {
                player.sendMessage("Usage: /holofansauth <cmd>");
                return true;
            }

            if (args[0].equals("setspawn")) {
                player.sendMessage("Set spawn command");
            } else if (args[0].equals("setbbox")) {
                player.sendMessage("Set bbox command");
            }
        }

        return true;
    }
}
