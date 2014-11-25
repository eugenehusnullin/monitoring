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

	private String gprmcValid;
	private Integer gpsSpeed;
	private Integer gpsDir;
	private Boolean dioIgnition;
	private Integer odoFull;
	private Boolean dioAlarm;
	private String driverId;
	private Integer dioInTor;
	private Integer batt;
	private Integer gprsHeader;
	private Integer rssi;
	private Integer battVolt;
	private Boolean mvtState;

	private Integer topAutoId;

	private MunicDataBehave municDataBehave;
	private MunicDataMdi municDataMdi;

	private Integer mapzoneId;
	private Integer mapzoneType;

	public boolean hasLocation() {
		return lat != null && lon != null;
	}

	public boolean hasVIN() {
		if (municDataMdi != null) {
			return municDataMdi.getMdiObdVin() != null;
		}
		return false;
	}

	public boolean hasMileage() {
		if (municDataMdi != null) {
			return municDataMdi.getMdiObdMileage() != null;
		}
		return false;
	}

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

	public String getGprmcValid() {
		return gprmcValid;
	}

	public void setGprmcValid(String gprmcValid) {
		this.gprmcValid = gprmcValid;
	}

	public Boolean getDioAlarm() {
		return dioAlarm;
	}

	public void setDioAlarm(Boolean dioAlarm) {
		this.dioAlarm = dioAlarm;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public Integer getDioInTor() {
		return dioInTor;
	}

	public void setDioInTor(Integer dioInTor) {
		this.dioInTor = dioInTor;
	}

	public Integer getBatt() {
		return batt;
	}

	public void setBatt(Integer batt) {
		this.batt = batt;
	}

	public Integer getGprsHeader() {
		return gprsHeader;
	}

	public void setGprsHeader(Integer gprsHeader) {
		this.gprsHeader = gprsHeader;
	}

	public Integer getRssi() {
		return rssi;
	}

	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}

	public Integer getBattVolt() {
		return battVolt;
	}

	public void setBattVolt(Integer battVolt) {
		this.battVolt = battVolt;
	}

	public Boolean getMvtState() {
		return mvtState;
	}

	public void setMvtState(Boolean mvtState) {
		this.mvtState = mvtState;
	}

	public Integer getTopAutoId() {
		return topAutoId;
	}

	public void setTopAutoId(Integer topAutoId) {
		this.topAutoId = topAutoId;
	}

	public Integer getMapzoneId() {
		return mapzoneId;
	}

	public void setMapzoneId(Integer mapzoneId) {
		this.mapzoneId = mapzoneId;
	}

	public Integer getMapzoneType() {
		return mapzoneType;
	}

	public void setMapzoneType(Integer mapzoneType) {
		this.mapzoneType = mapzoneType;
	}
}
