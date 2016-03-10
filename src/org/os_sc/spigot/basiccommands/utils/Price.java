package org.os_sc.spigot.basiccommands.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.os_sc.spigot.basiccommands.Plugin;

public class Price {

	public static void setStartPrice(int p)
	{
		startPrice = p;
	}

	public static int getPrice(Player player)
	{
		if (prices.containsKey(player)) {
			long local = Plugin.instance.getServer().getWorlds().get(0).getFullTime();
			long setted = prices.get(player).second;
			int price = prices.get(player).first;
			while ((local - setted) > decreaseTime) {
				price /= factor;
				setted += decreaseTime;
			}
			if (price < startPrice) {
				prices.remove(player);
				price = startPrice;
			} else {
				prices.put(player, new Pair<Integer, Long>(price, setted));
			}
			return price;
		} else
			return startPrice;
	}

	public static int increment(Player player)
	{
		long time = Plugin.instance.getServer().getWorlds().get(0).getFullTime();
		int price = getPrice(player) * factor;
		prices.put(player, new Pair<Integer, Long>(price, time));
		return price;
	}
	
	public static long getDecreaseTime()
	{
		return decreaseTime;
	}
	
	public static long getTime(Player p)
	{
		if (!prices.containsKey(p))
			return Plugin.instance.getServer().getWorlds().get(0).getFullTime();
		return prices.get(p).second;
	}

	public static void init()
	{
		FileConfiguration conf = Plugin.instance.getConfig();
		if (conf.isSet("config.teleport.startPrice"))
			startPrice = conf.getInt("config.teleport.startPrice");
		if (conf.isSet("config.teleport.factor"))
			factor = conf.getInt("config.teleport.factor");
		if (conf.isSet("config.teleport.decreaseTime"))
			decreaseTime = conf.getInt("config.teleport.decreaseTime");
	}

	private static int startPrice = 55;
	private static int factor = 2;
	private static long decreaseTime = 12000;
	private static Map<Player, Pair<Integer, Long>> prices = new HashMap<Player, Pair<Integer, Long>>();

}
