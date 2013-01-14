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

	protected void printHelp(CommandSender sender, String label) {
		if(sender.hasPermission("rapsheet.lookup")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " lookup <player>");
			sender.sendMessage(ChatColor.GRAY + "/" + label + " lookup <player> <charge#>");
		}
		if(sender.hasPermission("rapsheet.charge")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " charge <player> <reason> <description ...>");
		}
		if(sender.hasPermission("rapsheet.convict")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " convict <player> <charge#>");
		}
		if(sender.hasPermission("rapsheet.pardon")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " pardon <player> <charge#>");
		}
		if(sender.hasPermission("rapsheet.seal")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " seal <player> <charge#>");
			sender.sendMessage(ChatColor.GRAY + "/" + label + " unseal <player> <charge#>");
		}
		if(sender.hasPermission("rapsheet.expunge")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " expunge <player> <charge#>");
		}
	}
}
