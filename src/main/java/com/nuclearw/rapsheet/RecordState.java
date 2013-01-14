package com.nuclearw.rapsheet;

public enum RecordState {
	CHARGED(1), CONVICTED(2), PARDONED(3);

	private int value;

	RecordState(int value) {
		this.value = value;
	}

	public int toInt() {
		return value;
	}

	public static RecordState fromInt(int value) {
		switch (value) {
		case 1:
			return CHARGED;
		case 2:
			return CONVICTED;
		case 3:
			return PARDONED;
		default:
			return null;
		}
	}
}
