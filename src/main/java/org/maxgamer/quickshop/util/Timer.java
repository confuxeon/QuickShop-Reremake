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

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Timer {

    private long startTime;

    /**
     * Create a empty time, use setTimer to start
     */
    public Timer() {
    }

    /**
     * Create a empty time, auto start if autoSet is true
     *
     * @param autoSet Auto set the timer
     */
    public Timer(final boolean autoSet) {
        if (autoSet) {
            this.startTime = System.currentTimeMillis();
        }
    }

    /**
     * Create a empty time, use the param to init the startTime.
     *
     * @param startTime New startTime
     */
    public Timer(final long startTime) {
        this.startTime = startTime;
    }

    /**
     * Return how long time running when timer set and destory the timer.
     *
     * @return time
     */
    public long endTimer() {
        final long time = System.currentTimeMillis() - this.startTime;
        this.startTime = 0;
        return time;
    }

    /**
     * Return how long time running after atTimeS. THIS NOT WILL DESTORY AND STOP THE TIMER
     *
     * @param atTime The inited time
     * @return time
     */
    public long getTimerAt(final long atTime) {
        return atTime - this.startTime;
    }

    /**
     * Create a Timer. Time Unit: ms
     */
    public void setTimer() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Return how long time running when timer set. THIS NOT WILL DESTORY AND STOP THE TIMER
     *
     * @return time
     */
    public long getTimer() {
        return System.currentTimeMillis() - this.startTime;
    }

}