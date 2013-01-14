package com.nuclearw.rapsheet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;

public class LookupCommandExecutor extends BaseCommandExecutor {
	public LookupCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 2) {
			printArgsError(args);
			printHelp(sender, label);
			return true;
		}

		return true;
	}
}
