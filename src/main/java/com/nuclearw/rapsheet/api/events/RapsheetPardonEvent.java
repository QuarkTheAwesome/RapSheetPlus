package com.nuclearw.rapsheet.api.events;


public class RapsheetPardonEvent extends RapsheetEvent {
	public RapsheetPardonEvent(String offenderName, String officialName, int chargeId) {
		super(offenderName, officialName, chargeId);
	}
}
