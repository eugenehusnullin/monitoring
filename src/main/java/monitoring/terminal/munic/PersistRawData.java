package monitoring.terminal.munic;

import java.io.StringWriter;
import java.util.Date;

import monitoring.terminal.munic.domain.MunicRawData;
import monitoring.utils.DateUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

class PersistRawData implements RawHandler {
	private SessionFactory sessionFactory;

	@Transactional
	@Override
	public void procces(StringWriter writer) {
		MunicRawData municRawData = new MunicRawData();
		municRawData.setRawData(writer.toString());
		municRawData.setArrived(DateUtils.getUTC(new Date()));

		Session session = sessionFactory.getCurrentSession();
		session.save(municRawData);

	}

}
