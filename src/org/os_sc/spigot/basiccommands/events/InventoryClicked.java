package org.os_sc.spigot.basiccommands.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.os_sc.spigot.basiccommands.utils.Level;
import org.os_sc.spigot.basiccommands.utils.RewardInventory;

public class InventoryClicked implements Listener {
	@EventHandler
	void onInventoryClicked(InventoryClickEvent event)
	{
		if (event.getInventory().equals(RewardInventory.Inv)) {
			if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			int price = RewardInventory.itemPrices.get(event.getSlot());
			Player player = (Player) event.getWhoClicked();
			if (price > Level.getTotalXP(player)) {
				player.sendMessage(
					ChatColor.YELLOW + "[" + ChatColor.RED + "BC" + ChatColor.YELLOW + "] " +
					ChatColor.WHITE + "You can not afford this, it costs " + price
				);
			} else {
				Level.takeXP(player, price);
				player.getInventory().addItem(new ItemStack(event.getCurrentItem().getType()));
			}
			event.setCancelled(true);
		}
	}
}
