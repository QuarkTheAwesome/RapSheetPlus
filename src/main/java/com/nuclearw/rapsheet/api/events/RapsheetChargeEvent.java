package com.nuclearw.rapsheet.api.events;

import java.util.UUID;

public class RapsheetChargeEvent extends RapsheetEvent {
	private final String shortDescription;
	private final String longDescription;

	public RapsheetChargeEvent(UUID offenderUUID, UUID officialUUID, final String shortDescription, final String longDescription, int chargeId) {
		super(offenderUUID, officialUUID, chargeId);

		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	public final String getShortDescription() {
		return shortDescription;
	}

	public final String getLongDescription() {
		return longDescription;
	}
}
