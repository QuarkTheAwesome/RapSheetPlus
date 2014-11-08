package com.nuclearw.rapsheet.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;

public class BrandCommandExecutor extends RapsheetCommand implements CommandExecutor {
	public BrandCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length <= 1) {
			printArgsError(args);
			sender.sendMessage(ChatColor.GOLD + "/" + label + " brand <player> <brand...>: " + ChatColor.WHITE + "Brand a player for their crimes. Leave blank to remove brand.");
			return true;
		}
        if (Bukkit.getPlayer(args[1]) != null)
        {
        	if(args.length == 2) {
        		this.plugin.getConfig().set(args[1], null);
        		this.plugin.saveConfig();
        		sender.sendMessage(ChatColor.GOLD + "Brand for player " + ChatColor.AQUA + args[1] + ChatColor.GOLD + " removed.");
        		sender.getServer().getPlayer(args[1]).sendMessage(ChatColor.GOLD + "Your brand has been removed!");
        		return true;
        	}
        	int x = 2;
        	StringBuilder builder = new StringBuilder();
        	do
        	{
        		builder.append(args[x]);
        		if(x < args.length - 1) {
        			builder.append(" ");
        		}
        		x++;
        	}
        	while (
        			x < args.length);
        	String s = builder.toString();
        	this.plugin.getConfig().set(args[1], s);
        	this.plugin.saveConfig();
        	sender.sendMessage(ChatColor.GOLD + "Brand for " + ChatColor.AQUA + args[1] + ChatColor.GOLD + " set to " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', s));
        	sender.getServer().getPlayer(args[1]).sendMessage(ChatColor.RED + "You have been branded as " + ChatColor.translateAlternateColorCodes('&', s));
        	return true;
        } else {
        	sender.sendMessage(ChatColor.RED + "The player " + args[1] + " could not be found");
            return true;     	
        }
	}
}
