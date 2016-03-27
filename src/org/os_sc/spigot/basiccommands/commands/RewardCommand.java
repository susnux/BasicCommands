package org.os_sc.spigot.basiccommands.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.os_sc.spigot.basiccommands.utils.RewardInventory;

public class RewardCommand extends BasicCommand {
	@Override
	protected boolean handleCommand(CommandSender sender, Command command, String[] args) {
		Player player = (Player) sender;
		player.openInventory(RewardInventory.Inv);
		return true;
	}

	@Override
	protected boolean requiresPlayer() {
		return true;
	}

}
