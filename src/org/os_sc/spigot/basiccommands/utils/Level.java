package org.os_sc.spigot.basiccommands.utils;

import org.bukkit.entity.Player;

public class Level {

	public static int getTotalXP(int level) {
		return getTotalXP(level, 0);
	}

	public static int getTotalXP(Player p) {
		return getTotalXP(p.getLevel(), p.getExp());
	}

	public static int getTotalXP(int level, float percent) {
		float a = 1, b = 6, c = 0;
		if (level >= 16) {
			if (level < 31) {
				a = 2.5f;
				b = -40.5f;
				c = 360;
			} else {
				a = 4.5f;
				b= -162.5f;
				c = 2220;
			}
		}
		int xp_l = (int) (a * (level*level) + b * level + c);
		if (percent > 0) {
			xp_l += ((a * (level+1)*(level+1) + b * (level+1) + c) - xp_l) * percent;
		}
		return xp_l;
	}

	public static void setTotalXP(Player player, int xp)
	{
		if (xp == 0) return;
		if (xp < 0) {
			takeXP(player, -xp);
			return;
		}
		double level = 0;
		if (xp < 352) {
			level = Math.sqrt(xp + 9) - 3;
		} else if (xp < 1507) {
			level = (Math.sqrt(40*xp - 7839) + 81) / 10;
		} else {
			level = (Math.sqrt(72*xp - 54215) + 325) / 18;
		}
		int lx = getTotalXP((int)level);
		float exp = (float)(xp - lx) / (float)(getTotalXP(1 + (int)level) - lx);
		player.setLevel((int)level);
		player.setExp(exp);
	}

	public static int takeXP(Player player, int xp)
	{
		setTotalXP(player, getTotalXP(player) - xp);
		return getTotalXP(player);
	}
}
