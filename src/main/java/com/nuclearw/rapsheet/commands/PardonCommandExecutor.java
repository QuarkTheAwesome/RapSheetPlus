package com.nuclearw.rapsheet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;
import com.nuclearw.rapsheet.RecordState;

public class PardonCommandExecutor extends RapsheetCommand implements CommandExecutor {
	public PardonCommandExecutor(Rapsheet plugin) {
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

		if(found.isSealed() && !sender.hasPermission("rapsheet.viewsealed")) {
			sender.sendMessage(CANNOT_MODIFY_SEALED);
			return true;
		}

		if(found.getState() == RecordState.PARDONED) {
			sender.sendMessage(ChatColor.RED + "You cannot pardon a charge that is already pardoned!");
			return true;
		}

		boolean success = Rapsheet.getManager().pardonPlayer(target, chargeId);

		if(!success) {
			plugin.getLogger().severe("Error trying to pardon player " + target + " of chargeId: " + chargeId);
		}

		sender.sendMessage(ChatColor.LIGHT_PURPLE + "Pardoned" + ChatColor.RESET + ": " + ChatColor.AQUA + found.getOffender());
		sender.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());
		sender.sendMessage(ChatColor.GOLD + "Report" + ChatColor.RESET + ": " + ChatColor.GRAY + found.getChargeLong());

		// Notify player if they are online
		Player player = plugin.getServer().getPlayer(target);

		if(player == null) {
			return true;
		}

		player.sendMessage(ChatColor.LIGHT_PURPLE + "Pardoned" + ChatColor.GOLD + " by " + ChatColor.AQUA + sender.getName() + ChatColor.GOLD + " of " + ChatColor.GRAY + found.getChargeShort());
		player.sendMessage(ChatColor.GOLD + "Filed under Charge " + ChatColor.RESET + "#" + found.getChargeId());

		return true;
	}
}
