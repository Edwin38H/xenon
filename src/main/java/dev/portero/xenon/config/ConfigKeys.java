package dev.portero.xenon.config;

import dev.portero.xenon.Xenon;
import dev.portero.xenon.data.DataCredentials;

import java.util.Objects;

public class ConfigKeys {

    private static final Xenon plugin = Xenon.getInstance();

    public static final DataCredentials DATABASE_VALUES = new DataCredentials(
            plugin.getConfig().getString("data.address"),
            plugin.getConfig().getString("data.database"),
            plugin.getConfig().getString("data.username"),
            plugin.getConfig().getString("data.password"),
            plugin.getConfig().getInt("data.pool-settings.maximum-pool-size"),
            plugin.getConfig().getInt("data.pool-settings.minimum-idle"),
            plugin.getConfig().getInt("data.pool-settings.maximum-lifetime"),
            plugin.getConfig().getInt("data.pool-settings.keepalive-time"),
            plugin.getConfig().getInt("data.pool-settings.connection-timeout"),
            Objects.requireNonNull(plugin.getConfig().getConfigurationSection("data.properties")).getValues(false)
    );

    public static final String SQL_TABLE_PREFIX = plugin.getConfig().getString("data.table-prefix");
}

