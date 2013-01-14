package com.nuclearw.rapsheet.commands;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;

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

		SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy h:mm");

		String target = findTarget(args[1]);

		if(args.length == 2) {
			List<Record> found = plugin.getDatabase().find(Record.class).where().ieq("offender", target).findList();

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
				printHelp(sender, label);
				return true;
			}

			Record found = plugin.getDatabase().find(Record.class).where().ieq("offender", target).eq("charge_id", chargeId).findUnique();

			if(found == null) {
				sender.sendMessage(COULD_NOT_FIND_CHARGE.replace("<PLAYER>", target));
				return true;
			}

			// TODO: The part where we show them the record.
		}

		return true;
	}
}
