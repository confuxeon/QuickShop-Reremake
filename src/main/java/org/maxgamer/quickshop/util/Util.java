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

package org.maxgamer.quickshop.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.QuickShop;

@UtilityClass
public class Util {

    private final List<String> debugLogs = new LinkedList<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final boolean devMode = false;

    private final boolean disableDebugLogger = false;

    /**
     * Print debug log when plugin running on dev mode.
     *
     * @param logs logs
     */
    public void debugLog(@NotNull final String... logs) {
        if (Util.disableDebugLogger) {
            return;
        }
        Util.lock.writeLock().lock(); //TODO MOVE IT
        if (Util.debugLogs.size() >= 2000) {
            Util.debugLogs.clear();
        }
        //TODO <- MOVE TO THERE SHOULD BE FINE
        if (!Util.devMode) {
            for (final String log : logs) {
                Util.debugLogs.add("[DEBUG] " + log);
            }
            Util.lock.writeLock().unlock();
            return;
        }
        final StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        final String className = stackTraceElement.getClassName();
        final String methodName = stackTraceElement.getMethodName();
        final int codeLine = stackTraceElement.getLineNumber();
        for (final String log : logs) {
            Util.debugLogs.add("[DEBUG] [" + className + "] [" + methodName + "] (" + codeLine + ") " + log);
            QuickShop.getInstance().getLogger().info("[DEBUG] [" + className + "] [" + methodName + "] (" + codeLine + ") " + log);
        }
        Util.lock.writeLock().unlock();
    }

}
