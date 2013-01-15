package com.nuclearw.rapsheet.api;

import java.util.List;

import com.nuclearw.rapsheet.Record;

public interface RapsheetManager {
	public List<Record> getCharges(String playerName);

	public Record getCharge(String playerName, int chargeId);

	public int chargePlayer(String offenderName, String officialName, String shortDesciption, String longDescription);

	public boolean convictPlayer(String offenderName, int chargeId);

	public boolean pardonPlayer(String offenderName, int chargeId);

	public boolean sealPlayerCharge(String offenderName, int chargeId);

	public boolean unsealPlayerCharge(String offenderName, int chargeId);

	public boolean expungePlayerCharge(String offenderName, int chargeId);
}
