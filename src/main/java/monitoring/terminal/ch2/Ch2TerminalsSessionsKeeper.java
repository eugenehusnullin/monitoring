package monitoring.terminal.ch2;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import io.netty.channel.ChannelHandlerContext;
import monitoring.domain.AlarmDeviceDetached;
import monitoring.domain.AlarmVinChanged;
import monitoring.domain.Car;
import monitoring.handler.wialon.WialonMessage;

@EnableScheduling
public class Ch2TerminalsSessionsKeeper {
	private static final Logger logger = LoggerFactory.getLogger(Ch2TerminalsSessionsKeeper.class);
	private Map<Long, Ch2TerminalSession> sessionMap = new HashMap<Long, Ch2TerminalSession>();

	@Autowired
	private SessionFactory sessionFactory;

	@Value("#{mainSettings['demo.imeis'].split(',')}")
	private List<Long> demoImeis;
	private Map<Long, Ch2DemoInfo> demoInfoMap = new HashMap<>();
	private String commandGetVin = ":123456Z08#";

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

		for (Long imei : demoImeis) {
			Ch2DemoInfo demoInfo = demoInfoMap.get(imei);

			if (demoInfo != null) {
				checkVin(imei, demoInfo);
			} else {
				demoInfo = new Ch2DemoInfo();
				demoInfo.setLastDateCoord(new Date());
				demoInfoMap.put(imei, demoInfo);
			}

			senCommandGetVin(imei);
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
				makeAlarmDetach(imei, car.getId());
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
					makeAlarmVinChange(imei, demoInfo.getVin(), car.getVin(), car.getId());
					return;
				}
			}
		}
	}

	private void makeAlarmDetach(Long imei, Long carId) {
		AlarmDeviceDetached alarm = new AlarmDeviceDetached();
		alarm.setTerminalId(imei);
		alarm.setCarId(carId);
		alarm.setEventDate(new Date());
		sessionFactory.getCurrentSession().save(alarm);
	}

	private void makeAlarmVinChange(Long imei, String vinNew, String vinOld, Long carId) {
		AlarmVinChanged alarm = new AlarmVinChanged();
		alarm.setVinNew(vinNew);
		alarm.setVinOld(vinOld);
		alarm.setAlarmDate(new Date());
		alarm.setCarId(carId);
		alarm.setImei(imei);
		sessionFactory.getCurrentSession().saveOrUpdate(alarm);
	}

	private void senCommandGetVin(Long imei) {
		Ch2TerminalSession terminalSession = getTerminalSession(imei);
		if (terminalSession != null) {
			WialonMessage wm = new WialonMessage();
			wm.setTerminalId(imei);
			wm.setStrMessage(commandGetVin);
			terminalSession.write(wm);

			logger.info("Sended command get VIN to terminal: " + imei.toString());
		}
	}

	private Car getCar(long imei) {
		Session session = sessionFactory.getCurrentSession();
		return (Car) session.createCriteria(Car.class).add(Restrictions.eq("imei", imei)).uniqueResult();
	}

}
