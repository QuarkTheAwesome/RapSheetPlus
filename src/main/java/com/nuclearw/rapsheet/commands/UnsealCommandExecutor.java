package com.nuclearw.rapsheet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;
import com.nuclearw.rapsheet.api.NotifyChanges;

public class UnsealCommandExecutor extends RapsheetCommand implements CommandExecutor {
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

		Record found = Rapsheet.getManager().getCharge(target, chargeId);

		if(found == null) {
			sender.sendMessage(COULD_NOT_FIND_CHARGE.replace("<PLAYER>", target));
			return true;
		}

		if(!found.isSealed()) {
			sender.sendMessage(ChatColor.RED + "You cannot unseal a charge that isn't sealed!");
			return true;
		}

		boolean success = Rapsheet.getManager().unsealPlayerCharge(target, sender.getName(), chargeId, NotifyChanges.BOTH);

		if(!success) {
			plugin.getLogger().severe("Error trying to unseal player " + target + "'s chargeId: " + chargeId);
		}

		return true;
	}
}
