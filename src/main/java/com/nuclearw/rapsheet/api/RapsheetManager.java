package com.nuclearw.rapsheet.api;

import java.util.List;
import java.util.UUID;

import com.nuclearw.rapsheet.Record;

public interface RapsheetManager {
	public List<Record> getCharges(UUID playerUUID);

	public Record getCharge(UUID playerUUID, int chargeId);

	public int chargePlayer(UUID offenderUUID, UUID officialUUID, String shortDescription, String longDescription, NotifyChanges notify);

	public boolean convictPlayer(UUID offenderUUID, UUID officialUUID, int chargeId, NotifyChanges notify);

	public boolean pardonPlayer(UUID offenderUUID, UUID officialUUID, int chargeId, NotifyChanges notify);

	public boolean sealPlayerCharge(UUID offenderUUID, UUID officialUUID, int chargeId, NotifyChanges notify);

	public boolean unsealPlayerCharge(UUID offenderUUID, UUID officialUUID, int chargeId, NotifyChanges notify);

	public boolean expungePlayerCharge(UUID offenderUUID, UUID officialUUID, int chargeId, NotifyChanges notify);
}
