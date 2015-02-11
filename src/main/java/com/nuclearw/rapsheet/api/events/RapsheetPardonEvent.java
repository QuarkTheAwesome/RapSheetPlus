package com.nuclearw.rapsheet.api.events;

import java.util.UUID;


public class RapsheetPardonEvent extends RapsheetEvent {
	public RapsheetPardonEvent(UUID offenderUUID, UUID officialUUID, int chargeId) {
		super(offenderUUID, officialUUID, chargeId);
	}
}
