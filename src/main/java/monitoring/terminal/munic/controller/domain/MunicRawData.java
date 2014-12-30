package monitoring.terminal.munic.controller.domain;

import java.util.Date;

public class MunicRawData {
	private Long id;
	private String rawData;
	private Boolean processed;
	private Date arrived;

	public MunicRawData() {
		processed = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
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
