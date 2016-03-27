package org.os_sc.spigot.basiccommands.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.os_sc.spigot.basiccommands.Plugin;

public class RewardInventory {
	public static Inventory Inv = Bukkit.createInventory(null, 9, "Rewards");
	public static Map<Integer, Integer> itemPrices = new HashMap<>();

	public static void init ()
	{
		List<Map<?, ?>> items = Plugin.instance.getConfig().getMapList("reward.items");
		int slot = 0;
		for (Map<?, ?> item : items) {
			if (item.keySet().size() < 1)
				continue;
			String name = (String)item.keySet().toArray()[0];
			Integer price = (Integer)item.get(name);
			createDisplay(Material.getMaterial(name), Inv, slot, null, "Price: " + price + " XP");
			Logger.debug("Added Reward item: " + name + " Price: " + price);
			itemPrices.put(slot, price);
			slot++;
		}
	}

	public static void createDisplay(Material material, Inventory inv, int Slot, String name, String lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		if (name != null)
			meta.setDisplayName(name);
		ArrayList<String> Lore = new ArrayList<String>();
		Lore.add(lore);
		meta.setLore(Lore);
		item.setItemMeta(meta);
		inv.setItem(Slot, item); 
	}
}
