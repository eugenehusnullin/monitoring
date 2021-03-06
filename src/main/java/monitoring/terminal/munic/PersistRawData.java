package monitoring.terminal.munic;

import java.util.Date;

import monitoring.terminal.munic.domain.MunicRawData;
import monitoring.utils.DateUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

public class PersistRawData implements RawHandler {
	private SessionFactory sessionFactory;

	@Transactional
	@Override
	public void procces(String message) {
		MunicRawData municRawData = new MunicRawData();
		municRawData.setRawData(message);
		municRawData.setArrived(DateUtils.localTimeToOtherTimeZone(new Date(), DateUtils.TIMEZONEID_UTC));

		Session session = sessionFactory.getCurrentSession();
		session.save(municRawData);

	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
