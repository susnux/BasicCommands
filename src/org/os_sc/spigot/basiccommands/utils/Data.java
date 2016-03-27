package org.os_sc.spigot.basiccommands.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.os_sc.spigot.basiccommands.Plugin;

public class Data extends YamlConfiguration {
	private boolean dirty = false;
	private int taskId = -1;
	private File file;

	public Data(File file) throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		this.load(file);
		this.file = file;
	}

	public void registerAutosave()
	{
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.instance, new Writer(this),6000, 6000); // 5 minutes
	}

	public void unregisterAutosave()
	{
		if (taskId != -1)
			Bukkit.getScheduler().cancelTask(taskId);
	}

	public boolean save()
	{
		try {
			this.save(this.file);
		} catch (IOException e) {
			Logger.error("Could not save data", e);
			return false;
		}
		return true;
	}

	public boolean isDirty()
	{
		return this.dirty;
	}

	@Override
	public void set(String path, Object value) {
		this.dirty = true;
		super.set(path, value);
	}

	@Override
	public void save(File file) throws IOException {
		super.save(file);
		this.dirty = false;
	}

	@Override
	public void save(String file) throws IOException {
		super.save(file);
		this.dirty = false;
	}

	@Override
	public String saveToString() {
		this.dirty = false;
		return super.saveToString();
	}

	private class Writer implements Runnable {
		private Data data;

		public Writer(Data data)
		{
			this.data = data;
		}

		@Override
		public void run() {
			if (data.isDirty()) {
				data.save();
			}
		}
		
	}
}
