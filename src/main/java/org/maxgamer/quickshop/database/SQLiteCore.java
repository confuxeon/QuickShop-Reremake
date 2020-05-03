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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Objects;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.maxgamer.quickshop.QuickShop;

public class SQLiteCore implements DatabaseCore {

    private final File dbFile;

    private final LinkedList<BufferStatement> queue = new LinkedList<>();

    @NotNull
    private final QuickShop plugin;

    private Connection connection;

    private volatile Thread watcher;

    public SQLiteCore(@NotNull final QuickShop plugin, @NotNull final File dbFile) {
        this.plugin = plugin;
        this.dbFile = dbFile;
    }

    @Override
    public void close() {
        this.flush();
    }

    @Override
    public void flush() {
        while (!this.queue.isEmpty()) {
            final BufferStatement bs;
            synchronized (this.queue) {
                bs = this.queue.removeFirst();
            }
            synchronized (this.dbFile) {
                try {
                    final PreparedStatement ps = bs.prepareStatement(Objects.requireNonNull(this.getConnection()));
                    ps.execute();
                    ps.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void queue(@NotNull final BufferStatement bs) {
        synchronized (this.queue) {
            this.queue.add(bs);
        }
        if (this.watcher == null || !this.watcher.isAlive()) {
            this.startWatcher();
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
        try {
            // If we have a current connection, fetch it
            if (this.connection != null && !this.connection.isClosed()) {
                return this.connection;
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        if (this.dbFile.exists()) {
            // So we need a new connection
            try {
                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.dbFile);
                return this.connection;
            } catch (final ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // So we need a new file too.
            try {
                // Create the file
                this.dbFile.createNewFile();
                // Now we won't need a new file, just a connection.
                // This will return that new connection.
                return this.getConnection();
            } catch (final IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public @NotNull String getName() {
        return "BuiltIn-SQLite";
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    private void startWatcher() {
        this.watcher =
            new Thread(
                () -> {
                    try {
                        Thread.sleep(30000);
                    } catch (final InterruptedException e) {
                        // ignore
                    }
                    this.flush();
                });
        this.watcher.start();
    }

}