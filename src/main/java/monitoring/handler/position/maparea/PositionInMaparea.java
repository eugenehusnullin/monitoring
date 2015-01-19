package monitoring.handler.position.maparea;

import java.io.Serializable;

import monitoring.handler.position.Position;

public class PositionInMaparea implements Serializable {
	private static final long serialVersionUID = 3838348241050655613L;
	private Position position;
	private Maparea maparea;

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Maparea getMaparea() {
		return maparea;
	}

	public void setMaparea(Maparea maparea) {
		this.maparea = maparea;
	}
}
