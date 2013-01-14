package com.nuclearw.rapsheet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;

public class ChargeCommandExecutor extends BaseCommandExecutor {
	public ChargeCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 4) {
			printArgsError(args);
			printHelp(sender, label);
			return true;
		}

		return true;
	}
}
