package com.nuclearw.rapsheet.api.events;

public class RapsheetChargeEvent extends RapsheetEvent {
	private final String shortDescription;
	private final String longDescription;

	public RapsheetChargeEvent(String offenderName, String officialName, final String shortDescription, final String longDescription, int chargeId) {
		super(offenderName, officialName, chargeId);

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
