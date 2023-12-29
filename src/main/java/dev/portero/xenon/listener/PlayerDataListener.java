package dev.portero.xenon.listener;

import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.prefs.PreferenceChangeEvent;

import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class PlayerDataListener implements Listener {

    @EventHandler
    public void onJoinLoadPlayerData(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(Component.text("Your current locale is: ").color(YELLOW)
                .append(Component.space())
                .append(Component.text(player.locale().getDisplayName()).color(RED)));
    }

    @EventHandler
    public void onPrefChange(PlayerClientOptionsChangeEvent event){
        Player player = event.getPlayer();

        player.sendMessage(Component.text("Your current locale is: ").color(YELLOW)
                .append(Component.space())
                .append(Component.text(player.locale().getDisplayName()).color(RED)));
    }
}
