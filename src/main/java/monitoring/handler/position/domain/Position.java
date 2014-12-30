package monitoring.handler.position.domain;

import java.util.Date;

import monitoring.domain.TopAuto;

public class Position {
	private Long id;
	private Long terminalId;
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

	/**
	 * km/h (kilometer per hour)
	 * @return
	 */
	public Double getSpeed() {
		return speed;
	}

	/**
	 * km/h (kilometer per hour)
	 * @param speed
	 */
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

	public TopAuto getTopAuto() {
		return topAuto;
	}

	public void setTopAuto(TopAuto topAuto) {
		this.topAuto = topAuto;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}
}
