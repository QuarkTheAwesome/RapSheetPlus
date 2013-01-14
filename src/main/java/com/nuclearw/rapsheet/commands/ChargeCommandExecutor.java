package com.nuclearw.rapsheet.commands;

import java.util.List;

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

		// TODO: Make charge description from args

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

		return true;
	}
}
