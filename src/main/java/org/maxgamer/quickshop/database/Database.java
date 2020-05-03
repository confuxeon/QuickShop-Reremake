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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Cleanup;
import org.jetbrains.annotations.NotNull;

public class Database {

    @NotNull
    private final DatabaseCore core;

    /**
     * Creates a new database and validates its connection.
     *
     * <p>If the connection is invalid, this will throw a ConnectionException.
     *
     * @param core The core for the database, either MySQL or SQLite.
     * @throws Database.ConnectionException If the connection was invalid
     */
    public Database(@NotNull final DatabaseCore core) throws Database.ConnectionException {
        try {
            try {
                if (!core.getConnection().isValid(30)) {
                    throw new Database.ConnectionException("The database does not appear to be valid!");
                }
            } catch (final AbstractMethodError ignored) {
                // You don't need to validate this core.
            }
        } catch (final SQLException e) {
            throw new Database.ConnectionException(e.getMessage());
        }
        this.core = core;
    }

    /**
     * Closes the database
     */
    public void close() {
        this.core.close();
    }

    /**
     * Executes the given statement either immediately, or soon.
     *
     * @param query The query
     * @param objs The string values for each ? in the given query.
     */
    public void execute(@NotNull final String query, @NotNull final Object... objs) {
        final BufferStatement bs = new BufferStatement(query, objs);
        this.core.queue(bs);
    }

    /**
     * Returns true if the given table has the given column
     *
     * @param table The table
     * @param column The column
     * @return True if the given table has the given column
     * @throws SQLException If the database isn't connected
     */
    public boolean hasColumn(@NotNull final String table, @NotNull final String column) throws SQLException {
        if (!this.hasTable(table)) {
            return false;
        }
        final String query = "SELECT * FROM " + table + " LIMIT 0,1";
        try {
            @Cleanup final PreparedStatement ps = this.getConnection().prepareStatement(query);
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString(column) != null) {
                    return true;
                }
            }
        } catch (final SQLException e) {
            return false;
        }
        return false; // Uh, wtf.
    }

    /**
     * Fetches the connection to this database for querying. Try to avoid doing this in the main
     * thread.
     *
     * @return Fetches the connection to this database for querying.
     */
    public Connection getConnection() {
        return this.core.getConnection();
    }

    /**
     * Returns the database core object, that this database runs on.
     *
     * @return the database core object, that this database runs on.
     */
    @NotNull
    public DatabaseCore getCore() {
        return this.core;
    }

    /**
     * Returns true if the table exists
     *
     * @param table The table to check for
     * @return True if the table is found
     * @throws SQLException Throw exception when failed execute somethins on SQL
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean hasTable(@NotNull final String table) throws SQLException {
        final ResultSet rs = this.getConnection().getMetaData().getTables(null, null, "%", null);
        while (rs.next()) {
            if (table.equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                rs.close();
                return true;
            }
        }
        rs.close();
        return false;
    }

    /**
     * Represents a connection error, generally when the server can't connect to MySQL or something.
     */
    public static class ConnectionException extends Exception {

        private static final long serialVersionUID = 8348749992936357317L;

        private ConnectionException(final String msg) {
            super(msg);
        }

    }

}