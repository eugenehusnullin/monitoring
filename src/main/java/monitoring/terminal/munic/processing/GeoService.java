package monitoring.terminal.munic.processing;

import java.util.List;

import javax.annotation.PostConstruct;

import monitoring.domain.TopMapzone;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeoService {
	private static final Logger logger = LoggerFactory.getLogger(GeoService.class);

	@Autowired
	private SessionFactory sessionFactory;

	private List<TopMapzone> mapzones;

	@SuppressWarnings("unchecked")
	@PostConstruct
	@Transactional
	public void LoadDefs() {
		Session session = sessionFactory.openSession();

		mapzones = session.createCriteria(TopMapzone.class).list();
		
		int cnt = 0;
		if (mapzones != null) {
			cnt = mapzones.size();
			
			for (TopMapzone mapzone : mapzones) {
				mapzone.initBoundary();
			}
		}
		logger.trace("GeoService. Loaded mapzones count: " + cnt);
	}
	
	public TopMapzone checkMapzone(double lat, double lng) {
		for (TopMapzone mapzone : mapzones) {
			boolean b = mapzone.getBoundary().contains(lat, lng);
			if (b) {
				return mapzone;
			}
		}
		
		return null;
	}
}
