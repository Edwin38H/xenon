package dev.portero.xenon.cmd;

import dev.portero.xenon.command.ParentCommand;
import org.bukkit.command.CommandSender;

public class XenonCMD extends ParentCommand {

    public XenonCMD() {
        super("xenon.admin", true);
        this.addSubCommand(new MechanicsCMD());
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("XenonCMD");
        return false;
    }


}
