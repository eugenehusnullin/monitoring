package monitoring.handler.position;

import java.util.List;

import monitoring.domain.Message;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;
import monitoring.handler.PositionConverter;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class PositionPersistHandler implements Handler {
	private static final Logger logger = LoggerFactory.getLogger(PositionPersistHandler.class);
	private SessionFactory sessionFactory;
	private List<PositionHandler> positionHandlers;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setPositionHandlers(List<PositionHandler> positionHandlers) {
		this.positionHandlers = positionHandlers;
	}

	@Transactional
	@Override
	public void handle(Message message, HandlerStrategy strategy) {
		try {

			PositionConverter positionConverter = strategy.getPositionConverter();
			if (positionConverter != null) {
				Position position = positionConverter.convert(message);

				if (position != null) {
					Session session = sessionFactory.getCurrentSession();
					session.save(position);

					if (positionHandlers != null) {
						for (PositionHandler positionHandler : positionHandlers) {
							positionHandler.handle(position);
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error("PositionPersistHandler exception: ", e);
		}
	}
}
