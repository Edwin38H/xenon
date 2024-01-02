package dev.portero.xenon.command;

import dev.portero.xenon.locale.Message;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentCommand implements CommandExecutor {

    @Getter
    private final List<SubCommand> subCommands;
    private final String permission;
    private final boolean playerOnly;

    public ParentCommand(String permission, boolean playerOnly) {
        this.subCommands = new ArrayList<>();
        this.permission = permission;
        this.playerOnly = playerOnly;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!playerOnly) {
            if (!(sender instanceof Player)) {
                Message.COMMANDS_ONLY_PLAYERS.send(sender);
                return true;
            }
        }

        if (permission != null && !sender.hasPermission(permission)) {
            Message.COMMANDS_NO_PERMISSION.send(sender);
            return true;
        }

        if (!subCommands.isEmpty()) {
            if (args.length > 0) {
                for (SubCommand subCommand : subCommands) {
                    if (subCommand.getLabel().equalsIgnoreCase(args[0])) {
                        return subCommand.execute(sender, args);
                    }
                }
            }
        }

        return execute(sender, args);
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public void addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }
}
