package com.nuclearw.rapsheet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nuclearw.rapsheet.Rapsheet;

public class ChargeCommandExecutor extends RapsheetCommand implements CommandExecutor {
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

		StringBuilder sb = new StringBuilder();

		for(int i = 3; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}

		String chargeDescription = sb.toString().trim();

		int newChargeId = Rapsheet.getManager().chargePlayer(offender.getName(), sender.getName(), args[2], chargeDescription);

		if(newChargeId < 0) {
			plugin.getLogger().severe("There was an error trying to charge player " + offender.getName() + ": Got chargeId " + newChargeId);
		}

		sender.sendMessage(ChatColor.GOLD + "Charged" + ChatColor.RESET + ": " + ChatColor.AQUA + offender.getName());
		sender.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + newChargeId + ChatColor.GOLD + " - " + ChatColor.AQUA + args[2]);
		sender.sendMessage(ChatColor.GOLD + "Report" + ChatColor.RESET + ": " + ChatColor.GRAY + chargeDescription);

		offender.sendMessage(ChatColor.GOLD + "Charged by " + ChatColor.AQUA + sender.getName() + ChatColor.GOLD + " of " + ChatColor.GRAY + args[2]);
		offender.sendMessage(ChatColor.GOLD + "Filed under Charge " + ChatColor.RESET + "#" + newChargeId);

		return true;
	}
}
