package ru.gm.munic.domain;

import java.io.Serializable;
import java.util.Date;

public class MunicData implements Serializable {
	private static final long serialVersionUID = -5210229116835535429L;
	private MunicItemRawData municItemRawData;
	private String event;
	private Long asset;
	private Date recordedAt;
	private Double lat;
	private Double lon;

	private Integer gpsSpeed;
	private Integer gpsDir;
	private Boolean dioIgnition;
	private Integer odoFull;

	private MunicDataBehave municDataBehave;
	private MunicDataMdi municDataMdi;

	public MunicItemRawData getMunicItemRawData() {
		return municItemRawData;
	}

	public void setMunicItemRawData(MunicItemRawData municItemRawData) {
		this.municItemRawData = municItemRawData;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
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

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Integer getGpsSpeed() {
		return gpsSpeed;
	}

	public void setGpsSpeed(Integer gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}

	public Integer getGpsDir() {
		return gpsDir;
	}

	public void setGpsDir(Integer gpsDir) {
		this.gpsDir = gpsDir;
	}

	public Boolean getDioIgnition() {
		return dioIgnition;
	}

	public void setDioIgnition(Boolean dioIgnition) {
		this.dioIgnition = dioIgnition;
	}

	public Integer getOdoFull() {
		return odoFull;
	}

	public void setOdoFull(Integer odoFull) {
		this.odoFull = odoFull;
	}

	public MunicDataBehave getMunicDataBehave() {
		return municDataBehave;
	}

	public void setMunicDataBehave(MunicDataBehave municDataBehave) {
		this.municDataBehave = municDataBehave;
	}

	public MunicDataMdi getMunicDataMdi() {
		return municDataMdi;
	}

	public void setMunicDataMdi(MunicDataMdi municDataMdi) {
		this.municDataMdi = municDataMdi;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
