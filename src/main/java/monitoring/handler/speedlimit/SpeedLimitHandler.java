package monitoring.handler.speedlimit;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Formatter;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import monitoring.domain.Message;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;
import monitoring.handler.PositionConverter;
import monitoring.handler.position.Position;

public class SpeedLimitHandler implements Handler {
	private static final Logger logger = LoggerFactory.getLogger(SpeedLimitHandler.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	@Override
	public void handle(Message message, HandlerStrategy strategy) {
		try {

			PositionConverter positionConverter = strategy.getPositionConverter();
			if (positionConverter != null) {
				Position position = positionConverter.convert(message);

				if (position != null && position.getSpeed() != null && position.getSpeed() > 40) {
					// check speed limit by here.com
					Integer speed = getSpeedLimit(position.getLat(), position.getLon());
					if (speed != null && speed + 10 < position.getSpeed()) {
						SpeedLimit speedLimit = new SpeedLimit();
						speedLimit.setDate(position.getDate());
						speedLimit.setTerminalId(position.getTerminalId());
						speedLimit.setSpeed(position.getSpeed().intValue());
						speedLimit.setLimit(speed);
						
						Session session = sessionFactory.getCurrentSession();
						session.save(speedLimit);
					}
				}
			}

		} catch (Exception e) {
			logger.error("GyroHandler exception: ", e);
		}
	}

	protected Integer getSpeedLimit(Double lat, Double lon) {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);

		sb.append("http://legacy.route.cit.api.here.com/routing/6.2/getlinkinfo.json");
		sb.append('?');
		sb.append("app_id=g54Qc1ApYhkO2HpAwTEZ");
		sb.append('&');
		sb.append("app_code=TkrxmdbiVk05WxNZrPE3pA");
		sb.append('&');
		formatter.format("waypoint=%.6f,%.6f", lat, lon);
		sb.append('&');
		sb.append("linkattributes=speedCategory");
		formatter.close();

		try {
			InputStream is = makeGetRequest(sb.toString());
			String str = IOUtils.toString(is, "UTF-8");
			JSONObject linkInfo = (JSONObject) new JSONTokener(str).nextValue();
			JSONObject response = linkInfo.getJSONObject("Response");
			JSONArray arr = response.getJSONArray("Link");
			JSONObject linkItem = (JSONObject) arr.getJSONObject(0);
			double speedLimit = linkItem.optDouble("SpeedLimit");
			if (Double.isNaN(speedLimit)) {
				logger.debug("Not exists SpeedLimit value.");
				return null;
			} else {
				// convert to km/h from m/s
				return (int) (speedLimit * 3.6);
			}
		} catch (Exception e) {
			logger.error("here.com error.", e);
		}
		return null;

	}

	private InputStream makeGetRequest(String address) throws IOException {
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// conn.setRequestProperty("Accept", contentType);

		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;
	}

}
