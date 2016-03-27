package org.os_sc.spigot.basiccommands.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.os_sc.spigot.basiccommands.Plugin;

public abstract class BasicCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (requiresPlayer() && !(sender instanceof Player)) {
			say(sender, "Only ingame-players can use the >" + command.getName() + "< command.");
			return true;
		}
		if (!Plugin.checkPermission(sender, command.getPermission())) {
			say(sender, command.getPermissionMessage());
			return true;
		}
		return handleCommand(sender, command, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return null; // Dummy
	}

	protected FileConfiguration config()
	{
		return Plugin.instance.getConfig();
	}

	protected FileConfiguration data()
	{
		return Plugin.data;
	}

	protected abstract boolean handleCommand(CommandSender sender, Command command, String args[]);
	protected abstract boolean requiresPlayer();

	protected void say(CommandSender sender, String what) {
		final String bg = ChatColor.YELLOW + "[" + ChatColor.RED + "BC" + ChatColor.YELLOW + "] " + ChatColor.WHITE;
		sender.sendMessage(bg + what);
	}
}
