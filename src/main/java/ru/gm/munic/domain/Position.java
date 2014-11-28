package ru.gm.munic.domain;

import java.util.Date;

public class Position {
	private Long id;
	private Long imei;
	private TopAuto topAuto;
	private Date date;
	private Double lat;
	private Double lon;
	private Double speed;
	private Integer satellites;
	private Double accuracy;
	private Double course;
	private Double altitude;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Integer getSatellites() {
		return satellites;
	}

	public void setSatellites(Integer satellites) {
		this.satellites = satellites;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	public Double getCourse() {
		return course;
	}

	public void setCourse(Double course) {
		this.course = course;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public Long getImei() {
		return imei;
	}

	public void setImei(Long imei) {
		this.imei = imei;
	}

	public TopAuto getTopAuto() {
		return topAuto;
	}

	public void setTopAuto(TopAuto topAuto) {
		this.topAuto = topAuto;
	}
}
