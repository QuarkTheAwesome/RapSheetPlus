package com.nuclearw.rapsheet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;

public class BaseCommandExecutor implements CommandExecutor {
	private Rapsheet plugin;

	private final static String NO_PERMISSION = ChatColor.RED + "You do not have permission to use that command!";
	protected final static String COULD_NOT_FIND_PLAYER = ChatColor.RED + "Could not find that player!";
	protected final static String COULD_NOT_FIND_CHARGE = ChatColor.RED + "Could not find that charge for <PLAYER>!";

	private CommandExecutor lookupCommand;
	private CommandExecutor chargeCommand;
	private CommandExecutor convictCommand;
	private CommandExecutor pardonCommand;
	private CommandExecutor sealCommand;
	private CommandExecutor unsealCommand;
	private CommandExecutor expungeCommand;

	public BaseCommandExecutor(Rapsheet plugin) {
		this.plugin = plugin;

		this.lookupCommand  = new LookupCommandExecutor(plugin);
		this.chargeCommand  = new ChargeCommandExecutor(plugin);
		this.convictCommand = new ConvictCommandExecutor(plugin);
		this.pardonCommand  = new PardonCommandExecutor(plugin);
		this.sealCommand    = new SealCommandExecutor(plugin);
		this.unsealCommand  = new UnsealCommandExecutor(plugin);
		this.expungeCommand = new ExpungeCommandExecutor(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			printHelp(sender, label);
		}

		if(args[0].equalsIgnoreCase("lookup")) {
			if(!sender.hasPermission("rapsheet.lookup")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			lookupCommand.onCommand(sender, cmd, label, args);
		}

		if(args[0].equalsIgnoreCase("charge")) {
			if(!sender.hasPermission("rapsheet.charge")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			chargeCommand.onCommand(sender, cmd, label, args);
		}

		if(args[0].equalsIgnoreCase("convict")) {
			if(!sender.hasPermission("rapsheet.convict")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			convictCommand.onCommand(sender, cmd, label, args);
		}

		if(args[0].equalsIgnoreCase("pardon")) {
			if(!sender.hasPermission("rapsheet.pardon")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			pardonCommand.onCommand(sender, cmd, label, args);
		}

		if(args[0].equalsIgnoreCase("seal")) {
			if(!sender.hasPermission("rapsheet.seal")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			sealCommand.onCommand(sender, cmd, label, args);
		}

		if(args[0].equalsIgnoreCase("unseal")) {
			if(!sender.hasPermission("rapsheet.seal")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			unsealCommand.onCommand(sender, cmd, label, args);
		}

		if(args[0].equalsIgnoreCase("expunge")) {
			if(!sender.hasPermission("rapsheet.expunge")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			expungeCommand.onCommand(sender, cmd, label, args);
		}

		return true;
	}

	protected static void printHelp(CommandSender sender, String label) {
		if(sender.hasPermission("rapsheet.lookup")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " lookup <player>");
			sender.sendMessage(ChatColor.GRAY + "/" + label + " lookup <player> <charge#>");
		}
		if(sender.hasPermission("rapsheet.charge")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " charge <player> <reason> <description ...>");
		}
		if(sender.hasPermission("rapsheet.convict")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " convict <player> <charge#>");
		}
		if(sender.hasPermission("rapsheet.pardon")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " pardon <player> <charge#>");
		}
		if(sender.hasPermission("rapsheet.seal")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " seal <player> <charge#>");
			sender.sendMessage(ChatColor.GRAY + "/" + label + " unseal <player> <charge#>");
		}
		if(sender.hasPermission("rapsheet.expunge")) {
			sender.sendMessage(ChatColor.GRAY + "/" + label + " expunge <player> <charge#>");
		}
	}
}
