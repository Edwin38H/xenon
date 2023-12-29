package dev.portero.xenon.locale;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public interface Message {

    Args0 COMMAND_NO_PERMISSION = () -> translatable()
            .key("commands.no-permission")
            .color(RED)
            .build();

    interface Args0 {
        Component build();

        default void send(CommandSender sender) {
            sender.sendMessage(build());
        }
    }
}
