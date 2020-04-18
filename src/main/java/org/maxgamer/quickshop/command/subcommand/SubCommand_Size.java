package org.maxgamer.quickshop.command.subcommand;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.command.CommandProcesser;
import org.maxgamer.quickshop.shop.Shop;
import org.maxgamer.quickshop.util.MsgUtil;
import org.maxgamer.quickshop.util.Util;

import java.util.Collections;
import java.util.List;

public class SubCommand_Size implements CommandProcesser {


    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
        if (sender instanceof Player) {
            if (cmdArg.length == 1) {
                int amount;
                try {
                    amount = Integer.parseInt(cmdArg[0]);
                } catch (NumberFormatException e) {
                    MsgUtil.sendMessage(sender, MsgUtil.getMessage("not-a-integer", sender, cmdArg[0]));
                    return;
                }
                final BlockIterator bIt = new BlockIterator((Player) sender, 10);
                // Loop through every block they're looking at upto 10 blocks away
                if (!bIt.hasNext()) {
                    MsgUtil.sendMessage(sender, MsgUtil.getMessage("not-looking-at-shop", sender));
                    return;
                }

                while (bIt.hasNext()) {
                    final Block b = bIt.next();
                    final Shop shop = QuickShop.getInstance().getShopManager().getShop(b.getLocation());

                    if (shop != null) {
                        if (shop.getModerator().isModerator(((Player) sender).getUniqueId()) || sender.hasPermission("quickshop.other.amount")) {
                            if (amount <= 0 || amount > Util.getItemMaxStackSize(shop.getItem().getType())) {
                                MsgUtil.sendMessage(sender, MsgUtil.getMessage("command.invalid-bulk-amount", sender, Integer.toString(amount)));
                                return;
                            }
                            shop.getItem().setAmount(amount);
                            shop.refresh();
                            MsgUtil.sendMessage(sender, MsgUtil.getMessage("command.bulk-size-now", sender, Util.getItemStackName(shop.getItem()), Integer.toString(shop.getItem().getAmount())));
                        } else {
                            MsgUtil.sendMessage(sender, MsgUtil.getMessage("not-managed-shop", sender));
                        }
                    }
                }

            } else {
                MsgUtil.sendMessage(sender, MsgUtil.getMessage("command.bulk-size-not-set", sender));
            }
        } else {
            MsgUtil.sendMessage(sender, "This command can't be run by console");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
        return Collections.emptyList();
    }
}