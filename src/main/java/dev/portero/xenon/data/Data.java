package dev.portero.xenon.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.portero.xenon.Xenon;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Data {

    private final DataCredentials credentials;
    private final String tablePrefix;
    private HikariDataSource hikari;
    private final Function<String, String> statementProcessor;
    private final Xenon plugin;

    public Data(Xenon plugin, DataCredentials credentials, String tablePrefix) {
        this.plugin = plugin;
        this.credentials = credentials;
        this.tablePrefix = tablePrefix;

        this.statementProcessor = s -> s.replace("{prefix}", this.tablePrefix);
    }

    public void init() {
        HikariConfig config;
        try {
            config = new HikariConfig();
        } catch (LinkageError e) {
            throw new RuntimeException("Failed to create HikariConfig instance", e);
        }

        config.setPoolName("xenon-hikari");

        String[] addressSplit = this.credentials.address().split(":");
        String address = addressSplit[0];
        String port = addressSplit.length > 1 ? addressSplit[1] : "5432";

        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(String.format("jdbc:%s://%s:%s/%s", "postgresql", address, port, this.credentials.database()));
        config.setUsername(this.credentials.username());
        config.setPassword(this.credentials.password());

        Map<String, Object> properties = new HashMap<>(this.credentials.properties());

        properties.putIfAbsent("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

        for (Map.Entry<String, Object> property : properties.entrySet()) {
            config.addDataSourceProperty(property.getKey(), property.getValue());
        }

        config.setMaximumPoolSize(this.credentials.maxPoolSize());
        config.setMinimumIdle(this.credentials.minIdleConnections());
        config.setMaxLifetime(this.credentials.maxLifetime());
        config.setKeepaliveTime(this.credentials.keepAliveTime());
        config.setConnectionTimeout(this.credentials.connectionTimeout());

        config.setInitializationFailTimeout(-1);

        this.hikari = new HikariDataSource(config);

        boolean tableExists;
        try (Connection c = this.getConnection()) {
            tableExists = this.tableExists(c, this.statementProcessor.apply("{prefix}players"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!tableExists) {
            this.applySchema();
        }
    }

    public void shutdown() {
        if (this.hikari != null) {
            this.hikari.close();
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.hikari == null) {
            throw new SQLException("Unable to get a connection from the pool. (hikari is null)");
        }

        Connection connection = this.hikari.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to get a connection from the pool. (getConnection returned null)");
        }

        return connection;
    }

    public DataMetadata getMetadata() {
        DataMetadata metadata = new DataMetadata();

        boolean success = true;
        long start = System.currentTimeMillis();

        try (Connection c = getConnection()) {
            try (Statement s = c.createStatement()) {
                s.execute("/* ping */ SELECT 1");
            }
        } catch (SQLException e) {
            success = false;
        }

        if (success) {
            int duration = (int) (System.currentTimeMillis() - start);
            metadata.ping(duration);
        }

        metadata.connected(success);
        return metadata;
    }

    private boolean tableExists(Connection connection, String table) throws SQLException {
        try (ResultSet rs = connection.getMetaData().getTables(connection.getCatalog(), null, "%", null)) {
            while (rs.next()) {
                if (rs.getString(3).equalsIgnoreCase(table)) {
                    return true;
                }
            }
            return false;
        }
    }

    private void applySchema() {
        List<String> statements;

        String schemaFileName = "schema/postgresql.sql";
        try (InputStream is = this.plugin.getResource(schemaFileName)) {
            if (is == null) {
                throw new IOException("Couldn't locate schema file for " + "PostgreSQL" + " - " + schemaFileName);
            }

            statements = SchemaReader.getStatements(is).stream()
                    .map(this.statementProcessor)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = this.getConnection()) {
            boolean utf8mb4Unsupported = false;

            try (Statement s = connection.createStatement()) {
                for (String query : statements) {
                    s.addBatch(query);
                }

                try {
                    s.executeBatch();
                } catch (BatchUpdateException e) {
                    if (e.getMessage().contains("Unknown character set")) {
                        utf8mb4Unsupported = true;
                    } else {
                        throw e;
                    }
                }
            }

            if (utf8mb4Unsupported) {
                try (Statement s = connection.createStatement()) {
                    for (String query : statements) {
                        s.addBatch(query.replace("utf8mb4", "utf8"));
                    }

                    s.executeBatch();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
