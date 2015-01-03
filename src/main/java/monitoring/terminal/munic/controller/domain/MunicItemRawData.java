package monitoring.terminal.munic.controller.domain;

import monitoring.terminal.munic.processing.utils.ItemRawDataJson;

public class MunicItemRawData {
	private Long id;
	private Boolean processed;
	private Boolean wialonSended;
	private MunicRawData municRawData;
	private String itemRawData;
	private ItemRawDataJson itemRawDataJson;

	public MunicItemRawData() {
		processed = false;
		setWialonSended(false);
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

	public String getItemRawData() {
		return itemRawData;
	}

	public void setItemRawData(String itemRawDataString) {
		this.itemRawData = itemRawDataString;
	}

	public MunicRawData getMunicRawData() {
		return municRawData;
	}

	public void setMunicRawData(MunicRawData municRawData) {
		this.municRawData = municRawData;
	}

	public Boolean getWialonSended() {
		return wialonSended;
	}

	public void setWialonSended(Boolean wialonSended) {
		this.wialonSended = wialonSended;
	}

	public ItemRawDataJson getItemRawDataJson() {
		return itemRawDataJson;
	}

	public void setItemRawDataJson(ItemRawDataJson itemRawDataJson) {
		this.itemRawDataJson = itemRawDataJson;
	}
}
