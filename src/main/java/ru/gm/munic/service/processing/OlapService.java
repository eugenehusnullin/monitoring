package ru.gm.munic.service.processing;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@EnableScheduling
public class OlapService {
	private static final Logger logger = LoggerFactory.getLogger(OlapService.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	@Scheduled(cron = "0 30 * * * *")
	public void olapBehave() {
		logger.info("olapBehave called.");

		Session session = sessionFactory.getCurrentSession();

		SQLQuery sqlQuery = session.createSQLQuery("call olap_behave_calc();");
		sqlQuery.executeUpdate();
	}
}
