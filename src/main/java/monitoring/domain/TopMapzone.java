package monitoring.domain;

import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.List;

public class TopMapzone implements Serializable {
	private static final long serialVersionUID = -8659914248870197177L;
	private Integer id;
	private String name;
	private Integer type;

	private List<TopMapzonePoint> points;

	private Path2D boundary;

	public Path2D initBoundary() {
		boundary = new Path2D.Double();

		boolean isFirst = true;
		for (TopMapzonePoint point : points) {
			if (isFirst) {
				boundary.moveTo(point.getLat(), point.getLng());
				isFirst = false;
			} else {
				boundary.lineTo(point.getLat(), point.getLng());
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

	public List<TopMapzonePoint> getPoints() {
		return points;
	}

	public void setPoints(List<TopMapzonePoint> points) {
		this.points = points;
	}

	public Path2D getBoundary() {
		return boundary;
	}

	public void setBoundary(Path2D boundary) {
		this.boundary = boundary;
	}
}
