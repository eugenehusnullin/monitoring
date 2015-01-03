package monitoring.handler.position;

import monitoring.domain.Message;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;
import monitoring.handler.position.domain.Position;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

public class PositionPersistHandler implements Handler {

	private SessionFactory sessionFactory;

	@Transactional
	@Override
	public void handle(Message message, HandlerStrategy strategy) {

		PositionConverter positionConverter = strategy.getPositionConverter();
		if (positionConverter != null) {
			Position position = positionConverter.convert(message);

			if (position != null) {
				Session session = sessionFactory.getCurrentSession();
				session.save(position);
			}
		}
	}
}
