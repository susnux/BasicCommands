package org.os_sc.spigot.basiccommands.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.os_sc.spigot.basiccommands.Plugin;
import org.os_sc.spigot.basiccommands.utils.Level;
import org.os_sc.spigot.basiccommands.utils.Price;
import org.os_sc.spigot.basiccommands.utils.Teleporter;

public class TPCommand extends BasicCommand {
	@Override
	public boolean handleCommand(CommandSender sender, Command command, String[] args) {
		if (command.getName().equals("tpaccept")) handleTPAns((Player) sender, true);
		else if (command.getName().equals("tpdeny")) handleTPAns((Player) sender, false);
		else if (command.getName().equals("tpinfo")) handleTPInfo((Player) sender);
		else if (command.getName().equals("tp")) {
			if(args.length == 1)
				handleTP((Player) sender, args[0]);
			else
				return false;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!command.getName().equals("tp"))
			return null;
		List<String> ops = new LinkedList<String>();
		for (Player op : Plugin.instance.getServer().getOnlinePlayers())
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

	private static Map<Player, Queue<Player>> requests = new HashMap<Player, Queue<Player>>();;

	private void handleTPInfo(Player player) {
		int price = Price.getPrice(player);
		ChatColor color = price < Level.getTotalXP(player) ? ChatColor.GREEN : ChatColor.RED;
		long local = Plugin.instance.getServer().getWorlds().get(0).getFullTime();
		long hours = (Price.getDecreaseTime() - (local - Price.getTime(player))) / 1000;
		say(player,
			"Your current EXP: " + ChatColor.GREEN + Level.getTotalXP(player) + ChatColor.WHITE+ "\n" +
			(data().isSet("players." + player.getUniqueId().toString() + ".tp.last") ? "Current /ret will cost: " + (int)(data().getInt("players." + player.getUniqueId().toString() + ".tp.last.price") * config().getDouble("teleport.return.factor", 0.5) + 0.5) + "\n" : "") +
			"Teleporation will cost: " + color + price + ChatColor.WHITE + "\n" +
			"Next price reduction in " + hours + " hours.");
	}

	private void handleTPAns(Player sender, boolean answer)
	{
		if (!requests.containsKey(sender)) {
			say(sender, "No requests.");
			return;
		}
		Queue<Player> q = requests.get(sender);
		if (q.isEmpty()) {
			say(sender,"No pending teleport.");
			return;
		}
		if (!answer) {
			say(q.remove(), "Teleporation got declined");
		} else {
			Player p = q.remove();
			if (Level.getTotalXP(p) < Price.getPrice(p)) {
				say(sender, "Player " + p.getName() + " can not affort this teleport.");
				say(p,  "Teleport got aborted, you can not affort this teleport.\n" +
						"It costs: " + Price.getPrice(p) + " XP");
				return;
			}
			Level.takeXP(p, Price.getPrice(p));
			if (Teleporter.teleport(p, sender))
				Price.increment(p);
		}
	}

	private void handleTP(Player sender, String target)
	{
		Player player;
		if ((player = Plugin.instance.getServer().getPlayer(target)) == null) {
			say(sender, "Player: " + target + " not found.");
			return;
		}
		createRequest(sender, player);
	}

	private void createRequest(Player sender, Player target) {
		if (requests.containsKey(target)) {
			requests.get(target).add(sender);
		} else {
			Queue<Player> p = new LinkedList<Player>();
			p.add(sender);
			requests.put(target, p);
		}
		say(target, "You have got an teleporation request from: " + sender.getName() +
				".\nUse /tpaccept or /tpdeny");
	}

	@Override
	protected boolean requiresPlayer() {
		return true;
	}
}
