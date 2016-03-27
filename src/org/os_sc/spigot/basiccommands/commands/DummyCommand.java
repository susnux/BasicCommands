package org.os_sc.spigot.basiccommands.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DummyCommand extends BasicCommand {

	@Override
	protected boolean handleCommand(CommandSender sender, Command command, String[] args) {
		say(sender, ChatColor.RED + command.getName() + " is disabled, if you think this is a mistake contact the admin." + ChatColor.WHITE);
		return true;
	}

	@Override
	protected boolean requiresPlayer() {
		return false;
	}

}
