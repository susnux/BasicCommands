package org.os_sc.spigot.basiccommands.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.os_sc.spigot.basiccommands.Plugin;
import org.os_sc.spigot.basiccommands.utils.Level;

public class ReturnCommand extends BasicCommand {

	@Override
	protected boolean handleCommand(CommandSender sender, Command command, String[] args) {
		String bp = "players." + ((Player)sender).getUniqueId().toString() + ".tp.last";
		if (!data().isSet(bp)) {
			say(sender, "No previous teleport");
			return true;
		}
		if (config().getBoolean("teleport.return.costs")) {
			int price = (int)(data().getInt(bp + ".price") * config().getDouble("teleport.return.factor") + 0.5);
			if (Level.getTotalXP(((Player)sender)) < price) {
				say(sender, "Teleport got aborted, you can not affort this teleport.\n" +
						    "It costs: " + price + " XP");
				return true;
			}
			Level.takeXP(((Player)sender), price);
		}
		((Player)sender).teleport(new Location(Plugin.instance.getServer().getWorld(
				data().getString(bp + ".world")),
				data().getInt(bp + ".x"),
				data().getInt(bp + ".y"),
				data().getInt(bp + ".z")));
		data().set("players." + ((Player)sender).getUniqueId().toString() + ".tp.last", null);
		return true;
	}

	@Override
	protected boolean requiresPlayer() {
		return true;
	}
}
