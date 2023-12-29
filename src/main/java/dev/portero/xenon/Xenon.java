package dev.portero.xenon;

import dev.portero.xenon.cmd.XenonCMD;
import dev.portero.xenon.listener.PlayerDataListener;
import dev.portero.xenon.locale.LocaleManager;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Xenon extends JavaPlugin {

    @Getter
    private static Xenon instance;
    @Getter
    LocaleManager localeManager;

    @Override
    public void onLoad() {
        localeManager = new LocaleManager(this);
        localeManager.reload();
    }

    @Override
    public void onEnable() {
        this.setInstance(this);
        this.loadConfig();
        this.registerCommands();
        this.registerListeners();
    }

    @Override
    public void onDisable() {
        this.setInstance(null);
    }

    private void setInstance(Xenon instance) {
        Xenon.instance = instance;
    }

    /**
     * TODO: Implement a better solution to have multiple config files
     */
    private void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("xenon")).setExecutor(new XenonCMD());
    }

    private void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerDataListener(), this);
    }
}
