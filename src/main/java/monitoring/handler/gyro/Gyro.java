package monitoring.handler.gyro;

import java.util.Date;

public class Gyro {
	private Long id;
	private Long terminalId;
	private Date date;
	private Integer gx;
	private Integer gy;
	private Integer gz;

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
}
