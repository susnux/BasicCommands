package org.os_sc.spigot.basiccommands.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.os_sc.spigot.basiccommands.Plugin;
import org.os_sc.spigot.basiccommands.utils.Teleporter;

public class HomeCommand extends BasicCommand {

	@Override
	public boolean handleCommand(CommandSender sender, Command command, String[] args) {
		String bp = "players." + ((Player)sender).getUniqueId().toString() + ".home.";
		if (command.getName().equals("sethome")) {
			Location loc = ((Player) sender).getLocation();
			data().set(bp + "x", loc.getX());
			data().set(bp + "y", loc.getY());
			data().set(bp + "z", loc.getZ());
			data().set(bp + "world", loc.getWorld().getName());
			say(sender, String.format("Set home to: %f %f %f",
						loc.getX(), loc.getY(),loc.getZ())
			);
		} else if (command.getName().equals("home")) {
			if (!data().isSet(bp + "world")) {
				say(sender, "No home is set, use /sethome first");
				return false;
			}
			Teleporter.teleport((Player)sender, new Location(
					Plugin.instance.getServer().getWorld(data().getString(bp + "world")),
					data().getDouble(bp + "x"),
					data().getDouble(bp + "y"),
					data().getDouble(bp + "z")));
		} else {
			return false;
		}
		return true;
	}

	@Override
	protected boolean requiresPlayer() {
		return true;
	}

}
