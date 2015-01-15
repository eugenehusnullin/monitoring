package monitoring.handler.accelerometer;

import monitoring.domain.Message;
import monitoring.handler.AccelerometrConverter;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class AccelerometrPersistHandler implements Handler {

	private static final Logger logger = LoggerFactory.getLogger(AccelerometrPersistHandler.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	@Override
	public void handle(Message message, HandlerStrategy strategy) {
		try {

			AccelerometrConverter converter = strategy.getAccelerometrConverter();
			if (converter != null) {
				Accelerometr accelerometr = converter.convert(message);

				if (accelerometr != null) {
					Session session = sessionFactory.getCurrentSession();
					session.save(accelerometr);
				}
			}

		} catch (Exception e) {
			logger.error("AccelerometrPersistHandler exception: ", e);
		}
	}

}
