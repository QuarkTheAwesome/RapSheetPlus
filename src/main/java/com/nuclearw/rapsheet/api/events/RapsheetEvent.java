package com.nuclearw.rapsheet.api.events;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class RapsheetEvent extends Event {
	protected final UUID offenderUUID;
	protected final UUID officialUUID;
	protected final int chargeId;

	public RapsheetEvent(final UUID offenderUUID, final UUID officialUUID, final int chargeId) {
		this.officialUUID = officialUUID;
		this.offenderUUID = offenderUUID;
		this.chargeId = chargeId;
	}

	public final UUID getOffender() {
		return offenderUUID;
	}

	public final UUID getOfficial() {
		return officialUUID;
	}

	public final int getChargeId() {
		return chargeId;
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
