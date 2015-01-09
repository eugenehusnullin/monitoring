package monitoring.terminal.munic.domain;

import java.util.Date;

public class MunicRawData {
	private Long id;
	private String rawData;
	private Date arrived;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawDataString) {
		this.rawData = rawDataString;
	}

	public Date getArrived() {
		return arrived;
	}

	public void setArrived(Date arrived) {
		this.arrived = arrived;
	}
}
