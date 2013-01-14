package com.nuclearw.rapsheet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;

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

		Record found = plugin.getDatabase().find(Record.class).where().ieq("offender", target).eq("charge_id", chargeId).findUnique();

		if(found == null) {
			sender.sendMessage(COULD_NOT_FIND_CHARGE.replace("<PLAYER>", target));
			return true;
		}

		if(!found.isSealed()) {
			sender.sendMessage(ChatColor.RED + "You cannot unseal a charge that isn't sealed!");
			return true;
		}

		found.setSealed(false);

		plugin.getDatabase().update(found);

		sender.sendMessage(ChatColor.GRAY + "Unsealed " + ChatColor.GOLD + "charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());

		// Notify player if they are online
		Player player = plugin.getServer().getPlayer(target);

		if(player == null) {
			return true;
		}

		player.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " has been " + ChatColor.GRAY + "unsealed" + ChatColor.GOLD + " by " + ChatColor.AQUA + sender.getName());

		return true;
	}
}
