package com.nuclearw.rapsheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name="bg_log")
public class Record {
	@Id
	@Column(name="id")
	private int id;

	@NotNull
	@Column(name="charge_id")
	private int chargeId;

	@NotEmpty
	@Column(name="offender")
	private String offender;

	@NotEmpty
	@Column(name="official")
	private String official;

	@NotEmpty
	@Column(name="charge_short")
	private String chargeShort;

	@NotEmpty
	@Column(name="charge_long")
	private String chargeLong;

	@NotNull
	@Column(name="time")
	private long time;

	@NotNull
	@Column(name="sealed")
	private boolean sealed;

	@Enumerated
	@Column(name="state")
	private RecordState state;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getChargeId() {
		return chargeId;
	}

	public void setChargeId(int chargeId) {
		this.chargeId = chargeId;
	}

	public String getOffender() {
		return offender;
	}

	public void setOffender(String offender) {
		this.offender = offender;
	}

	public String getOfficial() {
		return official;
	}

	public void setOfficial(String official) {
		this.official = official;
	}

	public String getChargeShort() {
		return chargeShort;
	}

	public void setChargeShort(String chargeShort) {
		this.chargeShort = chargeShort;
	}

	public String getChargeLong() {
		return chargeLong;
	}

	public void setChargeLong(String chargeLong) {
		this.chargeLong = chargeLong;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isSealed() {
		return sealed;
	}

	public void setSealed(boolean sealed) {
		this.sealed = sealed;
	}

	public RecordState getState() {
		return state;
	}

	public void setState(RecordState state) {
		this.state = state;
	}
}
