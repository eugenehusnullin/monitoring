package ru.gm.munic.service.processing;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gm.munic.domain.MunicItemRawData;
import ru.gm.munic.domain.MunicRawData;

@Service
public class ProcessFailedItems {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MunicRawDataProcessing municRawDataProcessing;

	@Transactional
	public void processMunicRawData(Date from, Date to, boolean onlyFailed) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MunicRawData.class);

		if (onlyFailed) {
			criteria = criteria.add(Restrictions.eq("processed", false));
		}

		if (from != null) {
			criteria = criteria.add(Restrictions.ge("arrived", from));
		}

		if (to != null) {
			criteria = criteria.add(Restrictions.le("arrived", to));
		}

		@SuppressWarnings("unchecked")
		List<MunicRawData> listMunicRawDatas = criteria.addOrder(Order.asc("arrived")).list();

		@SuppressWarnings("unchecked")
		List<MunicItemRawData> listMunicItemRawDatas = sessionFactory.getCurrentSession()
				.createCriteria(MunicItemRawData.class).add(Restrictions.in("municRawData", listMunicRawDatas)).list();

		for (MunicItemRawData municItemRawData : listMunicItemRawDatas) {
			sessionFactory.getCurrentSession().delete(municItemRawData);
		}

		municRawDataProcessing.addList(listMunicRawDatas);
	}
}
