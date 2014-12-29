package monitoring.handler;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import monitoring.domain.HasPosition;
import monitoring.domain.Message;
import monitoring.domain.Position;
import monitoring.domain.TopAuto;

public class PositionPersistHandler implements IPositionHandler {
	
	private SessionFactory sessionFactory;

	@Transactional
	@Override
	public void handle(Message message) {
		if (message instanceof HasPosition) {
			HasPosition hasPosition = (HasPosition) message;
			Position position = hasPosition.getPosition();

			if (position != null) {
				Session session = sessionFactory.getCurrentSession();
				TopAuto topAuto = (TopAuto) session.createCriteria(TopAuto.class)
						.add(Restrictions.eq("asset", position.getTerminalId()))
						.uniqueResult();
				
				if (topAuto != null) {
					position.setTopAuto(topAuto);
				}
				
				session.save(position);
			}
		}
	}
}
