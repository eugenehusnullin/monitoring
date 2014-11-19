package ru.gm.munic.service.processing;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gm.munic.domain.MunicData;
import ru.gm.munic.domain.MunicItemRawData;
import ru.gm.munic.domain.MunicRawData;
import ru.gm.munic.domain.TopAuto;

@Service
public class LowService {
	private static final Logger logger = LoggerFactory.getLogger(LowService.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<MunicItemRawData> processMunicRawData(MunicRawData municRawData) {

		JSONTokener jsonTokener = new JSONTokener(municRawData.getRawData());
		logger.trace(jsonTokener.toString());

		Session session = sessionFactory.getCurrentSession();
		List<MunicItemRawData> list = new ArrayList<MunicItemRawData>();
		JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			MunicItemRawData municItemRawData = new MunicItemRawData();
			municItemRawData.setItemRawData(jsonObject.toString());
			municItemRawData.setMunicRawData(municRawData);
			session.save(municItemRawData);

			list.add(municItemRawData);
		}

		municRawData.setProcessed(true);
		session.merge(municRawData);

		return list;
	}

	@Transactional
	public void setWialonSended(MunicItemRawData municItemRawData) {
		municItemRawData.setWialonSended(true);
		sessionFactory.getCurrentSession().update(municItemRawData);
	}

	@Transactional
	public void saveMunicData(MunicData municData) {
		Session s = sessionFactory.getCurrentSession();
		TopAuto topAuto = (TopAuto) s.createCriteria(TopAuto.class).add(
				Restrictions.eq("asset", municData.getAsset())
			).uniqueResult();
		if (topAuto != null) {
			municData.setTopAutoId(topAuto.getTopAutoId());
			
			if (municData.hasVIN()) {
				if (!municData.getMunicDataMdi().getMdiObdVin().equals(topAuto.getVIN())) {
					// TODO: reactions on change VIN code can place here
				}
			}
		}
		s.save(municData);

		municData.getMunicItemRawData().setProcessed(true);
		s.update(municData.getMunicItemRawData());
	}

}
