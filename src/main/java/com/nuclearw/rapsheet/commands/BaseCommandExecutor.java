package com.nuclearw.rapsheet.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;

public class BaseCommandExecutor implements CommandExecutor {
	protected Rapsheet plugin;

	private final String NO_PERMISSION = ChatColor.RED + "You do not have permission to use that command!";
	protected final String COULD_NOT_FIND_PLAYER = ChatColor.RED + "Could not find that player!";
	protected final String COULD_NOT_FIND_CHARGE = ChatColor.RED + "Could not find that charge for <PLAYER>!";
	protected final String CANNOT_MODIFY_SEALED  = ChatColor.RED + "You cannot modify a sealed record you cannot see!";

	private CommandExecutor lookupCommand  = new LookupCommandExecutor(plugin);
	private CommandExecutor chargeCommand  = new ChargeCommandExecutor(plugin);
	private CommandExecutor convictCommand = new ConvictCommandExecutor(plugin);
	private CommandExecutor pardonCommand  = new PardonCommandExecutor(plugin);
	private CommandExecutor sealCommand    = new SealCommandExecutor(plugin);
	private CommandExecutor unsealCommand  = new UnsealCommandExecutor(plugin);
	private CommandExecutor expungeCommand = new ExpungeCommandExecutor(plugin);

	public BaseCommandExecutor(Rapsheet plugin) {
		this.plugin = plugin;
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

	protected String findTarget(String target) {
		OfflinePlayer search = plugin.getServer().getPlayer(target);

		if(search == null) {
			plugin.getServer().getOfflinePlayer(target);
		}

		if(search != null) {
			target = search.getName();
		}

		return target;
	}

	protected void printArgsError(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("Invalid arguments passed to ");
		sb.append(this.getClass().getName());
		sb.append(": ");
		for(String arg : args) {
			sb.append(arg);
			sb.append(" ");
		}
		plugin.getLogger().severe(sb.toString());
	}

	protected void printHelp(CommandSender sender, String label) {
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
