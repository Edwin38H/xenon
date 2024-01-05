package dev.portero.xenon.data;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record DataCredentials(String address, String database, String username, String password, int maxPoolSize,
                              int minIdleConnections, int maxLifetime, int keepAliveTime, int connectionTimeout,
                              @NotNull Map<String, Object> properties) {

    @Override
    public String address() {
        return this.address;
    }

    @Override
    public String database() {
        return this.database;
    }

    @Override
    public String username() {
        return this.username;
    }

    @Override
    public String password() {
        return this.password;
    }

    @Override
    public String toString() {
        return "DatabaseCredentials{" +
                "address='" + address + '\'' +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", maxPoolSize=" + maxPoolSize +
                ", minIdleConnections=" + minIdleConnections +
                ", maxLifetime=" + maxLifetime +
                ", keepAliveTime=" + keepAliveTime +
                ", connectionTimeout=" + connectionTimeout +
                ", properties=" + properties +
                '}';
    }
}
