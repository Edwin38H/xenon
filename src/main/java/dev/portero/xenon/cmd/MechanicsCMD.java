package dev.portero.xenon.cmd;

import dev.portero.xenon.command.SubCommand;
import org.bukkit.command.CommandSender;

public class MechanicsCMD implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("MechanicsCMD");
        return false;
    }

    @Override
    public String getLabel() {
        return "mechanics";
    }
}
