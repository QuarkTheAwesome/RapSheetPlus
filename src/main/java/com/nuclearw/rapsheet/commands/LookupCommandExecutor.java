package com.nuclearw.rapsheet.commands;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;

public class LookupCommandExecutor extends RapsheetCommand implements CommandExecutor {
	public LookupCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 2) {
			printArgsError(args);
			sender.sendMessage(ChatColor.GOLD + "/" + label + " lookup <player>: " + ChatColor.WHITE + "Look up a player's records.");
			sender.sendMessage(ChatColor.GOLD + "/" + label + " lookup <player> <charge#>: " + ChatColor.WHITE + "Look at a specific charge on a player.");
			return true;
		}

		SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy h:mm");

		String target = findTarget(args[1]);

		if(args.length == 2) {
			List<Record> found = Rapsheet.getManager().getCharges(target);

			if(found == null || found.isEmpty()) {
				sender.sendMessage(COULD_NOT_FIND_PLAYER);
				return true;
			}

			sender.sendMessage(ChatColor.GOLD + "----- " + ChatColor.AQUA + target + ChatColor.GOLD + " -----");

			Iterator<Record> iterator = found.iterator();
			while(iterator.hasNext()) {
				Record record = iterator.next();

				String message = "#" + record.getChargeId() + " ";

				if(record.isSealed() && !sender.hasPermission("rapsheet.viewsealed")) {
					message += ChatColor.GRAY + "-Sealed-";
				} else {
					message += format.format(record.getTime()) + " ";

					message += ChatColor.GRAY + "[";

					switch(record.getState()) {
						case CHARGED:
							message += ChatColor.GOLD + "CHARGED";
							break;
						case CONVICTED:
							message += ChatColor.DARK_RED + "CONVICTED";
							break;
						case PARDONED:
							message += ChatColor.LIGHT_PURPLE + "PARDONED";
							break;
						default:
							message += ChatColor.RED + "ERROR";
							plugin.getLogger().severe("Invalid RecordState in LookupCommand!");
							break;
					}

					message += ChatColor.GRAY + "] ";

					if(record.isSealed() && sender.hasPermission("rapsheet.viewsealed")) {
						message += "[SEALED] ";
					}

					message += ChatColor.GOLD + record.getChargeShort();
				}

				sender.sendMessage(message);
			}
		}

		if(args.length == 3) {
			int chargeId = -1;

			try {
				chargeId = Integer.valueOf(args[2]);
			} catch (NumberFormatException ex) {
				sender.sendMessage(ChatColor.GOLD + "/" + label + " lookup <player>: " + ChatColor.WHITE + "Look up a player's records.");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " lookup <player> <charge#>: " + ChatColor.WHITE + "Look at a specific charge on a player.");
				return true;
			}

			Record found = Rapsheet.getManager().getCharge(target, chargeId);

			if(found == null) {
				sender.sendMessage(COULD_NOT_FIND_CHARGE.replace("<PLAYER>", target));
				return true;
			}

			if(found.isSealed() && !sender.hasPermission("rapsheet.viewsealed")) {
				sender.sendMessage(ChatColor.RED + "You cannot view a sealed charge!");
				return true;
			}

			sender.sendMessage(ChatColor.GOLD + "----- " + ChatColor.AQUA + target + ChatColor.GOLD + " -----");
			sender.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + chargeId + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());
			sender.sendMessage(ChatColor.GOLD + "Filed" + ChatColor.RESET + ": " + ChatColor.AQUA + format.format(found.getTime()) + ChatColor.GOLD + " Official: " + ChatColor.AQUA + found.getOfficial());
			sender.sendMessage(ChatColor.GOLD + "Report" + ChatColor.RESET + ": " + ChatColor.GRAY + found.getChargeLong());

			if(found.isSealed() && sender.hasPermission("rapsheet.viewsealed")) {
				sender.sendMessage(ChatColor.GOLD + "This charge is: " + ChatColor.GRAY + "sealed");
			}
		}

		return true;
	}
}
