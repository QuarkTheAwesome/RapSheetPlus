package com.nuclearw.rapsheet.api.events;

public class RapsheetExpungeEvent extends RapsheetEvent {
	public RapsheetExpungeEvent(String offenderName, String officialName, int chargeId) {
		super(offenderName, officialName, chargeId);
	}
}
