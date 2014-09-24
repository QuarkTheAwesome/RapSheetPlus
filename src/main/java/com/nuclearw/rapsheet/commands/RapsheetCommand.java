package com.nuclearw.rapsheet.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;

public class RapsheetCommand {
	protected Rapsheet plugin;

	protected final String COULD_NOT_FIND_PLAYER = ChatColor.RED + "Could not find that player!";
	protected final String COULD_NOT_FIND_CHARGE = ChatColor.RED + "Could not find that charge for <PLAYER>!";
	protected final String CANNOT_MODIFY_SEALED  = ChatColor.RED + "You cannot modify a sealed record you cannot see!";

	public RapsheetCommand(Rapsheet plugin) {
		this.plugin = plugin;
	}

	protected String findTarget(String target) {
		OfflinePlayer search = plugin.getServer().getPlayer(target);

		if(search == null) {
			plugin.getServer().getOfflinePlayer(target);
		}

		if(search != null) {
			target = search.getName();
		}

		return target;
	}

	protected void printArgsError(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("Invalid arguments passed to ");
		sb.append(this.getClass().getName());
		sb.append(": ");
		for(String arg : args) {
			sb.append(arg);
			sb.append(" ");
		}
		plugin.getLogger().severe(sb.toString());
	}

	protected void printHelp(CommandSender sender, String label, String page) {
		if (page.equalsIgnoreCase("1")) {
			sender.sendMessage(ChatColor.GOLD + "----- " + ChatColor.AQUA + "RapSheetPlus" + ChatColor.GOLD + " -----");
			if(sender.hasPermission("rapsheet.lookup")) {
				sender.sendMessage(ChatColor.GOLD + "/" + label + " lookup <player>: " + ChatColor.WHITE + "Look up a player's records.");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " lookup <player> <charge#>: " + ChatColor.WHITE + "Look at a specific charge on a player.");
			}
			if(sender.hasPermission("rapsheet.charge")) {
				sender.sendMessage(ChatColor.GOLD + "/" + label + " charge <player> <reason> <description ...>: " + ChatColor.WHITE + "Charge a player of a crime. Reason is a one-word crime, description goes more into it.");
			}

			if(sender.hasPermission("rapsheet.lookupme")) {
				sender.sendMessage(ChatColor.GOLD + "/" + label + " me: " + ChatColor.WHITE + "See your own RAP sheet.");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " me <charge#>: " + ChatColor.WHITE + "See a specific charge on your RAP sheet.");
			}
			sender.sendMessage(ChatColor.GOLD + "Type " + ChatColor.RED + "/" + label + " 2" + ChatColor.GOLD + " to read the next page.");
		} else if (page.equalsIgnoreCase("2")) {
			sender.sendMessage(ChatColor.GOLD + "----- " + ChatColor.AQUA + "RapSheetPlus" + ChatColor.GOLD + " -----");
			if(sender.hasPermission("rapsheet.seal")) {
				sender.sendMessage(ChatColor.GOLD + "/" + label + " seal <player> <charge#>: " + ChatColor.WHITE + "Seal a charge.");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " unseal <player> <charge#>: " + ChatColor.WHITE + "Unseal a charge.");
			}
			if(sender.hasPermission("rapsheet.expunge")) {
				sender.sendMessage(ChatColor.GOLD + "/" + label + " expunge <player> <charge#>: " + ChatColor.WHITE + "Expunge a charge against a player. " + ChatColor.RED + "This cannot be undone.");
			}
			if(sender.hasPermission("rapsheet.charge")) {
				sender.sendMessage(ChatColor.GOLD + "/" + label + " brand <player> <brand...>: " + ChatColor.WHITE + "Brand a player. Leave blank to clear.");
			}
			if(sender.hasPermission("rapsheet.pardon")) {
				sender.sendMessage(ChatColor.GOLD + "/" + label + " pardon <player> <charge#>: " + ChatColor.WHITE + "Pardon a charge.");
			}
			if(sender.hasPermission("rapsheet.convict")) {
				sender.sendMessage(ChatColor.GOLD + "/" + label + " convict <player> <charge#>: " + ChatColor.WHITE + "Convict (confirm) a charge.");
			}
			sender.sendMessage(ChatColor.GOLD + "/" + label + " version: " + ChatColor.WHITE + "Display version info.");
		} else {
			sender.sendMessage("Error printing help. Please try /rap 1.");
		}

	} 
}
