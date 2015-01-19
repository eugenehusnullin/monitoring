package monitoring.handler.position.maparea;

import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.List;

public class Maparea implements Serializable {
	private static final long serialVersionUID = -8659914248870197177L;
	private Integer id;
	private String name;
	private Integer type;
	private List<MapareaPoint> points;

	private Path2D boundary;

	public Path2D initBoundary() {
		boundary = new Path2D.Double();

		boolean isFirst = true;
		for (MapareaPoint point : points) {
			if (isFirst) {
				boundary.moveTo(point.getLat(), point.getLon());
				isFirst = false;
			} else {
				boundary.lineTo(point.getLat(), point.getLon());
			}
		}
		boundary.closePath();

		return boundary;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<MapareaPoint> getPoints() {
		return points;
	}

	public void setPoints(List<MapareaPoint> points) {
		this.points = points;
	}

	public Path2D getBoundary() {
		return boundary;
	}

	public void setBoundary(Path2D boundary) {
		this.boundary = boundary;
	}
}
