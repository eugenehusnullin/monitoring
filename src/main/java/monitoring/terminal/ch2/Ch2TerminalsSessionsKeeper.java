package monitoring.terminal.ch2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import io.netty.channel.ChannelHandlerContext;
import monitoring.domain.AlarmDeviceDetached;
import monitoring.domain.AlarmVinChanged;
import monitoring.domain.Car;
import monitoring.domain.Message;
import monitoring.handler.wialon.WialonMessage;

@EnableScheduling
public class Ch2TerminalsSessionsKeeper {
	private static final Logger logger = LoggerFactory.getLogger(Ch2TerminalsSessionsKeeper.class);
	private Map<Long, Ch2TerminalSession> sessionMap = new HashMap<Long, Ch2TerminalSession>();

	@Autowired
	private SessionFactory sessionFactory;

	private Map<Long, Ch2DemoInfo> demoInfoMap = new HashMap<>();
	private String commandGetVin = ":123456Z08#";
	private String commandObsoleteGetVin = ":123456Z296#";

	@Transactional
	public void messageArrived(Message message, ChannelHandlerContext ctx) {
		Car car = getCar(message.getTerminalId());
		if (car == null) {
			logger.info(message.getTerminalId() + ", device not found in scoring.");
			return;
		}

		putTerminalSession(message.getTerminalId(), ctx);

		// demo
		Ch2DemoInfo demoInfo = getDemoInfo(message.getTerminalId());
		if (demoInfo != null) {
			if (message instanceof Ch2Message) {
				Ch2Message mm = (Ch2Message) message;
				demoInfo.setLastDateCoord(new Date());
				demoInfo.setLat(mm.getLatitude());
				demoInfo.setLon(mm.getLongitude());

				if (demoInfo.getVin() != null) {
					mm.setVin(demoInfo.getVin());
				}

				if (demoInfo.isDetachAlarmed()) {
					AlarmDeviceDetached last = getLastAlarmDeviceDetached(message.getTerminalId());
					if (last != null) {
						last.setOnDate(new Date());
						last.setOnLat(demoInfo.getLat());
						last.setOnLon(demoInfo.getLon());
						last.setOnMileage(demoInfo.getMileage());
						sessionFactory.getCurrentSession().update(last);
					}
					demoInfo.setDetachAlarmed(false);
				}

				if (demoInfo.isVinChangeAlarmed() && car.getVin() != null && demoInfo.getVin() != null
						&& car.getVin().equals(demoInfo.getVin())) {
					AlarmVinChanged last = getLastAlarmVinChanged(message.getTerminalId());
					if (last != null) {
						last.setOnDate(new Date());
						last.setOnLat(demoInfo.getLat());
						last.setOnLon(demoInfo.getLon());
						last.setOnMileage(demoInfo.getMileage());
						sessionFactory.getCurrentSession().update(last);
					}
					demoInfo.setVinChangeAlarmed(false);
				}

			} else if (message instanceof Ch2Response) {
				Ch2Response mr = (Ch2Response) message;
				if (mr.getResponseType().equals("88")) {
					if (!mr.getResponse().startsWith("88 00 00 00 00 00 00 00 00 00")) {
						String vin = mr.getResponse().substring(4);
						demoInfo.setVin(vin);
					}
				} else if (mr.getResponseType().equals("296")) {
					String vin = mr.getResponse().substring(4);
					demoInfo.setVin(vin);
				}
			}
		}
	}

	public Ch2TerminalSession getTerminalSession(long terminalId) {
		synchronized (sessionMap) {
			Ch2TerminalSession session = sessionMap.get(terminalId);
			sessionMap.notifyAll();
			return session;
		}
	}

	public void putTerminalSession(long terminalId, ChannelHandlerContext ctx) {
		synchronized (sessionMap) {
			Ch2TerminalSession terminalSession = sessionMap.get(terminalId);
			if (terminalSession == null) {
				terminalSession = new Ch2TerminalSession(ctx);
				sessionMap.put(terminalId, terminalSession);
			}
			terminalSession.setSession(ctx);
			sessionMap.notifyAll();
		}
	}

	public Ch2DemoInfo getDemoInfo(Long imei) {
		return demoInfoMap.get(imei);
	}

	@Transactional
	@Scheduled(cron = "0 0/5 * * * *")
	public void demo() {
		logger.info("Start scheduled demo processing...");

		for (Long imei : sessionMap.keySet()) {
			Ch2DemoInfo demoInfo = demoInfoMap.get(imei);

			if (demoInfo != null) {
				checkVin(imei, demoInfo);
			} else {
				demoInfo = new Ch2DemoInfo();
				demoInfo.setLastDateCoord(new Date());
				demoInfoMap.put(imei, demoInfo);
			}

			sendCommandGetVin(imei, demoInfo);
		}
	}

