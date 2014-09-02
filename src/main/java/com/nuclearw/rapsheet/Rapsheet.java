package com.nuclearw.rapsheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.nuclearw.rapsheet.api.RapsheetManager;
import com.nuclearw.rapsheet.commands.BaseCommandExecutor;
import com.nuclearw.rapsheet.locale.LocaleManager;

public class Rapsheet extends JavaPlugin {
	private static RapsheetManager manager;
	private static LocaleManager locale;
	@Override
	public void onEnable() {
		initDatabase();

		try {
			locale = new LocaleManager(this);
		} catch (IOException e) {
			// We could not load locale, this is an error we cannot get around.
			getLogger().severe("Could not load Locale!  This is a non-recoverable error!");
			e.printStackTrace();
			getPluginLoader().disablePlugin(this);
			return;
		}
		manager = new SimpleRapsheetManager(this);
		getCommand("rapsheet").setExecutor(new BaseCommandExecutor(this));
		metrics();
		getLogger().info("Finished loading " + getDescription().getFullName());
	}

	@Override
	public void onDisable() {
		getLogger().info("Finished unloading " + getDescription().getFullName());
		getLogger().info(ChatColor.RED + "WARNING! If this is a reload, " + getDescription().getFullName() + " will NOT continue running! You will have to restart the server.");
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(Record.class);
		return list;
	}

	public static RapsheetManager getManager() {
		return manager;
	}

	public LocaleManager getLocale() {
		return locale;
	}

	private void initDatabase() {
		try {
			getDatabase().find(Record.class).findRowCount();
		} catch (PersistenceException ex) {
			getLogger().info("Initializing database");
			this.installDDL();
		}
	}

	private void metrics() {
		try {
			Metrics metrics = new Metrics(this);
//			metrics.start();
		} catch (IOException e) { }
	}
}
