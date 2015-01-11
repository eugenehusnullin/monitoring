package monitoring.terminal.munic.trash;

import java.util.Date;

import monitoring.domain.TerminalDetach;
import monitoring.domain.TopAuto;
import monitoring.domain.TopBlockAlert;
import monitoring.terminal.munic.domain.MunicData;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LowService {

	private SessionFactory sessionFactory;
	
	@Transactional
	public void saveTerminalDetach(TerminalDetach terminalDetach) {
		Session session = sessionFactory.getCurrentSession();
		session.save(terminalDetach);
	}

	@Transactional
	public void saveMunicData(MunicData municData) {
		Session session = sessionFactory.getCurrentSession();
		TopAuto topAuto = (TopAuto) session.createCriteria(TopAuto.class)
				.add(Restrictions.eq("asset", municData.getAsset())).uniqueResult();

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
						jsonAlert.put("lat", municData.getLat());
						jsonAlert.put("lot", municData.getLon());
					}

					alert.setMessage(jsonAlert.toString());
					session.save(alert);
				}
			}

			// check distance
			// try {
			// if (municData.hasMileage()) {
			// if (municData.getMunicDataMdi().getMdiObdMileage() != 0) {
			// @SuppressWarnings("unchecked")
			// List<MunicData> prevList =
			// session.createCriteria(MunicData.class)
			// .add(Restrictions.eq("asset", municData.getAsset()))
			// .add(Restrictions.lt("recordedAt", municData.getRecordedAt()))
			// .addOrder(Order.desc("recordedAt"))
			// .setMaxResults(1)
			// .createCriteria("municDataMdi")
			// .add(Restrictions.isNotNull("mdiObdMileage"))
			// .list();
			//
			// if (prevList.size() == 0) {
			// logger.error("Unexpecting behavior while quering municdata for previvouse trip.");
			// } else {
			// MunicData prevMD = prevList.get(0);
			// Integer obdDistance =
			// municData.getMunicDataMdi().getMdiObdMileage() -
			// prevMD.getMunicDataMdi().getMdiObdMileage();
			//
			// // calculate distance by gps coordinates
			// @SuppressWarnings("unchecked")
			// List<MunicData> listOfGps =
			// session.createCriteria(MunicData.class)
			// .add(Restrictions.eq("asset", municData.getAsset()))
			// .add(Restrictions.le("recordedAt", municData.getRecordedAt()))
			// .add(Restrictions.gt("recordedAt", prevMD.getRecordedAt()))
			// .add(Restrictions.isNotNull("lat"))
			// .add(Restrictions.isNotNull("lon"))
			// .addOrder(Order.asc("recordedAt"))
			// .list();
			//
			// int gpsDistance = 0;
			// double prevLat = listOfGps.get(0).getLat();
			// double prevLon = listOfGps.get(0).getLon();
			// for (int i = 1; i < listOfGps.size(); i++) {
			// gpsDistance += calcDistance(prevLat, prevLon,
			// listOfGps.get(i).getLat(), listOfGps.get(i).getLon());
			// }
			//
			// if (gpsDistance != 0) {
			// double percent = Math.abs(obdDistance * 100 / gpsDistance);
			// if (percent > maxPercent) {
			// TopBlockAlert alert = new TopBlockAlert();
			// alert.setTopAuto(topAuto);
			// alert.setAddDate(new Date());
			// alert.setStatus(2);
			// alert.setAlertType(1);
			//
			// JSONObject jsonAlert = new JSONObject();
			// jsonAlert.put("percent", percent);
			// jsonAlert.put("gpsDistance", gpsDistance);
			// jsonAlert.put("obdDistance", obdDistance);
			// if (municData.getLat() != null && municData.getLon() != null) {
			// jsonAlert.put("lat",municData.getLat());
			// jsonAlert.put("lot",municData.getLon());
			// }
			//
			// alert.setMessage(jsonAlert.toString());
			// session.save(alert);
			// }
			// }
			// }
			//
			// }
			// }
			// }catch(Exception e) {
			// logger.error(e.toString());
			// }
		}

		session.save(municData);
	}
}
