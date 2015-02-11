package com.nuclearw.rapsheet.api.events;

import java.util.UUID;

public class RapsheetConvictEvent extends RapsheetEvent {
	public RapsheetConvictEvent(UUID offenderUUID, UUID officialUUID, final int chargeId) {
		super(offenderUUID, officialUUID, chargeId);
	}
}
