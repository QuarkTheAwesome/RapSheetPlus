package com.nuclearw.rapsheet;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nuclearw.rapsheet.api.NotifyChanges;
import com.nuclearw.rapsheet.api.RapsheetManager;
import com.nuclearw.rapsheet.api.events.RapsheetChargeEvent;
import com.nuclearw.rapsheet.api.events.RapsheetConvictEvent;
import com.nuclearw.rapsheet.api.events.RapsheetExpungeEvent;
import com.nuclearw.rapsheet.api.events.RapsheetPardonEvent;
import com.nuclearw.rapsheet.api.events.RapsheetSealChangeEvent;
import com.nuclearw.rapsheet.locale.LocaleManager;

public class SimpleRapsheetManager implements RapsheetManager {
	private Rapsheet plugin;
	private LocaleManager locale;

	public SimpleRapsheetManager(Rapsheet plugin) {
		this.plugin = plugin;
		this.locale = plugin.getLocale();
	}

	@Override
	public List<Record> getCharges(UUID playerUUID) {
		return plugin.getDatabase().find(Record.class).where().ieq("offender", playerUUID.toString()).findList();
	}

	@Override
	public Record getCharge(UUID playerUUID, int chargeId) {
		return plugin.getDatabase().find(Record.class).where().ieq("offender", playerUUID.toString()).eq("charge_id", chargeId).findUnique();
	}

