package dev.portero.xenon.cmd;

import dev.portero.xenon.locale.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class XenonCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Message.COMMANDS_ONLY_PLAYERS.send(sender);
            return false;
        }

        Message.COMMANDS_NO_PERMISSION.send(player);
        return false;
    }
}
