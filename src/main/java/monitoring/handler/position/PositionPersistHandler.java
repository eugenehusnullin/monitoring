package monitoring.handler.position;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import monitoring.domain.Message;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;
import monitoring.handler.PositionConverter;
import monitoring.state.DeviceStateHolder;

public class PositionPersistHandler implements Handler {
	private static final Logger logger = LoggerFactory.getLogger(PositionPersistHandler.class);
	private SessionFactory sessionFactory;
	private List<PositionHandler> positionHandlers;
	private DeviceStateHolder deviceStateHolder;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setPositionHandlers(List<PositionHandler> positionHandlers) {
		this.positionHandlers = positionHandlers;
	}

	public void setDeviceStateHolder(DeviceStateHolder deviceStateHolder) {
		this.deviceStateHolder = deviceStateHolder;
	}

	@Transactional
	@Override
	public void handle(Message message, HandlerStrategy strategy) {
		try {

			PositionConverter positionConverter = strategy.getPositionConverter();
			if (positionConverter != null) {
				Position position = positionConverter.convert(message);

				if (position != null) {

					position.setAbnormal(checkCrazy(position));
					if (position.getAbnormal()) {
						// don't valid or crazy speed
						logger.info("Crazy position detected: TerminalId=" + position.getTerminalId() + " Date="
								+ position.getDate() + " Valid=" + position.getGpsValid() + " Speed="
								+ position.getSpeed());
					}

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

	private boolean checkCrazy(Position position) {
		if (deviceStateHolder != null) {
			return deviceStateHolder.checkCrazy(position);
		} else {
			return false;
		}
	}
}
