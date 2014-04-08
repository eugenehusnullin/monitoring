package ru.gm.munic.domain;

import java.util.Date;

public class Message {
	private Long id;
	private Long asset;
	private String value;
	private Date recordedAt;
	private Date recievedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getAsset() {
		return asset;
	}

	public void setAsset(Long asset) {
		this.asset = asset;
	}

	public Date getRecordedAt() {
		return recordedAt;
	}

	public void setRecordedAt(Date recordedAt) {
		this.recordedAt = recordedAt;
	}

	public Date getRecievedAt() {
		return recievedAt;
	}

	public void setRecievedAt(Date recievedAt) {
		this.recievedAt = recievedAt;
	}
}
