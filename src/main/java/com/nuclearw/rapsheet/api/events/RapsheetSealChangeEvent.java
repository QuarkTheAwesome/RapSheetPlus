package com.nuclearw.rapsheet.api.events;

import java.util.UUID;


public class RapsheetSealChangeEvent extends RapsheetEvent {
	private final boolean sealing;

	public RapsheetSealChangeEvent(UUID offenderUUID, UUID officialUUID, int chargeId, final boolean sealing) {
		super(offenderUUID, officialUUID, chargeId);

		this.sealing = sealing;
	}

	public final boolean isSealing() {
		return sealing;
	}
}
