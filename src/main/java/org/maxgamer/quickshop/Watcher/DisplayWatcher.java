package org.maxgamer.quickshop.Watcher;

import java.util.ArrayList;
import java.util.Iterator;

import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.Shop.Shop;
import org.maxgamer.quickshop.Util.Util;

@Data
public class DisplayWatcher {
    private QuickShop plugin;
    private ArrayList<Shop> pendingCheckDisplay = new ArrayList<>();

    public DisplayWatcher(QuickShop plugin) {
        this.plugin = plugin;
        registerTask();
    }

    private void registerTask() {
        plugin.getLogger().info("Registering DisplayCheck task....");
        if (plugin.isDisplay() && plugin.getDisplayItemCheckTicks() > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (plugin.getConfig().getInt("shop.display-items-check-ticks") < 3000)
                        plugin.getLogger()
                                .severe("Shop.display-items-check-ticks is too low! It may cause HUGE lag! Pick a number > 3000");
                    Iterator<Shop> it = plugin.getShopManager().getShopIterator();
                    while (it.hasNext()) {
                        Shop shop = it.next();
                        if (shop == null)
                            continue;
                        if (!shop.isLoaded())
                            continue;
                        if (!Util.isLoaded(shop.getLocation()))
                            continue;
                        pendingCheckDisplay.add(shop);
                    }
                    Bukkit.getScheduler().runTask(plugin, new DisplayRunnable());
                }
            }.runTaskTimerAsynchronously(plugin, 1L, plugin.getDisplayItemCheckTicks());
        }
    }

}

class DisplayRunnable implements Runnable {
    @Override
    public void run() {
        @SuppressWarnings("unchecked") ArrayList<Shop> pendingCheckDisplayCopy = (ArrayList<Shop>) QuickShop.instance
                .getDisplayWatcher().getPendingCheckDisplay().clone();
        for (Shop shop : pendingCheckDisplayCopy) {
            shop.checkDisplay();
        }
        pendingCheckDisplayCopy.clear();
        QuickShop.instance.getDisplayWatcher().getPendingCheckDisplay().clear();
    }
}