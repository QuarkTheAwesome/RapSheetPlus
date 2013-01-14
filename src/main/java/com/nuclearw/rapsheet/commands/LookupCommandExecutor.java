package com.nuclearw.rapsheet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;

public class LookupCommandExecutor implements CommandExecutor {
	private Rapsheet plugin;

	public LookupCommandExecutor(Rapsheet plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 2) {
			StringBuilder sb = new StringBuilder();
			sb.append("Invalid arguments passed to LookupCommand: ");
			for(String arg : args) {
				sb.append(arg);
				sb.append(" ");
			}
			plugin.getLogger().severe(sb.toString());

			BaseCommandExecutor.printHelp(sender, label);
			return true;
		}

		return true;
	}
}
