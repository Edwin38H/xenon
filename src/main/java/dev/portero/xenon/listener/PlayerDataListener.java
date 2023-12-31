package dev.portero.xenon.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerDataListener implements Listener {

    @EventHandler
    public void onJoinLoadPlayerData(PlayerJoinEvent event) {
        Player player = event.getPlayer();

    }
}
