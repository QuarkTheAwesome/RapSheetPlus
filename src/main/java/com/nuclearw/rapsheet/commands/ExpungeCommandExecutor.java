package com.nuclearw.rapsheet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;

public class ExpungeCommandExecutor implements CommandExecutor {
	private Rapsheet plugin;

	public ExpungeCommandExecutor(Rapsheet plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return true;
	}
}
