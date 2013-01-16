package com.nuclearw.rapsheet.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class RapsheetEvent extends Event {
	protected final String offenderName;
	protected final String officialName;

	public RapsheetEvent(final String offenderName, final String officialName) {
		this.officialName = officialName;
		this.offenderName = offenderName;
	}

	public final String getOffender() {
		return offenderName;
	}

	public final String getOfficial() {
		return officialName;
	}

	// Required boilerplate
	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
