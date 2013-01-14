package com.nuclearw.rapsheet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;

public class UnsealCommandExecutor extends BaseCommandExecutor {
	public UnsealCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 3) {
			printArgsError(args);
			printHelp(sender, label);
			return true;
		}

		String target = findTarget(args[1]);

		int chargeId = -1;

		try {
			chargeId = Integer.valueOf(args[2]);
		} catch (NumberFormatException ex) {
			printHelp(sender, label);
			return true;
		}

		Record found = plugin.getDatabase().find(Record.class).where().ieq("offender", target).eq("charge_id", chargeId).findUnique();

		if(found == null) {
			sender.sendMessage(COULD_NOT_FIND_CHARGE.replace("<PLAYER>", target));
			return true;
		}

		return true;
	}
}
