package monitoring.service.processing;

import java.util.List;

import monitoring.domain.TopAuto;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
public class OlapService {
	private static final Logger logger = LoggerFactory.getLogger(OlapService.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	@Scheduled(cron = "0 30 * * * *")
	public void olapBehave() {
		logger.info("olapBehave executing.");

		Session session = sessionFactory.getCurrentSession();

		SQLQuery sqlQuery = session.createSQLQuery("call olap_behave_calc();");
		sqlQuery.executeUpdate();
	}
	
	@Transactional
	@Scheduled(cron = "0 30 * * * *")
	public void olapDistance() {
		logger.info("olapDistance executing.");

		Session session = sessionFactory.getCurrentSession();

		SQLQuery sqlQuery = session.createSQLQuery("call olap_distance_calc();");
		sqlQuery.executeUpdate();
	}
	
	@SuppressWarnings("unused")
	private void calcDicstance() {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<TopAuto> blocks = session.createCriteria(TopAuto.class).list();
		
		for (TopAuto block : blocks) {
			
		}
	}
}
