package org.os_sc.spigot.basiccommands.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.os_sc.spigot.basiccommands.Plugin;

public class EntityDeath implements Listener {
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
	     LivingEntity entity = event.getEntity();
		    if(entity.getType() == EntityType.PLAYER) {
	    	Plugin.data.set("players." + entity.getUniqueId().toString() + ".kills.current",
	    			new Integer(0));
	    }
	    if (entity.getKiller() != null) {
	    	String uuid = entity.getKiller().getUniqueId().toString();
	    	Plugin.data.set("players." + uuid + ".kills.current",
	    			new Integer(Plugin.data.getInt("players." + uuid + ".kills.current") + 1));
	    	Plugin.data.set("players." + uuid + ".kills.total",
	    			new Integer(Plugin.data.getInt("players." + uuid + ".kills.total") + 1));
	    }
	}
}
