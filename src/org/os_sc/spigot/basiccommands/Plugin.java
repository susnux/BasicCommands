package org.os_sc.spigot.basiccommands;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.os_sc.spigot.basiccommands.commands.HomeCommand;
import org.os_sc.spigot.basiccommands.commands.KillsCommand;
import org.os_sc.spigot.basiccommands.commands.TPCommand;
import org.os_sc.spigot.basiccommands.events.EntityDeath;
import org.os_sc.spigot.basiccommands.utils.Price;

import net.milkbowl.vault.permission.Permission;

public class Plugin extends JavaPlugin {
	public static Plugin instance;
	public static Permission permissions;

	// Fired when plugin is first enabled
	@Override
	public void onEnable()
	{
		this.setupPermissions();
		createConfig();
		this.getCommand("kills").setExecutor(new KillsCommand());
		this.getCommand("kills").setTabCompleter(new KillsCommand());
		this.getCommand("home").setExecutor(new HomeCommand());
		this.getCommand("sethome").setExecutor(new HomeCommand());
		this.getCommand("tp").setExecutor(new TPCommand());
		this.getCommand("tp").setExecutor(new TPCommand());
		this.getCommand("tpaccept").setExecutor(new TPCommand());
		this.getCommand("tpdeny").setExecutor(new TPCommand());
		this.getCommand("tpinfo").setExecutor(new TPCommand());
		this.getServer().getPluginManager().registerEvents(new EntityDeath(), this);
		instance = this;
		Price.init(); // last
	}

	// Fired when plugin is disabled
	@Override
	public void onDisable()
	{
		this.saveConfig();
	}
	
	public static boolean checkPermission(CommandSender sender, String permission)
	{
		if (permissions != null) {
			return permissions.has(sender, permission);
		}
		return sender.hasPermission(permission);
	}

	private void setupPermissions()
    {
		if (this.getServer().getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
	        if (permissionProvider != null) {
	            Plugin.permissions = permissionProvider.getProvider();
	        }
			if (Plugin.permissions == null) {
				getLogger().info("[BG] Vault permissions hook not found. Permissions support unavailable.");
			}
		} else {
			getLogger().info("[BG] Vault dependency not found, defaulting to Bukkit Permissions.");
		}
    }

	private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("config.yml not found, creating!");
                try {
                	saveDefaultConfig();
                } catch (IllegalArgumentException e) {
                	// If no default config write empty config
                	saveConfig();
                }
            } else {
                getLogger().info("config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}