	private void checkVin(Long imei, Ch2DemoInfo demoInfo) {
		// 860719028553836,CMD-Z,88 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		// check detach

		Car car = getCar(imei);
		if (car == null) {
			logger.info(imei.toString() + ", device not found in scoring.");
			return;
		}

		Date now = new Date();
		long nowMs = now.getTime() - (5 * 60 * 1000);

		if (nowMs > demoInfo.getLastDateCoord().getTime()) {
			if (!demoInfo.isDetachAlarmed()) {
				logger.info(imei.toString() + ", device detached.");
				makeAlarmDetach(imei, car.getId(), demoInfo);
				demoInfo.setDetachAlarmed(true);
			}
			return;
		}

		if (demoInfo.getVin() != null && !demoInfo.getVin().isEmpty()) {
			logger.info(imei.toString() + ", device have vin.");
			if (car.getVin() == null || car.getVin().isEmpty()) {
				car.setVin(demoInfo.getVin());
				sessionFactory.getCurrentSession().save(car);
				return;
			}

			if (car.getVin() != null && !car.getVin().equals(demoInfo.getVin())) {
				if (!demoInfo.isVinChangeAlarmed()) {
					logger.info(imei.toString() + ", vin changed.");
					makeAlarmVinChange(imei, demoInfo.getVin(), car.getVin(), car.getId(), demoInfo);
					return;
				}
			}
		}
	}

	private void makeAlarmDetach(Long imei, Long carId, Ch2DemoInfo demoInfo) {
		AlarmDeviceDetached alarm = new AlarmDeviceDetached();
		alarm.setTerminalId(imei);
		alarm.setCarId(carId);

		alarm.setOffDate(new Date());
		alarm.setOffLat(demoInfo.getLat());
		alarm.setOffLon(demoInfo.getLon());
		alarm.setOffMileage(demoInfo.getMileage());

		sessionFactory.getCurrentSession().save(alarm);
	}

	private void makeAlarmVinChange(Long imei, String vinNew, String vinOld, Long carId, Ch2DemoInfo demoInfo) {
		AlarmVinChanged alarm = new AlarmVinChanged();
		alarm.setVinNew(vinNew);
		alarm.setVinOld(vinOld);
		alarm.setCarId(carId);
		alarm.setImei(imei);

		alarm.setOffDate(new Date());
		alarm.setOffLat(demoInfo.getLat());
		alarm.setOffLon(demoInfo.getLon());
		alarm.setOffMileage(demoInfo.getMileage());

		sessionFactory.getCurrentSession().saveOrUpdate(alarm);
	}

	private void sendCommandGetVin(Long imei, Ch2DemoInfo demoInfo) {
		Ch2TerminalSession terminalSession = getTerminalSession(imei);
		if (terminalSession != null) {

			boolean ver = demoInfo.changeVersion();

			if (ver) {
				WialonMessage wm = new WialonMessage();
				wm.setTerminalId(imei);
				wm.setStrMessage(commandGetVin);
				terminalSession.write(wm);
				logger.info("Sended command get VIN to terminal: " + commandGetVin + " " + imei.toString());

			} else {
				// send obsolete command
				WialonMessage wm2 = new WialonMessage();
				wm2.setTerminalId(imei);
				wm2.setStrMessage(commandObsoleteGetVin);
				terminalSession.write(wm2);
				logger.info("Sended command get VIN to terminal: " + commandObsoleteGetVin + " " + imei.toString());
			}
		}
	}

	private Car getCar(long imei) {
		Session session = sessionFactory.getCurrentSession();
		return (Car) session.createCriteria(Car.class)
				.add(Restrictions.eq("imei", imei))
				.uniqueResult();
	}

	private AlarmDeviceDetached getLastAlarmDeviceDetached(long imei) {
		Session session = sessionFactory.getCurrentSession();

		return (AlarmDeviceDetached) session.createCriteria(AlarmDeviceDetached.class)
				.add(Restrictions.eq("terminalId", imei))
				.addOrder(Order.desc("offDate"))
				.setMaxResults(1)
				.uniqueResult();
	}

	private AlarmVinChanged getLastAlarmVinChanged(long imei) {
		Session session = sessionFactory.getCurrentSession();

		return (AlarmVinChanged) session.createCriteria(AlarmVinChanged.class)
				.add(Restrictions.eq("imei", imei))
				.addOrder(Order.desc("offDate"))
				.setMaxResults(1)
				.uniqueResult();
	}

}
