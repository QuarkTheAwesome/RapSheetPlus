package com.nuclearw.rapsheet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nuclearw.rapsheet.Rapsheet;

public class BaseCommandExecutor extends RapsheetCommand implements CommandExecutor {
	private final String NO_PERMISSION = ChatColor.RED + "You do not have permission to use that command!";

	private CommandExecutor lookupCommand  = new LookupCommandExecutor(plugin);
	private CommandExecutor chargeCommand  = new ChargeCommandExecutor(plugin);
	private CommandExecutor convictCommand = new ConvictCommandExecutor(plugin);
	private CommandExecutor pardonCommand  = new PardonCommandExecutor(plugin);
	private CommandExecutor sealCommand    = new SealCommandExecutor(plugin);
	private CommandExecutor unsealCommand  = new UnsealCommandExecutor(plugin);
	private CommandExecutor expungeCommand = new ExpungeCommandExecutor(plugin);
	private CommandExecutor meCommand = new MeCommandExecutor(plugin);
	private CommandExecutor brandCommand = new BrandCommandExecutor(plugin);

	public BaseCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			printHelp(sender, label, "1");
			return true;
		}
			if(args[0].equalsIgnoreCase("lookup")) {
			if(!sender.hasPermission("rapsheet.lookup")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			lookupCommand.onCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("charge")) {
			if(!sender.hasPermission("rapsheet.charge")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			chargeCommand.onCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("convict")) {
			if(!sender.hasPermission("rapsheet.convict")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			convictCommand.onCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("2")) {
			printHelp(sender, label, "2");
		} else if(args[0].equalsIgnoreCase("help")) {
			try {
				if(args[1].equalsIgnoreCase("2")) {
					printHelp(sender, label, "2");
					return true;
				}
			} catch (Exception ex) {}
			printHelp(sender, label, "1");
			return true;
		} else if(args[0].equalsIgnoreCase("version")) {
			sender.sendMessage(ChatColor.GOLD + "----- " + ChatColor.AQUA + this.plugin.getDescription().getFullName() + ChatColor.GOLD + " -----");
			sender.sendMessage(ChatColor.GOLD + "Original plugin by " + ChatColor.AQUA + "NuclearW" + ChatColor.GOLD + ". Original available here:");
			sender.sendMessage(ChatColor.GRAY + "https://github.com/NuclearW/Rapsheet");
			sender.sendMessage(ChatColor.GOLD + "'Plus' version by " + ChatColor.AQUA + "Quark" + ChatColor.GOLD + ". Available here:");
			sender.sendMessage(ChatColor.GRAY + "https://github.com/QuarkTheAwesome/RapSheetPlus");
			sender.sendMessage(ChatColor.GOLD + "Find it on BukkitDev!");
			sender.sendMessage(ChatColor.GRAY + "http://dev.bukkit.org/bukkit-plugins/rapsheetplus/");
		} else if(args[0].equalsIgnoreCase("pardon")) {
			if(!sender.hasPermission("rapsheet.pardon")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			pardonCommand.onCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("seal")) {
			if(!sender.hasPermission("rapsheet.seal")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			sealCommand.onCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("me")) {
			if(!sender.hasPermission("rapsheet.lookupme")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			meCommand.onCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("unseal")) {
			if(!sender.hasPermission("rapsheet.seal")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			unsealCommand.onCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("expunge")) {
			if(!sender.hasPermission("rapsheet.expunge")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			expungeCommand.onCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("brand")) {
			if(!sender.hasPermission("rapsheet.brand")) {
				sender.sendMessage(NO_PERMISSION);
				return true;
			}
			brandCommand.onCommand(sender, cmd, label, args);
		} else {
			printHelp(sender, label, "1");
		}
		return true;
	}
}
