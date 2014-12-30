package monitoring.service.processing;

import java.util.Calendar;
import java.util.Date;

import monitoring.terminal.munic.controller.domain.MunicRawData;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MunicRawDataProcessing municRawDataProcessing;

	@Transactional
	public void processRawData(String rawData) {
		Session session = sessionFactory.getCurrentSession();

		MunicRawData municRawData = new MunicRawData();
		municRawData.setRawData(rawData);
		municRawData.setProcessed(false);
		municRawData.setArrived(getUTC(new Date()));
		session.save(municRawData);

		municRawDataProcessing.add(municRawData);
	}
	
	private Date getUTC(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.ZONE_OFFSET)*(-1));
		return calendar.getTime();
	}
}
