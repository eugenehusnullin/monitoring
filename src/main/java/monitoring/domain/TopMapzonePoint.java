package monitoring.domain;

import java.io.Serializable;

public class TopMapzonePoint implements Serializable {
	private static final long serialVersionUID = 1054955808648938296L;
//	private Integer id;
	private TopMapzone mapzone;
	private Integer sort;
	private float lat;
	private float lng;

//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}

	public TopMapzone getMapzone() {
		return mapzone;
	}

	public void setMapzone(TopMapzone mapzone) {
		this.mapzone = mapzone;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}
}
