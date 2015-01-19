package monitoring.handler.position.maparea;

import java.util.List;

import monitoring.handler.position.PositionHandler;
import monitoring.handler.position.Position;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class MapareaDetector implements PositionHandler {
	private static final Logger logger = LoggerFactory.getLogger(MapareaDetector.class);
	private SessionFactory sessionFactory;
	private List<Maparea> mapareas;

	@Override
	@Transactional
	public void handle(Position position) {
		if (position == null) {
			return;
		}

		if (position.getLat() != null && position.getLon() != null) {
			Maparea maparea = check(position.getLat(), position.getLon());
			if (maparea != null) {
				PositionInMaparea positionInMaparea = new PositionInMaparea();
				positionInMaparea.setPosition(position);
				positionInMaparea.setMaparea(maparea);

				Session session = sessionFactory.getCurrentSession();
				session.save(positionInMaparea);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void start() {
		Session session = sessionFactory.openSession();
		mapareas = session.createCriteria(Maparea.class).list();

		int cnt = 0;
		if (mapareas != null) {
			cnt = mapareas.size();

			for (Maparea mapzone : mapareas) {
				mapzone.initBoundary();
			}
		}
		logger.info("MapareaDetector. Loaded mapareas count: " + cnt);
	}

	private Maparea check(double lat, double lng) {
		for (Maparea maparea : mapareas) {
			boolean b = maparea.getBoundary().contains(lat, lng);
			if (b) {
				return maparea;
			}
		}
		return null;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
