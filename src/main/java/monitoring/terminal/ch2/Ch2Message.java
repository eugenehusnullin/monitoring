package monitoring.terminal.ch2;

import java.util.Date;

import monitoring.domain.Message;

public class Ch2Message implements Message {
	private long terminalId;
	private String cmd;
	private Boolean validLocation;
	private Date date;
	private Double latitude;
	private Double longitude;
	private Double speed;
	private String other;
	private Integer course;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Boolean getValidLocation() {
		return validLocation;
	}

	public void setValidLocation(Boolean validLocation) {
		this.validLocation = validLocation;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	@Override
	public long getTerminalId() {
		return terminalId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public Integer getCourse() {
		return course;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

}
