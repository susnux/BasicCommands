package org.os_sc.spigot.basiccommands.commands;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.os_sc.spigot.basiccommands.Plugin;

public abstract class BasicCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!Plugin.checkPermission(sender, command.getPermission())) {
			say(sender, command.getPermissionMessage());
			return true;
		}
		return handleCommand(sender, command, args);
	}

	protected FileConfiguration config()
	{
		return Plugin.instance.getConfig();
	}

	protected abstract boolean handleCommand(CommandSender sender, Command command, String args[]);

	protected void say(CommandSender sender, String what) {
		final String bg = ChatColor.YELLOW + "[" + ChatColor.RED + "BG" + ChatColor.YELLOW + "] " + ChatColor.WHITE;
		sender.sendMessage(bg + what);
	}

	protected void log_info(String txt)
	{
		Plugin.instance.getLogger().log(Level.INFO, "[BG] " + txt);
	}

	protected void log_warn(String txt)
	{
		Plugin.instance.getLogger().log(Level.WARNING, ChatColor.YELLOW + "[BG] " + ChatColor.WHITE + txt);
	}

	protected void log_error(String txt)
	{
		Plugin.instance.getLogger().log(Level.WARNING, ChatColor.RED + "[BG] " + txt);
	}
}
