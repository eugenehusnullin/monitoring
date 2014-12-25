package monitoring.service.processing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import monitoring.domain.MunicData;
import monitoring.domain.MunicItemRawData;
import monitoring.domain.MunicRawData;
import monitoring.domain.Position;
import monitoring.domain.TopAuto;
import monitoring.domain.TopBlockAlert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LowService {
	private static final Logger logger = LoggerFactory.getLogger(LowService.class);

	@Value("#{mainSettings['block.distance.percent']}")
	private Integer maxPercent;

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void catchPosition(Position position) {
		Session session = sessionFactory.getCurrentSession();
		TopAuto topAuto = (TopAuto) session.createCriteria(TopAuto.class)
				.add(Restrictions.eq("asset", position.getTerminalId()))
				.uniqueResult();
		
		if (topAuto != null) {
			position.setTopAuto(topAuto);
		}
		
		session.save(position);
	}

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
		Session session = sessionFactory.getCurrentSession();
		TopAuto topAuto = (TopAuto) session.createCriteria(TopAuto.class).add(Restrictions.eq("asset", municData.getAsset()))
				.uniqueResult();

		if (topAuto != null) {
			// set topautoId to municdata
			municData.setTopAutoId(topAuto.getTopAutoId());

			// work with VIN code
			if (municData.hasVIN()) {
				if (topAuto.getVIN() == null) {
					topAuto.setVIN(municData.getMunicDataMdi().getMdiObdVin());
					session.update(topAuto);
				} else if (!municData.getMunicDataMdi().getMdiObdVin().equals(topAuto.getVIN())) {
					TopBlockAlert alert = new TopBlockAlert();
					alert.setTopAuto(topAuto);
					alert.setAddDate(new Date());
					alert.setStatus(1);
					alert.setAlertType(1);
					
					JSONObject jsonAlert = new JSONObject();
					jsonAlert.put("newVIN", municData.getMunicDataMdi().getMdiObdVin());
					jsonAlert.put("currentVIN", topAuto.getVIN());
					if (municData.getLat() != null && municData.getLon() != null) {
						jsonAlert.put("lat",municData.getLat());
						jsonAlert.put("lot",municData.getLon());
					}
					
					alert.setMessage(jsonAlert.toString());
					session.save(alert);
				}
			}

			// check distance
//			try {
//				if (municData.hasMileage()) {
//					if (municData.getMunicDataMdi().getMdiObdMileage() != 0) {
//						@SuppressWarnings("unchecked")
//						List<MunicData> prevList = session.createCriteria(MunicData.class)
//								.add(Restrictions.eq("asset", municData.getAsset()))
//								.add(Restrictions.lt("recordedAt", municData.getRecordedAt()))
//								.addOrder(Order.desc("recordedAt"))
//								.setMaxResults(1)
//								.createCriteria("municDataMdi")
//									.add(Restrictions.isNotNull("mdiObdMileage"))
//								.list();
//						
//						if (prevList.size() == 0) {
//							logger.error("Unexpecting behavior while quering municdata for previvouse trip.");
//						} else {
//							MunicData prevMD = prevList.get(0);
//							Integer obdDistance = municData.getMunicDataMdi().getMdiObdMileage() - prevMD.getMunicDataMdi().getMdiObdMileage();
//							
//							// calculate distance by gps coordinates
//							@SuppressWarnings("unchecked")
//							List<MunicData> listOfGps = session.createCriteria(MunicData.class)
//									.add(Restrictions.eq("asset", municData.getAsset()))
//									.add(Restrictions.le("recordedAt", municData.getRecordedAt()))
//									.add(Restrictions.gt("recordedAt", prevMD.getRecordedAt()))
//									.add(Restrictions.isNotNull("lat"))
//									.add(Restrictions.isNotNull("lon"))
//									.addOrder(Order.asc("recordedAt"))
//									.list();
//							
//							int gpsDistance = 0;
//							double prevLat = listOfGps.get(0).getLat();
//							double prevLon = listOfGps.get(0).getLon();
//							for (int i = 1; i < listOfGps.size(); i++) {
//								gpsDistance += calcDistance(prevLat, prevLon, listOfGps.get(i).getLat(), listOfGps.get(i).getLon());
//							}
//							
//							if (gpsDistance != 0) {
//								double percent = Math.abs(obdDistance * 100 / gpsDistance);
//								if (percent > maxPercent) {
//									TopBlockAlert alert = new TopBlockAlert();
//									alert.setTopAuto(topAuto);
//									alert.setAddDate(new Date());
//									alert.setStatus(2);
//									alert.setAlertType(1);
//									
//									JSONObject jsonAlert = new JSONObject();
//									jsonAlert.put("percent", percent);
//									jsonAlert.put("gpsDistance", gpsDistance);
//									jsonAlert.put("obdDistance", obdDistance);
//									if (municData.getLat() != null && municData.getLon() != null) {
//										jsonAlert.put("lat",municData.getLat());
//										jsonAlert.put("lot",municData.getLon());
//									}
//									
//									alert.setMessage(jsonAlert.toString());
//									session.save(alert);
//								}
//							}
//						}
//						
//					}
//				}
//			}catch(Exception e) {
//				logger.error(e.toString());
//			}
		}

		session.save(municData);

		municData.getMunicItemRawData().setProcessed(true);
		session.update(municData.getMunicItemRawData());
	}
	
	@SuppressWarnings("unused")
	private double calcDistance(double lat1, double lon1, double lat2, double lon2) {
		long radius = 6371L;

		double rlat1 = Math.toRadians(lat1);
		double rlon1 = Math.toRadians(lon1);
		double rlat2 = Math.toRadians(lat2);	
		double rlon2 = Math.toRadians(lon2);

		double lat_delta = rlat2 - rlat1;
		double lon_delta = rlon2 - rlon1;
		
		return radius * 2 * Math.asin(
				Math.sqrt(
						Math.pow(Math.sin(lat_delta / 2), 2)
						+ Math.cos(rlat1) * Math.cos(rlat2) * Math.pow(Math.sin(lon_delta / 2),2)
					)
				);
	}

}
