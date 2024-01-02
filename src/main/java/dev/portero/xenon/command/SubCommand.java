package dev.portero.xenon.command;

import org.bukkit.command.CommandSender;

/**
 * Represents a subcommand in Xenon.
 * This interface defines the structure for any subcommand that can be executed within the plugin.
 */
public interface SubCommand {

    /**
     * Executes the subcommand with the given arguments.
     *
     * @param sender The sender of the command. This can be a player, console, or any other type of command sender.
     * @param args   The arguments passed to the command. The array contains the arguments, excluding the command label.
     * @return true if the command was executed successfully, false otherwise.
     */
    boolean execute(CommandSender sender, String[] args);

    /**
     * Returns the label of the subcommand. This label is used to identify and invoke the subcommand.
     *
     * @return The label of the subcommand.
     */
    String getLabel();
}
