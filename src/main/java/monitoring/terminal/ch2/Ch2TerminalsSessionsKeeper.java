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
import monitoring.domain.AlarmDisconnected;
import monitoring.domain.AlarmVinChanged;
import monitoring.domain.Car;
import monitoring.domain.Message;
import monitoring.handler.wialon.WialonMessage;

@EnableScheduling
public class Ch2TerminalsSessionsKeeper {
	private static final Logger logger = LoggerFactory.getLogger(Ch2TerminalsSessionsKeeper.class);
	private Map<Long, Ch2DemoInfo> demoInfosMap = new HashMap<Long, Ch2DemoInfo>();

	@Autowired
	private SessionFactory sessionFactory;

	private String commandGetVin = ":123456Z08#";
	private String commandObsoleteGetVin = ":123456Z296#";
	private String commandGetMileage = ":123456Z05#";

	@Transactional
	public void messageArrived(Message message, ChannelHandlerContext ctx) {
		Long imei = message.getTerminalId();
		Car car = getCar(imei);
		if (car == null) {
			logger.info(imei + ", device not found in scoring.");
			return;
		}

		boolean needCheckVIN = false;

		// initDate
		if (car.getInitDate() == null) {
			car.setInitDate(new Date());
			sessionFactory.getCurrentSession().save(car);
		}

		Ch2DemoInfo demoInfo = getDemoInfo(imei);
		if (demoInfo == null) {
			demoInfo = new Ch2DemoInfo();
			demoInfosMap.put(imei, demoInfo);
		}
		putTerminalSession(demoInfo, ctx);
		demoInfo.setDisconnectedDate(null);

		if (message instanceof Ch2Message) {
			Ch2Message mm = (Ch2Message) message;

			if (mm.getLongitude() == null || mm.getLatitude() == null) {
				return;
			}

			demoInfo.setLastDateCoord(new Date());
			demoInfo.setLat(mm.getLatitude());
			demoInfo.setLon(mm.getLongitude());

			if (mm.getVin() != null) {
				demoInfo.setVin(mm.getVin());
				needCheckVIN = true;
				logger.info(imei.toString() + ", device have vin.");
			} else if (demoInfo.getVin() != null) {
				mm.setVin(demoInfo.getVin());
			}

			// detach
			if (!mm.getPluged() && !demoInfo.isDetached()) {
				logger.info("===DETACH: " + imei);
				AlarmDeviceDetached alarm = new AlarmDeviceDetached();
				alarm.setTerminalId(imei);
				alarm.setCarId(car.getId());

				alarm.setOffDate(new Date());
				alarm.setOffLat(demoInfo.getLat());
				alarm.setOffLon(demoInfo.getLon());
				alarm.setOffMileage(demoInfo.getMileage());

				sessionFactory.getCurrentSession().save(alarm);

				demoInfo.setDetached(true);
			}

			// tach
			if (mm.getPluged() && demoInfo.isDetached()) {
				logger.info("===TACH: " + imei);
				AlarmDeviceDetached last = getLastAlarmDeviceDetached(imei);
				if (last != null) {
					last.setOnDate(new Date());
					last.setOnLat(demoInfo.getLat());
					last.setOnLon(demoInfo.getLon());
					last.setOnMileage(demoInfo.getMileage());
					sessionFactory.getCurrentSession().update(last);
				}

				demoInfo.setDetached(false);
			}

			// connected
			if (demoInfo.isDisconnected()) {
				logger.info("===CONNECTED: " + imei);
				AlarmDisconnected last = getLastAlarmDisconnected(imei);
				if (last != null) {
					last.setOnDate(new Date());
					last.setOnLat(demoInfo.getLat());
					last.setOnLon(demoInfo.getLon());
					last.setOnMileage(demoInfo.getMileage());
					sessionFactory.getCurrentSession().update(last);
				}

				demoInfo.setDisconnected(false);
			}

			// sendCommandGetMileage(mm.getTerminalId(), demoInfo);
			// sendCommandGetVin(mm.getTerminalId(), demoInfo);

		} else if (message instanceof Ch2Response) {
			Ch2Response mr = (Ch2Response) message;
			if (mr.getResponseType().equals("88")) {
				if (!mr.getResponse().startsWith("88 00 00 00 00 00 00 00 00 00")) {
					String vin = mr.getResponse().substring(4);
					demoInfo.setVin(vin);
					needCheckVIN = true;
					logger.info(imei.toString() + ", device have vin.");
				}
			} else if (mr.getResponseType().equals("296")) {
				String vin = mr.getResponse().substring(4);
				demoInfo.setVin(vin);
				needCheckVIN = true;
				logger.info(imei.toString() + ", device have vin.");

			} else if (mr.getResponseType().equals("85")) {
				String odom = mr.getResponse().substring(5);
				demoInfo.setMileage(Long.parseLong(odom));
			}
		}

		// CHECK VIN
		if (needCheckVIN) {

			if (demoInfo.getVin() != null && !demoInfo.getVin().isEmpty()) {

				if (car.getVin() == null || car.getVin().isEmpty()) {
					car.setVin(demoInfo.getVin());
					sessionFactory.getCurrentSession().save(car);
				}

				else if (!demoInfo.isVinChanged() && !car.getVin().equals(demoInfo.getVin())) {
					logger.info("===VIN_CHANGED: " + imei.toString());
					makeAlarmVinChange(imei, demoInfo.getVin(), car.getVin(), car.getId(), demoInfo);
					demoInfo.setVinChanged(true);
				}

				else if (demoInfo.isVinChanged() && car.getVin().equals(demoInfo.getVin())) {
					logger.info("===VIN_RETURN: " + imei);
					AlarmVinChanged last = getLastAlarmVinChanged(imei);
					if (last != null) {
						last.setOnDate(new Date());
						last.setOnLat(demoInfo.getLat());
						last.setOnLon(demoInfo.getLon());
						last.setOnMileage(demoInfo.getMileage());
						sessionFactory.getCurrentSession().update(last);
					}
					demoInfo.setVinChanged(false);
				}
			}

		}
	}

