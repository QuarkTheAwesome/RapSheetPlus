package com.nuclearw.rapsheet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;

public class ConvictCommandExecutor extends BaseCommandExecutor {
	public ConvictCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 3) {
			printArgsError(args);
			printHelp(sender, label);
			return true;
		}

		return true;
	}
}
