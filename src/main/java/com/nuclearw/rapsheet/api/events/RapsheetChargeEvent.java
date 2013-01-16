package com.nuclearw.rapsheet.api.events;

public class RapsheetChargeEvent extends RapsheetEvent {
	private final String shortDescription;
	private final String longDescription;
	private final int chargeId;

	public RapsheetChargeEvent(String offenderName, String officialName, final String shortDescription, final String longDescription, final int chargeId) {
		super(offenderName, officialName);

		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.chargeId = chargeId;
	}

	public final String getShortDescription() {
		return shortDescription;
	}

	public final String getLongDescription() {
		return longDescription;
	}

	public final int getChargeId() {
		return chargeId;
	}
}
