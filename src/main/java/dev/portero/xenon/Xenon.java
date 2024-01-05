package dev.portero.xenon;

import dev.portero.xenon.cmd.XenonCMD;
import dev.portero.xenon.config.ConfigKeys;
import dev.portero.xenon.config.ConfigManager;
import dev.portero.xenon.data.Data;
import dev.portero.xenon.listener.PlayerDataListener;
import dev.portero.xenon.locale.LocaleManager;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.Objects;
import java.util.logging.Level;

public class Xenon extends JavaPlugin {

    @Getter
    private static Xenon instance;
    @Getter
    LocaleManager localeManager;
    @Getter
    private Data data;
    @Getter
    private ConfigManager configManager;

    private Instant startTime;

    @Override
    public void onLoad() {
        this.startTime = Instant.now();
        localeManager = new LocaleManager(this);
        localeManager.reload();
    }

    @Override
    public void onEnable() {
        this.setInstance(this);
        this.loadStorage();
        this.registerCommands();
        this.registerListeners();
        this.getLogger().log(Level.INFO, "Xenon has been enabled in (" + (Instant.now().toEpochMilli() - this.startTime.toEpochMilli()) + ")ms");
    }

    @Override
    public void onDisable() {
        data.shutdown();
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

    private void loadDatabase() {
        this.data = new Data(this, ConfigKeys.DATABASE_VALUES, ConfigKeys.SQL_TABLE_PREFIX);
        this.data.init();
    }

    private void loadStorage() {
        this.configManager = new ConfigManager(this);
        this.loadConfig();
        this.loadDatabase();

        this.getLogger().log(Level.WARNING, ConfigKeys.EXAMPLE);
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("xenon")).setExecutor(new XenonCMD());
    }

    private void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerDataListener(), this);
    }
}
