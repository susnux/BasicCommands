package org.os_sc.spigot.basiccommands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.os_sc.spigot.basiccommands.commands.BasicCommand;
import org.os_sc.spigot.basiccommands.commands.DummyCommand;
import org.os_sc.spigot.basiccommands.commands.HomeCommand;
import org.os_sc.spigot.basiccommands.commands.KillsCommand;
import org.os_sc.spigot.basiccommands.commands.ReturnCommand;
import org.os_sc.spigot.basiccommands.commands.RewardCommand;
import org.os_sc.spigot.basiccommands.commands.TPCommand;
import org.os_sc.spigot.basiccommands.events.EntityDeath;
import org.os_sc.spigot.basiccommands.events.InventoryClicked;
import org.os_sc.spigot.basiccommands.utils.Data;
import org.os_sc.spigot.basiccommands.utils.Logger;
import org.os_sc.spigot.basiccommands.utils.Price;
import org.os_sc.spigot.basiccommands.utils.RewardInventory;

import net.milkbowl.vault.permission.Permission;

public class Plugin extends JavaPlugin {
	public static Plugin instance;
	public static Permission permissions;
	public static Data data;

	private static final Map<String, Class<? extends BasicCommand>> cmds = new HashMap<>();
	static {
		cmds.put("home", HomeCommand.class);
		cmds.put("sethome", HomeCommand.class);
		cmds.put("kills", KillsCommand.class);
		cmds.put("tp", TPCommand.class);
		cmds.put("tpinfo", TPCommand.class);
		cmds.put("tpdeny", TPCommand.class);
		cmds.put("tpaccept", TPCommand.class);
		cmds.put("ret", ReturnCommand.class);
		cmds.put("reward", RewardCommand.class);
	}

	// Fired when plugin is first enabled
	@Override
	public void onEnable()
	{
		instance = this;
		Logger.init(this.getLogger());
		createFiles();
		List<String> enabledCmds = this.getConfig().getStringList("commands");
		if (enabledCmds == null || enabledCmds.size() == 0) {
			Logger.error("No commands enabled, shutting down");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		this.setupPermissions();
		enabledCmds.retainAll(cmds.keySet());
		String ecmds = "";
		for (String cmd : enabledCmds) {
			try {
				this.getCommand(cmd).setExecutor(cmds.get(cmd).newInstance());
				this.getCommand(cmd).setTabCompleter(cmds.get(cmd).newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				Logger.error("Could not register command: " + cmd, e);
			}
			ecmds += cmd + ", ";
		}
		ecmds.substring(0, ecmds.length() - 3);
		Logger.info("Enabled commands: " + ecmds);
		List<String> disabledCmds = new ArrayList<>(cmds.keySet());
		disabledCmds.removeAll(enabledCmds);
		for (String cmd : disabledCmds) {
			this.getCommand(cmd).setExecutor(new DummyCommand());
		}
		if (enabledCmds.contains("kills")) {
			this.getServer().getPluginManager().registerEvents(new EntityDeath(), this);
		}
		if (enabledCmds.contains("reward")) {
			RewardInventory.init();
			this.getServer().getPluginManager().registerEvents(new InventoryClicked(), this);
		}
		data.registerAutosave();
		Price.init(); // last
	}

	// Fired when plugin is disabled
	@Override
	public void onDisable()
	{
		data.unregisterAutosave();
		data.save();
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
				Logger.info("[BG] Vault permissions hook not found. Permissions support unavailable.");
			}
		} else {
			Logger.info("[BG] Vault dependency not found, defaulting to Bukkit Permissions.");
		}
    }

	private void createFiles() {
        
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                Logger.info("config.yml not found, creating!");
                try {
                	saveDefaultConfig();
                } catch (IllegalArgumentException e) {
                	// If no default config write empty config
                	saveConfig();
                }
            } else {
                Logger.info("config.yml found, loading!");
            }
            file = new File(getDataFolder(), "data.yml");
            if (!file.exists()) {
                Logger.info("data.yml not found, creating!");
                try {
					file.createNewFile();
				} catch (IOException e1) {
					Logger.error("Can not create new data.yml", e1);
				}
                try {
                	InputStream is = getResource("data.yml");
                	if (is == null) throw new IllegalArgumentException("No data.yml");
                	FileOutputStream fos = new FileOutputStream(file);
                	int offset = 0, read;
                	byte buffer[] = new byte[255];
                	while ((read = is.read(buffer, offset, 255)) != -1) {
                		fos.write(buffer, offset, read);
                		offset += read;
                	}
                	fos.close();
                	is.close();
                } catch (IllegalArgumentException e) {
                	Logger.info("No default found, create new.");
                } catch (IOException e) {
					Logger.error("Failed to create data.yml from default", e);
				}
            } else {
                Logger.info("data.yml found, loading!");
            }
            try {
				data = new Data(file);
			} catch (IOException | InvalidConfigurationException e) {
				Logger.error("Could not create new Data handler", e);
			}
        
    }
}