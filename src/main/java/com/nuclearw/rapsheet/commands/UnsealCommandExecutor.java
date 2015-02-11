package com.nuclearw.rapsheet.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;
import com.nuclearw.rapsheet.UUIDFetcher;
import com.nuclearw.rapsheet.api.NotifyChanges;

public class UnsealCommandExecutor extends RapsheetCommand implements CommandExecutor {
	public UnsealCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 3) {
			printArgsError(args);
			sender.sendMessage(ChatColor.GOLD + "/" + label + " seal <player> <charge#>: " + ChatColor.WHITE + "Seal a charge. Only those with rapsheet.seal can view them.");
			sender.sendMessage(ChatColor.GOLD + "/" + label + " unseal <player> <charge#>: " + ChatColor.WHITE + "Unseal a charge.");
			return true;
		}

		UUID target = findTarget(UUIDFetcher.getUUIDOf(args[1]));

		int chargeId = -1;

		try {
			chargeId = Integer.valueOf(args[2]);
		} catch (NumberFormatException ex) {
			sender.sendMessage(ChatColor.GOLD + "/" + label + " seal <player> <charge#>: " + ChatColor.WHITE + "Seal a charge. Only those with rapsheet.seal can view them.");
			sender.sendMessage(ChatColor.GOLD + "/" + label + " unseal <player> <charge#>: " + ChatColor.WHITE + "Unseal a charge.");
			return true;
		}

		Record found = Rapsheet.getManager().getCharge(target, chargeId);

		if(found == null) {
			sender.sendMessage(COULD_NOT_FIND_CHARGE.replace("<PLAYER>", org.bukkit.Bukkit.getPlayer(target).getName()));
			return true;
		}

		if(!found.isSealed()) {
			sender.sendMessage(ChatColor.RED + "You cannot unseal a charge that isn't sealed!");
			return true;
		}

		boolean success = Rapsheet.getManager().unsealPlayerCharge(target, ((Player) sender).getUniqueId(), chargeId, NotifyChanges.BOTH);

		if(!success) {
			plugin.getLogger().severe("Error trying to unseal player " + target + "'s chargeId: " + chargeId);
		}

		return true;
	}
}
