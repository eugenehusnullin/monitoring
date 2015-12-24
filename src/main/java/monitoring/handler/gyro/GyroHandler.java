package monitoring.handler.gyro;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import monitoring.domain.Message;
import monitoring.handler.GyroConverter;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;

public class GyroHandler implements Handler {

	private static final Logger logger = LoggerFactory.getLogger(GyroHandler.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public void handle(Message message, HandlerStrategy strategy) {
		try {

			GyroConverter converter = strategy.getGyroConverter();
			if (converter != null) {
				Gyro gyro = converter.convert(message);

				if (gyro != null) {
					Session session = sessionFactory.getCurrentSession();
					session.save(gyro);
				}
			}

		} catch (Exception e) {
			logger.error("GyroHandler exception: ", e);
		}
	}
}