	public Ch2TerminalSession getTerminalSession(long terminalId) {
		synchronized (demoInfosMap) {
			Ch2DemoInfo demoInfo = demoInfosMap.get(terminalId);
			return demoInfo.getSession();
		}
	}

	public void putTerminalSession(Ch2DemoInfo demoInfo, ChannelHandlerContext ctx) {
		if (demoInfo.getSession() == null) {
			demoInfo.setSession(new Ch2TerminalSession(ctx));
		} else {
			demoInfo.getSession().setSession(ctx);
		}
	}

	public Ch2DemoInfo getDemoInfo(Long imei) {
		return demoInfosMap.get(imei);
	}

	public void deviceDisconnected(Long terminalId) {
		Ch2DemoInfo demoInfo = demoInfosMap.get(terminalId);
		if (demoInfo != null) {
			demoInfo.setDisconnectedDate(new Date());
		}
	}

	@Transactional
	@Scheduled(cron = "0 0/1 * * * *")
	public void demo() {
		for (Map.Entry<Long, Ch2DemoInfo> entry : demoInfosMap.entrySet()) {
			checkNeedAlarmDisconnect(entry.getKey(), entry.getValue());
		}
	}

	private void checkNeedAlarmDisconnect(Long imei, Ch2DemoInfo demoInfo) {
		Date now = new Date();
		long nowMs = now.getTime() - (5 * 60 * 1000);

		if (demoInfo.getDisconnectedDate() != null && nowMs > demoInfo.getDisconnectedDate().getTime()) {
			if (!demoInfo.isDisconnected()) {
				Car car = getCar(imei);
				if (car == null) {
					logger.info(imei.toString() + ", device not found in scoring.");
					return;
				}

				logger.info("===DISCONNECT: " + imei.toString());
				makeAlarmDisconnected(imei, car.getId(), demoInfo);
				demoInfo.setDisconnected(true);
			}
		}
	}

	private void makeAlarmDisconnected(Long imei, Long carId, Ch2DemoInfo demoInfo) {
		AlarmDisconnected alarm = new AlarmDisconnected();
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

	@SuppressWarnings("unused")
	private void sendCommandGetVin(Long imei, Ch2DemoInfo demoInfo) {
		Ch2TerminalSession terminalSession = demoInfo.getSession();
		if (demoInfo.needVin()) {
			if (demoInfo.changeVinVersion()) {
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

	@SuppressWarnings("unused")
	private void sendCommandGetMileage(Long imei, Ch2DemoInfo demoInfo) {
		Ch2TerminalSession terminalSession = demoInfo.getSession();
		if (demoInfo.needMileage()) {
			WialonMessage wm = new WialonMessage();
			wm.setTerminalId(imei);
			wm.setStrMessage(commandGetMileage);
			terminalSession.write(wm);
			logger.info("Sended command get Mileage to terminal: " + commandGetMileage + " " + imei.toString());
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

	private AlarmDisconnected getLastAlarmDisconnected(long terminalId) {
		Session session = sessionFactory.getCurrentSession();

		return (AlarmDisconnected) session.createCriteria(AlarmDisconnected.class)
				.add(Restrictions.eq("terminalId", terminalId))
				.addOrder(Order.desc("offDate"))
				.setMaxResults(1)
				.uniqueResult();
	}
}
