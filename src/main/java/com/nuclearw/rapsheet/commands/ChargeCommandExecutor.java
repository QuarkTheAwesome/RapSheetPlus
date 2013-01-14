package com.nuclearw.rapsheet.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;
import com.nuclearw.rapsheet.RecordState;

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

		// We won't use findTarget because we're not going to allow charging of offline players.
		Player offender = plugin.getServer().getPlayer(args[1]);

		if(offender == null) {
			sender.sendMessage(COULD_NOT_FIND_PLAYER);
			return true;
		}

		int newChargeId = 1;

		// I do not like the way this is done
		List<Record> found = plugin.getDatabase().find(Record.class).where().ieq("offender", offender.getName()).findList();

		if(found != null && !found.isEmpty()) {
			newChargeId = found.get(found.size() - 1).getChargeId() + 1;
		}

		StringBuilder sb = new StringBuilder();

		for(int i = 3; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}

		String chargeDescription = sb.toString().trim();

		Record record = new Record();
		record.setChargeId(newChargeId);
		record.setOffender(offender.getName());
		record.setOfficial(sender.getName());
		record.setChargeShort(args[2]);
		record.setChargeLong(chargeDescription);
		record.setTime(System.currentTimeMillis());
		record.setSealed(false);
		record.setState(RecordState.CHARGED);

		plugin.getDatabase().save(record);

		sender.sendMessage(ChatColor.GOLD + "Charged" + ChatColor.RESET + ": " + ChatColor.AQUA + offender.getName());
		sender.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + newChargeId + ChatColor.GOLD + " - " + ChatColor.AQUA + args[2]);
		sender.sendMessage(ChatColor.GOLD + "Report" + ChatColor.RESET + ": " + ChatColor.GRAY + chargeDescription);

		offender.sendMessage(ChatColor.GOLD + "Charged by " + ChatColor.AQUA + sender.getName() + ChatColor.GOLD + " of " + ChatColor.GRAY + args[2]);
		offender.sendMessage(ChatColor.GOLD + "Filed under Charge " + ChatColor.RESET + "#" + newChargeId);

		return true;
	}
}
