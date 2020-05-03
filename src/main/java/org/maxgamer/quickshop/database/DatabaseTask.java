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
import java.sql.SQLException;
import lombok.NonNull;
import lombok.ToString;
import org.maxgamer.quickshop.QuickShop;

@ToString
public class DatabaseTask {

    private final Database database = QuickShop.getInstance().getDatabase();

    private final String statement;

    private final DatabaseTask.Task task;

    public DatabaseTask(final String statement, final DatabaseTask.Task task) {
        this.statement = statement;
        this.task = task;
    }

    public void run() {
        try (final PreparedStatement ps = this.database.getConnection().prepareStatement(this.statement)) {
            this.task.edit(ps);
            ps.execute();
            this.task.onSuccess();
        } catch (final SQLException e) {
            this.task.onFailed(e);
        }
    }

    public void run(@NonNull final Connection connection) {
        try (final PreparedStatement ps = connection.prepareStatement(this.statement)) {
            this.task.edit(ps);
            ps.execute();
            this.task.onSuccess();
        } catch (final SQLException e) {
            this.task.onFailed(e);
        }
    }

    interface Task {

        void edit(PreparedStatement ps) throws SQLException;

        default void onSuccess() {
        }

        default void onFailed(final SQLException e) {
            e.printStackTrace();
        }

    }

}