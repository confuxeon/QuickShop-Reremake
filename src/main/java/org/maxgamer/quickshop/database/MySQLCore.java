/*
 * QuickShop
 * Copyright (C) 2020  Bukkit Commons Studio and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.maxgamer.quickshop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.maxgamer.quickshop.QuickShop;

public class MySQLCore implements DatabaseCore {

    private final List<Connection> POOL = new ArrayList<>();

    private final int MAX_CONNECTIONS = 8;

    /**
     * The connection properties... user, pass, autoReconnect..
     */
    @NotNull
    private final Properties info;

    @NotNull
    private final String url;

    @NotNull
    private final QuickShop plugin;

    public MySQLCore(
        @NotNull final QuickShop plugin,
        @NotNull final String host,
        @NotNull final String user,
        @NotNull final String pass,
        @NotNull final String database,
        @NotNull final String port,
        final boolean useSSL) {
        this.plugin = plugin;
        this.info = new Properties();
        this.info.setProperty("autoReconnect", "true");
        this.info.setProperty("user", user);
        this.info.setProperty("password", pass);
        this.info.setProperty("useUnicode", "true");
        this.info.setProperty("characterEncoding", "utf8");
        this.info.setProperty("useSSL", String.valueOf(useSSL));
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        for (int i = 0; i < this.MAX_CONNECTIONS; i++) {
            this.POOL.add(null);
        }
    }

    @Override
    public void close() {
        // Nothing, because queries are executed immediately for MySQL
    }

    @Override
    public void flush() {
        // Nothing, because queries are executed immediately for MySQL
    }

    @Override
    public void queue(@NotNull final BufferStatement bs) {
        try {
            Connection con = this.getConnection();
            while (con == null) {
                try {
                    Thread.sleep(15);
                } catch (final InterruptedException e) {
                    // ignore
                }
                // Try again
                con = this.getConnection();
            }
            final PreparedStatement ps = bs.prepareStatement(con);
            ps.execute();
            ps.close();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the database connection for executing queries on.
     *
     * @return The database connection
     */
    @Nullable
    @Override
    public Connection getConnection() {
        for (int i = 0; i < this.MAX_CONNECTIONS; i++) {
            Connection connection = this.POOL.get(i);
            try {
                // If we have a current connection, fetch it
                if (connection != null && !connection.isClosed()) {
                    if (connection.isValid(10)) {
                        return connection;
                    }
                    // Else, it is invalid, so we return another connection.
                }
                connection = DriverManager.getConnection(this.url, this.info);

                this.POOL.set(i, connection);
                return connection;
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public @NotNull String getName() {
        return "BuiltIn-MySQL";
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

}