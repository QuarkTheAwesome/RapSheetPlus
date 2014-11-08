package com.nuclearw.rapsheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nuclearw.rapsheet.api.RapsheetManager;
import com.nuclearw.rapsheet.commands.BaseCommandExecutor;
import com.nuclearw.rapsheet.locale.LocaleManager;

public class Rapsheet extends JavaPlugin implements Listener {
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
		if(Rapsheet.getInstance().getConfig().getString("autoupdate") == null) {
			getLogger().info("Could not find autoupdate in config, setting it to true...");
			Rapsheet.getInstance().getConfig().set("autoupdate", "true");
			Rapsheet.getInstance().saveConfig();
		}
		if(Rapsheet.getInstance().getConfig().getString("autoupdate") == "true") {
			getLogger().info("Enabling AutoUpdate...");
			try {
				AutoUpdate update = new AutoUpdate(Rapsheet.getInstance(), Rapsheet.getInstance().getConfig());
				update.enabled = true;
			} catch (Exception e) {
				getLogger().info("Couldn't enable AutoUpdate! Stack trace:");
				e.printStackTrace();
			}
		}
		manager = new SimpleRapsheetManager(this);
		getCommand("rapsheet").setExecutor(new BaseCommandExecutor(this));
    	PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(this, this);
		getLogger().info("Finished loading " + getDescription().getFullName());
	}

	@Override
	public void onDisable() {
		getLogger().info("Finished unloading " + getDescription().getFullName());
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
	  @EventHandler
	  public void onChat(AsyncPlayerChatEvent e)
	  {
	    Player player = e.getPlayer();
	    if (Rapsheet.getInstance().getConfig().getString(player.getName()) != null) {
	    		      String s = Rapsheet.getInstance().getConfig().getString(player.getName());
	      e.setFormat(ChatColor.translateAlternateColorCodes('&', s) + ChatColor.RESET + e.getFormat());
	    }
	  }
	  public static Rapsheet getInstance() {
		  Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("RapSheetPlus");
		  if (plugin == null || !(plugin instanceof Rapsheet)) {
			  throw new RuntimeException("'RapSheetPlus' not found. 'RapSheetPlus' plugin disabled?");
		  }
		  return (Rapsheet) plugin;
	  }
}
