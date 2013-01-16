package com.nuclearw.rapsheet.api.events;

public class RapsheetConvictEvent extends RapsheetEvent {
	public RapsheetConvictEvent(String offenderName, String officialName, final int chargeId) {
		super(offenderName, officialName, chargeId);
	}
}
