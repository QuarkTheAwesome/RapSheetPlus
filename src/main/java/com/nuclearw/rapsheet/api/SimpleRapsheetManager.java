package com.nuclearw.rapsheet.api;

import java.util.List;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;
import com.nuclearw.rapsheet.RecordState;

public class SimpleRapsheetManager implements RapsheetManager {
	private Rapsheet plugin;

	public SimpleRapsheetManager(Rapsheet plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<Record> getCharges(String playerName) {
		return plugin.getDatabase().find(Record.class).where().ieq("offender", playerName).findList();
	}

	@Override
	public Record getCharge(String playerName, int chargeId) {
		return plugin.getDatabase().find(Record.class).where().ieq("offender", playerName).eq("charge_id", chargeId).findUnique();
	}

	@Override
	public int chargePlayer(String offenderName, String officialName, String shortDesciption, String longDescription) {
		int newChargeId = 1;

		// I do not like the way this is done
		List<Record> found = getCharges(offenderName);

		if(found != null && !found.isEmpty()) {
			newChargeId = found.get(found.size() - 1).getChargeId() + 1;
		}

		Record record = new Record();
		record.setChargeId(newChargeId);
		record.setOffender(offenderName);
		record.setOfficial(officialName);
		record.setChargeShort(shortDesciption);
		record.setChargeLong(longDescription);
		record.setTime(System.currentTimeMillis());
		record.setSealed(false);
		record.setState(RecordState.CHARGED);

		plugin.getDatabase().save(record);

		return newChargeId;
	}

	@Override
	public boolean convictPlayer(String offenderName, int chargeId) {
		Record found = getCharge(offenderName, chargeId);

		if(found == null) return false;

		found.setState(RecordState.CONVICTED);

		plugin.getDatabase().update(found);

		return true;
	}

	@Override
	public boolean pardonPlayer(String offenderName, int chargeId) {
		Record found = getCharge(offenderName, chargeId);

		if(found == null) return false;

		found.setState(RecordState.PARDONED);

		plugin.getDatabase().update(found);

		return true;
	}

	@Override
	public boolean sealPlayerCharge(String offenderName, int chargeId) {
		Record found = getCharge(offenderName, chargeId);

		if(found == null) return false;

		found.setSealed(true);

		plugin.getDatabase().update(found);

		return true;
	}

	@Override
	public boolean unsealPlayerCharge(String offenderName, int chargeId) {
		Record found = getCharge(offenderName, chargeId);

		if(found == null) return false;

		found.setSealed(false);

		plugin.getDatabase().update(found);

		return true;
	}

	@Override
	public boolean expungePlayerCharge(String offenderName, int chargeId) {
		Record found = getCharge(offenderName, chargeId);

		if(found == null) return false;

		plugin.getDatabase().delete(found);

		return true;
	}
}
