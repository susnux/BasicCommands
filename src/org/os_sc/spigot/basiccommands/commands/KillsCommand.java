package org.os_sc.spigot.basiccommands.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.os_sc.spigot.basiccommands.Plugin;

public class KillsCommand extends BasicCommand {

	@Override
	public boolean handleCommand(CommandSender sender, Command cmd, String[] arguments) {
		if (sender instanceof Player) {
			if (arguments.length > 1)
				return false;
			String uuid = null;
			if (arguments.length == 0) {
				if (!Plugin.checkPermission(sender, "basiccommands.kills.own")) {
					say(sender, ChatColor.RED + "You do not have the permission to do this!");
					return true;
				}
				uuid = ((Player) sender).getUniqueId().toString();
			} else {
				if (!Plugin.checkPermission(sender, "basiccommands.kills.other")) {
					say(sender, ChatColor.RED + "You do not have the permission to do this!");
					return true;
				}
				if (Plugin.instance.getServer().getPlayer(arguments[0]) == null) {
					for (OfflinePlayer pl : Plugin.instance.getServer().getOfflinePlayers()) {
						if (pl.getName().equals(arguments[0])) {
							uuid = pl.getUniqueId().toString();
							break;
						}
					}
					if (uuid == null) {
						say(sender, "No player " + arguments[0] + " found.");
						return true;
					}
				} else {
					uuid = Plugin.instance.getServer().getPlayer(arguments[0]).getUniqueId().toString();
				}
			}
			int now = data().getInt("players." + uuid + ".kills.current");
			int total = data().getInt("players." + uuid + ".kills.total");
			say(sender, formatMsg(now, total));
		}
		return true;
	}

	private String formatMsg(int current, int total)
	{
		return  "Current strike is " + current + " kills."+
				" And " + total + " total.";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> ops = new LinkedList<String>();
		for (OfflinePlayer op : Plugin.instance.getServer().getOfflinePlayers())
			ops.add(op.getName());
		if (args.length > 1)
			return null;
		if (args.length == 1) {
			for (Iterator<String> itr = ops.iterator(); itr.hasNext();)
				if (!itr.next().startsWith(args[0]))
					itr.remove();
		}
		return ops;
	}

	@Override
	protected boolean requiresPlayer() {
		return false;
	}
}
