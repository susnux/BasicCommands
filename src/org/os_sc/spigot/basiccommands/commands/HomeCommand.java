package org.os_sc.spigot.basiccommands.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.os_sc.spigot.basiccommands.Plugin;

public class HomeCommand extends BasicCommand {

	@Override
	public boolean handleCommand(CommandSender sender, Command command, String[] args) {
		if (! (sender instanceof Player)) {
			say(sender, "Only available ingame for real players.");
			return false;
		}
		String bp = "players." + ((Player)sender).getUniqueId().toString() + ".home.";
		if (command.getName().equals("sethome")) {
			Location loc = ((Player) sender).getLocation();
			config().set(bp + "x", loc.getX());
			config().set(bp + "y", loc.getY());
			config().set(bp + "z", loc.getZ());
			config().set(bp + "world", loc.getWorld().getName());
			say(sender, String.format("Set home to: %f %f %f",
						loc.getX(), loc.getY(),loc.getZ())
			);
		} else if (command.getName().equals("home")) {
			if (!config().isSet(bp + "world")) {
				say(sender, "No home is set, use /sethome first");
				return false;
			}
			((Player)sender).teleport(new Location(
					Plugin.instance.getServer().getWorld(config().getString(bp + "world")),
					config().getDouble(bp + "x"),
					config().getDouble(bp + "y"),
					config().getDouble(bp + "z")));
		} else {
			return false;
		}
		return true;
	}

}
