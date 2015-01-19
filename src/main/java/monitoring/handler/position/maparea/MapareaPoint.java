package monitoring.handler.position.maparea;

import java.io.Serializable;

public class MapareaPoint implements Serializable {
	private static final long serialVersionUID = 1054955808648938296L;
	private Maparea maparea;
	private Integer sort;
	private float lat;
	private float lon;

	public Maparea getMaparea() {
		return maparea;
	}

	public void setMaparea(Maparea maparea) {
		this.maparea = maparea;
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

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}
}
