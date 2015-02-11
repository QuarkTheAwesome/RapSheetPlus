package com.nuclearw.rapsheet.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.UUIDFetcher;
import com.nuclearw.rapsheet.api.NotifyChanges;

public class ChargeCommandExecutor extends RapsheetCommand implements CommandExecutor {
	public ChargeCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 4) {
			printArgsError(args);
			sender.sendMessage(ChatColor.GOLD + "/" + label + " charge <player> <reason> <description ...>: " + ChatColor.WHITE + "Charge a player of a crime. Reason is a one-word crime, description goes more into it.");
			return true;
		}

		UUID target = findTarget(UUIDFetcher.getUUIDOf(args[1]));

		StringBuilder sb = new StringBuilder();

		for(int i = 3; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}

		String chargeDescription = sb.toString().trim();

		int newChargeId = Rapsheet.getManager().chargePlayer(target, ((Player) sender).getUniqueId(), args[2], chargeDescription, NotifyChanges.BOTH);

		if(newChargeId < 0) {
			plugin.getLogger().severe("There was an error trying to charge player " + target + ": Got chargeId " + newChargeId);
		}

		return true;
	}
}
