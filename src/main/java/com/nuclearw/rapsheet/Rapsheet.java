package com.nuclearw.rapsheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.nuclearw.rapsheet.commands.BaseCommandExecutor;

public class Rapsheet extends JavaPlugin {
	@Override
	public void onEnable() {
		initDatabase();

		getCommand("rapsheet").setExecutor(new BaseCommandExecutor(this));

		metrics();

		getLogger().info("Finished loading " + getDescription().getFullName());
	}

	@Override
	public void onDisable() {
		getLogger().info("Finished unloading " + getDescription().getFullName());
	}

	private void initDatabase() {
		try {
			getDatabase().find(Record.class).findRowCount();
		} catch (PersistenceException ex) {
			getLogger().info("Initializing database");
			this.installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(Record.class);
		return list;
	}

	private void metrics() {
		try {
			Metrics metrics = new Metrics(this);
//			metrics.start();
		} catch (IOException e) { }
	}
}
