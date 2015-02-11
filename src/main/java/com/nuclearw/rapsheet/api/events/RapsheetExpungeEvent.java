package com.nuclearw.rapsheet.api.events;

import java.util.UUID;

public class RapsheetExpungeEvent extends RapsheetEvent {
	public RapsheetExpungeEvent(UUID offenderUUID, UUID officialUUID, int chargeId) {
		super(offenderUUID, officialUUID, chargeId);
	}
}
