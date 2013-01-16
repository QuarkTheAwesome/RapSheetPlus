package com.nuclearw.rapsheet.api.events;


public class RapsheetSealChangeEvent extends RapsheetEvent {
	private final boolean sealing;

	public RapsheetSealChangeEvent(String offenderName, String officialName, int chargeId, final boolean sealing) {
		super(offenderName, officialName, chargeId);

		this.sealing = sealing;
	}

	public final boolean isSealing() {
		return sealing;
	}
}
