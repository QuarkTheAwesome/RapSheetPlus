package com.nuclearw.rapsheet.api;

import com.nuclearw.rapsheet.api.events.RapsheetEvent;

public class RapsheetPardonEvent extends RapsheetEvent {
	public RapsheetPardonEvent(String offenderName, String officialName, int chargeId) {
		super(offenderName, officialName, chargeId);
	}
}
