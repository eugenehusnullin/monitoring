package monitoring.terminal.munic.domain;

import monitoring.terminal.munic.trash.ItemRawDataJson;

@Deprecated
public class MunicItemRawData {
	private Long id;
	private MunicRawData municRawData;
	private String itemRawData;
	private ItemRawDataJson itemRawDataJson;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public ItemRawDataJson getItemRawDataJson() {
		return itemRawDataJson;
	}

	public void setItemRawDataJson(ItemRawDataJson itemRawDataJson) {
		this.itemRawDataJson = itemRawDataJson;
	}
}
