package org.os_sc.spigot.basiccommands.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.os_sc.spigot.basiccommands.Plugin;

public class Teleporter {
	public static boolean teleport(Player p, Player q)
	{
		return teleport(p, q.getLocation());
	}

	public static boolean teleport(Player p, Location l)
	{
		Location pl = p.getLocation();
		boolean success = p.teleport(l);
		if (success) {
			String bp = "players." + p.getUniqueId().toString() + ".tp.last.";
			Plugin.data.set(bp + "x", pl.getX());
			Plugin.data.set(bp + "y", pl.getY());
			Plugin.data.set(bp + "z", pl.getZ());
			Plugin.data.set(bp + "world", pl.getWorld().getName());
			Plugin.data.set(bp + "price", Price.getPrice(p));
		}
		return success;
	}
}
