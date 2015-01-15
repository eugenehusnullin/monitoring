package monitoring.handler.accelerometer;

import java.util.Date;

public class Accelerometr {

	private Long id;
	private Long terminalId;
	private Date date;
	private Integer accX;
	private Integer accY;
	private Integer accZ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getAccX() {
		return accX;
	}

	public void setAccX(Integer accX) {
		this.accX = accX;
	}

	public Integer getAccY() {
		return accY;
	}

	public void setAccY(Integer accY) {
		this.accY = accY;
	}

	public Integer getAccZ() {
		return accZ;
	}

	public void setAccZ(Integer accZ) {
		this.accZ = accZ;
	}
}
