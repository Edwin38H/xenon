package dev.portero.xenon;

import org.bukkit.plugin.java.JavaPlugin;

public class Xenon extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("Xenon has been enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Xenon has been disabled!");
    }
}