	@Override
	public int chargePlayer(UUID offenderUUID, UUID officialUUID, String shortDescription, String longDescription, NotifyChanges notify) {
		int newChargeId = 1;

		// I do not like the way this is done
		List<Record> found = getCharges(offenderUUID);

		if(found != null && !found.isEmpty()) {
			newChargeId = found.get(found.size() - 1).getChargeId() + 1;
		}

		Record record = new Record();
		record.setChargeId(newChargeId);
		record.setOffender(offenderUUID);
		record.setOfficial(officialUUID);
		record.setChargeShort(shortDescription);
		record.setChargeLong(longDescription);
		record.setTime(System.currentTimeMillis());
		record.setSealed(false);
		record.setState(RecordState.CHARGED);

		plugin.getDatabase().save(record);

		RapsheetChargeEvent chargeEvent = new RapsheetChargeEvent(offenderUUID, officialUUID, shortDescription, longDescription, newChargeId);
		plugin.getServer().getPluginManager().callEvent(chargeEvent);

		if(notify == NotifyChanges.NONE) {
			return newChargeId;
		}

		CommandSender sender = null;
		// Hard code console check?
		if(officialUUID.equals("CONSOLE")) {
			sender = plugin.getServer().getConsoleSender();
		} else {
			sender = plugin.getServer().getPlayer(officialUUID);
		}
		//Let's get the accused's nickname.
		
		Player player = Bukkit.getPlayer(offenderUUID);
		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFICIAL) {
			if(player == null && sender == null) {
				plugin.getLogger().warning("Could not notify official " + officialUUID + " of charge!");
			} else {
				if(player != null && sender == null) {
					sender = (CommandSender) player;
				}
				try {
					sender.sendMessage(ChatColor.GOLD + "Charged" + ChatColor.RESET + ": " + ChatColor.AQUA + Bukkit.getPlayer(offenderUUID).getDisplayName());
					sender.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + newChargeId + ChatColor.GOLD + " - " + ChatColor.AQUA + shortDescription);
					sender.sendMessage(ChatColor.GOLD + "Report" + ChatColor.RESET + ": " + ChatColor.GRAY + longDescription);
				} catch (NullPointerException ex) {
					sender.sendMessage(ChatColor.GOLD + "Charged" + ChatColor.RESET + ": " + ChatColor.AQUA + offenderUUID);
					sender.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + newChargeId + ChatColor.GOLD + " - " + ChatColor.AQUA + shortDescription);
					sender.sendMessage(ChatColor.GOLD + "Report" + ChatColor.RESET + ": " + ChatColor.GRAY + longDescription);
					ex.printStackTrace();
				}
			}
		}

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFENDER) {
			//if(player == null) {
				plugin.getLogger().warning("Could not notify offender " + offenderUUID + " of charge!");
			//} else {
				if(!officialUUID.equals("CONSOLE")) {
					player.sendMessage(ChatColor.GOLD + "Charged by " + ChatColor.AQUA + ((Player) sender).getDisplayName() + ChatColor.GOLD + " of " + ChatColor.GRAY + shortDescription);
					player.sendMessage(ChatColor.GOLD + "Report: " + ChatColor.GRAY  + longDescription);
					player.sendMessage(locale.getString("filed-under", new Object[] {newChargeId}));
				} else {
					player.sendMessage(ChatColor.GOLD + "Charged by " + ChatColor.AQUA + officialUUID + ChatColor.GOLD + " of " + ChatColor.GRAY + shortDescription);
					player.sendMessage(ChatColor.GOLD + "Report: " + ChatColor.GRAY  + longDescription);
					player.sendMessage(locale.getString("filed-under", new Object[] {newChargeId}));
			//	}
			}
		}

		return newChargeId;
	}

	@Override
	public boolean convictPlayer(UUID offenderUUID, UUID officialUUID, int chargeId, NotifyChanges notify) {
		Record found = getCharge(offenderUUID, chargeId);

		if(found == null) return false;

		found.setState(RecordState.CONVICTED);

		plugin.getDatabase().update(found);

		RapsheetConvictEvent convictEvent = new RapsheetConvictEvent(offenderUUID, officialUUID, chargeId);
		plugin.getServer().getPluginManager().callEvent(convictEvent);

		if(notify == NotifyChanges.NONE) {
			return true;
		}

		CommandSender sender = null;
		if(Bukkit.getPlayer(officialUUID).getName().equals("CONSOLE")) {
			//TODO WHAT KIND OF A METHOD IS Bukkit.getPlayer(officialUUID).getName().equals("CONSOLE")???????????
			sender = plugin.getServer().getConsoleSender();
		} else {
			sender = plugin.getServer().getPlayer(officialUUID);
		}

		Player player = plugin.getServer().getPlayer(offenderUUID);

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFICIAL) {
			if(player == null && sender == null) {
				//TODO Eval username of official. UUIDs... :P
				plugin.getLogger().warning("Could not notify official " + officialUUID + " of charge!");
			} else {
				if(player != null && sender == null) {
					sender = (CommandSender) player;
				}
				try {
					sender.sendMessage(ChatColor.DARK_RED + "Convicted" + ChatColor.RESET + ": " + ChatColor.AQUA + plugin.getServer().getPlayer(found.getOffender()).getDisplayName());
					sender.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());
					sender.sendMessage(ChatColor.GOLD + "Report" + ChatColor.RESET + ": " + ChatColor.GRAY + found.getChargeLong());
				} catch (NullPointerException ex) {
					sender.sendMessage(ChatColor.DARK_RED + "Convicted" + ChatColor.RESET + ": " + ChatColor.AQUA + offenderUUID);
					sender.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());
					sender.sendMessage(ChatColor.GOLD + "Report" + ChatColor.RESET + ": " + ChatColor.GRAY + found.getChargeLong());
				}
			}
		}

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFENDER) {
			if(player == null) {
				plugin.getLogger().warning("Could not notify offender " + offenderUUID + " of charge!");
			} else { //TODO Console UUID eval
				if (!officialUUID.equals("CONSOLE")) {
					player.sendMessage(ChatColor.DARK_RED + "Convicted" + ChatColor.GOLD + " by " + ChatColor.AQUA + plugin.getServer().getPlayer(UUIDFetcher.getUUIDOf(sender.getName())).getDisplayName() + ChatColor.GOLD + " of " + ChatColor.GRAY + found.getChargeShort());
					player.sendMessage(ChatColor.GOLD + "Report: " + ChatColor.GRAY + found.getChargeLong());
					player.sendMessage(ChatColor.GOLD + "Filed under Charge " + ChatColor.RESET + "#" + found.getChargeId());	
				} else {
					player.sendMessage(ChatColor.DARK_RED + "Convicted" + ChatColor.GOLD + " by " + ChatColor.AQUA + officialUUID + ChatColor.GOLD + " of " + ChatColor.GRAY + found.getChargeShort());
					player.sendMessage(ChatColor.GOLD + "Report: " + ChatColor.GRAY + found.getChargeLong());
					player.sendMessage(ChatColor.GOLD + "Filed under Charge " + ChatColor.RESET + "#" + found.getChargeId());
				}
			}
		}

		return true;
	}

	@Override
	public boolean pardonPlayer(UUID offenderUUID, UUID officialUUID, int chargeId, NotifyChanges notify) {
		Record found = getCharge(offenderUUID, chargeId);

		if(found == null) return false;

		found.setState(RecordState.PARDONED);

		plugin.getDatabase().update(found);

		RapsheetPardonEvent pardonEvent = new RapsheetPardonEvent(offenderUUID, officialUUID, chargeId);
		plugin.getServer().getPluginManager().callEvent(pardonEvent);

		if(notify == NotifyChanges.NONE) {
			return true;
		}

		CommandSender sender = null;
		// Hard code console check?
		if(officialUUID.equals("CONSOLE")) {
			sender = plugin.getServer().getConsoleSender();
		} else {
			sender = plugin.getServer().getPlayer(officialUUID);
		}

		Player player = plugin.getServer().getPlayer(offenderUUID);

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFICIAL) {
			if(player == null && sender == null) {
				plugin.getLogger().warning("Could not notify official " + officialUUID + " of charge!");
			} else {
				if(player != null && sender == null) {
					sender = (CommandSender) player;
				}
				try {
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "Pardoned" + ChatColor.RESET + ": " + ChatColor.AQUA + plugin.getServer().getPlayer(found.getOffender()).getDisplayName());
					sender.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());
					player.sendMessage(ChatColor.GOLD + "Report: " + ChatColor.GRAY  + found.getChargeLong());
				} catch (NullPointerException ex) {
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "Pardoned" + ChatColor.RESET + ": " + ChatColor.AQUA + offenderUUID);
					sender.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());
					player.sendMessage(ChatColor.GOLD + "Report: " + ChatColor.GRAY  + found.getChargeLong());
				}

			}
		}

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFENDER) {
			if(player == null) {
				plugin.getLogger().warning("Could not notify offender " + offenderUUID + " of charge!");
			} else {
				if(!officialUUID.equals("CONSOLE")) {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Pardoned" + ChatColor.GOLD + " by " + ChatColor.AQUA + plugin.getServer().getPlayer(UUIDFetcher.getUUIDOf(sender.getName())).getDisplayName() + ChatColor.GOLD + " of " + ChatColor.GRAY + found.getChargeShort());
					player.sendMessage(ChatColor.GOLD + "Filed under Charge " + ChatColor.RESET + "#" + found.getChargeId());
				} else {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Pardoned" + ChatColor.GOLD + " by " + ChatColor.AQUA + officialUUID + ChatColor.GOLD + " of " + ChatColor.GRAY + found.getChargeShort());
					player.sendMessage(ChatColor.GOLD + "Filed under Charge " + ChatColor.RESET + "#" + found.getChargeId());
				}

			}
		}

		return true;
	}

	@Override
	public boolean sealPlayerCharge(UUID offenderUUID, UUID officialUUID, int chargeId, NotifyChanges notify) {
		Record found = getCharge(offenderUUID, chargeId);

		if(found == null) return false;

		found.setSealed(true);

		plugin.getDatabase().update(found);

		RapsheetSealChangeEvent sealChangeEvent = new RapsheetSealChangeEvent(offenderUUID, officialUUID, chargeId, true);
		plugin.getServer().getPluginManager().callEvent(sealChangeEvent);

		if(notify == NotifyChanges.NONE) {
			return true;
		}

		CommandSender sender = null;
		// Hard code console check?
		if(officialUUID.equals("CONSOLE")) {
			sender = plugin.getServer().getConsoleSender();
		} else {
			sender = plugin.getServer().getPlayer(officialUUID);
		}
		Player player = plugin.getServer().getPlayer(offenderUUID);

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFICIAL) {
			if(player == null && sender == null) {
				plugin.getLogger().warning("Could not notify official " + officialUUID + " of charge!");
			} else {
				if(player != null && sender == null) {
					sender = (CommandSender) player;
				}
				sender.sendMessage(ChatColor.GRAY + "Sealed " + ChatColor.GOLD + "charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());
			}
		}

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFENDER) {
			if(player == null) {
				plugin.getLogger().warning("Could not notify offender " + offenderUUID + " of charge!");
			} else {
				if(!officialUUID.equals("CONSOLE")) {
					player.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " has been " + ChatColor.GRAY + "sealed" + ChatColor.GOLD + " by " + ChatColor.AQUA + plugin.getServer().getPlayer(UUIDFetcher.getUUIDOf(sender.getName())).getDisplayName());
				} else {
					player.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " has been " + ChatColor.GRAY + "sealed" + ChatColor.GOLD + " by " + ChatColor.AQUA + officialUUID);
				}
				
			}
		}

		return true;
	}

	@Override
	public boolean unsealPlayerCharge(UUID offenderUUID, UUID officialUUID, int chargeId, NotifyChanges notify) {
		Record found = getCharge(offenderUUID, chargeId);

		if(found == null) return false;

		found.setSealed(false);

		plugin.getDatabase().update(found);

		RapsheetSealChangeEvent sealChangeEvent = new RapsheetSealChangeEvent(offenderUUID, officialUUID, chargeId, false);
		plugin.getServer().getPluginManager().callEvent(sealChangeEvent);

		if(notify == NotifyChanges.NONE) {
			return true;
		}

		CommandSender sender = null;
		// Hard code console check?
		if(officialUUID.equals("CONSOLE")) {
			sender = plugin.getServer().getConsoleSender();
		} else {
			sender = plugin.getServer().getPlayer(officialUUID);
		}

		Player player = plugin.getServer().getPlayer(offenderUUID);

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFICIAL) {
			if(player == null && sender == null) {
				plugin.getLogger().warning("Could not notify official " + officialUUID + " of charge!");
			} else {
				if(player != null && sender == null) {
					sender = (CommandSender) player;
				}
				
				sender.sendMessage(ChatColor.GRAY + "Unsealed " + ChatColor.GOLD + "charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());
			}
		}

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFENDER) {
			if(player == null) {
				plugin.getLogger().warning("Could not notify offender " + offenderUUID + " of charge!");
			} else {
				if(!officialUUID.equals("CONSOLE")) {
					player.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " has been " + ChatColor.GRAY + "unsealed" + ChatColor.GOLD + " by " + ChatColor.AQUA + plugin.getServer().getPlayer(UUIDFetcher.getUUIDOf(sender.getName())).getDisplayName());
				} else {
					player.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " has been " + ChatColor.GRAY + "unsealed" + ChatColor.GOLD + " by " + ChatColor.AQUA + officialUUID);
				}
				
			}
		}

		return true;
	}

	@Override
	public boolean expungePlayerCharge(UUID offenderUUID, UUID officialUUID, int chargeId, NotifyChanges notify) {
		Record found = getCharge(offenderUUID, chargeId);

		if(found == null) return false;

		// This event before we delete it so as to let plugins actually do something with the knowledge it's being deleted before we delete it.
		RapsheetExpungeEvent expungeEvent = new RapsheetExpungeEvent(offenderUUID, officialUUID, chargeId);
		plugin.getServer().getPluginManager().callEvent(expungeEvent);

		plugin.getDatabase().delete(found);

		if(notify == NotifyChanges.NONE) {
			return true;
		}

		CommandSender sender = null;
		// Hard code console check?
		if(officialUUID.equals("CONSOLE")) {
			sender = plugin.getServer().getConsoleSender();
		} else {
			sender = plugin.getServer().getPlayer(officialUUID);
		}

		Player player = plugin.getServer().getPlayer(offenderUUID);

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFICIAL) {
			if(player == null && sender == null) {
				plugin.getLogger().warning("Could not notify official " + officialUUID + " of charge!");
			} else {
				if(player != null && sender == null) {
					sender = (CommandSender) player;
				}

				sender.sendMessage(ChatColor.GRAY + "Expunged " + ChatColor.GOLD + "charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());
			}
		}

		if(notify == NotifyChanges.BOTH || notify == NotifyChanges.OFFENDER) {
			if(player == null) {
				plugin.getLogger().warning("Could not notify offender " + offenderUUID + " of charge!");
			} else {
				if(!officialUUID.equals("CONSOLE")) {
					player.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " has been " + ChatColor.GRAY + "expunged" + ChatColor.GOLD + " by " + ChatColor.AQUA + plugin.getServer().getPlayer(UUIDFetcher.getUUIDOf(sender.getName())).getDisplayName());
				} else {
					player.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " has been " + ChatColor.GRAY + "expunged" + ChatColor.GOLD + " by " + ChatColor.AQUA + officialUUID);
				}
				
			}
		}

		return true;
	}
}
