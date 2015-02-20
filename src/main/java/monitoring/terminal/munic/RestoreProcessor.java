package monitoring.terminal.munic;

import java.util.Date;
import java.util.List;

import monitoring.terminal.munic.domain.MunicRawData;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

public class RestoreProcessor {
	private SessionFactory sessionFactory;
	private List<RawHandler> rawHandlers;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setRawHandlers(List<RawHandler> rawHandlers) {
		this.rawHandlers = rawHandlers;
	}

	@Transactional
	public void restore(Date date1, Date date2) {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<MunicRawData> list = session.createCriteria(MunicRawData.class)
			.add(Restrictions.ge("arrived", date1))
			.add(Restrictions.lt("arrived", date2))
			.list();
		
		for (MunicRawData municRawData : list) {
			for (RawHandler handler : rawHandlers) {
				handler.procces(municRawData.getRawData());
			}
		}
	}

}
