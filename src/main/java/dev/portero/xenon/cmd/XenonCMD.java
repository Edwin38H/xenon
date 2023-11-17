package dev.portero.xenon.cmd;

import dev.portero.xenon.Xenon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class XenonCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Xenon.getInstance().getLogger().info("XenonCMD executed!");
        return false;
    }
}
