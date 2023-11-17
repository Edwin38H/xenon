package dev.portero.xenon;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Xenon extends JavaPlugin {

    @Getter
    private static Xenon instance;

    @Override
    public void onEnable() {
        this.setInstance(this);
    }

    @Override
    public void onDisable() {
        this.setInstance(null);
    }

    private void setInstance(Xenon instance) {
        Xenon.instance = instance;
    }
}
