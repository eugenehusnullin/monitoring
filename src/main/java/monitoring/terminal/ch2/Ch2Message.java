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
	private Integer ax;
	private Integer ay;
	private Integer az;
	private Integer gx;
	private Integer gy;
	private Integer gz;
	private String laccid;
	private double hdop;
	private String gpsSignal;

	public Integer getAx() {
		return ax;
	}

	public void setAx(Integer ax) {
		this.ax = ax;
	}

	public Integer getAy() {
		return ay;
	}

	public void setAy(Integer ay) {
		this.ay = ay;
	}

	public Integer getAz() {
		return az;
	}

	public void setAz(Integer az) {
		this.az = az;
	}

	public Integer getGx() {
		return gx;
	}

	public void setGx(Integer gx) {
		this.gx = gx;
	}

	public Integer getGy() {
		return gy;
	}

	public void setGy(Integer gy) {
		this.gy = gy;
	}

	public Integer getGz() {
		return gz;
	}

	public void setGz(Integer gz) {
		this.gz = gz;
	}

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

	public String getLaccid() {
		return laccid;
	}

	public void setLaccid(String laccid) {
		this.laccid = laccid;
	}

	public double getHdop() {
		return hdop;
	}

	public void setHdop(double hdop) {
		this.hdop = hdop;
	}

	public String getGpsSignal() {
		return gpsSignal;
	}

	public void setGpsSignal(String gpsSignal) {
		this.gpsSignal = gpsSignal;
	}

}
