package com.nuclearw.rapsheet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;
import com.nuclearw.rapsheet.RecordState;
import com.nuclearw.rapsheet.api.NotifyChanges;

public class PardonCommandExecutor extends RapsheetCommand implements CommandExecutor {
	public PardonCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 3) {
			printArgsError(args);
			sender.sendMessage(ChatColor.GOLD + "/" + label + " pardon <player> <charge#>: " + ChatColor.WHITE + "Pardon a charge or conviction.");
			return true;
		}

		String target = findTarget(args[1]);

		int chargeId = -1;

		try {
			chargeId = Integer.valueOf(args[2]);
		} catch (NumberFormatException ex) {
			sender.sendMessage(ChatColor.GOLD + "/" + label + " pardon <player> <charge#>: " + ChatColor.WHITE + "Pardon a charge or conviction.");
			return true;
		}

		Record found = Rapsheet.getManager().getCharge(target, chargeId);

		if(found == null) {
			sender.sendMessage(COULD_NOT_FIND_CHARGE.replace("<PLAYER>", target));
			return true;
		}

		if(found.isSealed() && !sender.hasPermission("rapsheet.viewsealed")) {
			sender.sendMessage(CANNOT_MODIFY_SEALED);
			return true;
		}

		if(found.getState() == RecordState.PARDONED) {
			sender.sendMessage(ChatColor.RED + "You cannot pardon a charge that is already pardoned!");
			return true;
		}

		boolean success = Rapsheet.getManager().pardonPlayer(target, sender.getName(), chargeId, NotifyChanges.BOTH);

		if(!success) {
			plugin.getLogger().severe("Error trying to pardon player " + target + " of chargeId: " + chargeId);
		}

		return true;
	}
}
