package ru.gm.munic.service.sender;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.postgresql.util.Base64;

import ru.gm.munic.domain.Message;

public class Client {

	private int port;
	private String host;
	private Message message;

	public Client(String host, int port, Message message) {
		this.host = host;
		this.port = port;
		this.message = message;
	}

	public void send() {
		String messageString = getMessageString(message);
		if (messageString == null) {
			return;
		}

		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(3000);

		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MunicCodecFactory()));
		connector.setHandler(new ClientSessionHandler(messageString));

		IoSession session;
		ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
		future.awaitUninterruptibly();
		session = future.getSession();

		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}

	public String getMessageString(Message message) {
		JSONTokener jsonTokener = new JSONTokener(message.getValue());
		JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();

		JSONObject meta = jsonObject.getJSONObject("meta");
		String event = meta.getString("event");

		if (event.equals("track")) {
			JSONObject payload = jsonObject.getJSONObject("payload");
			String asset = payload.getString("asset");
			String recorded_at = payload.getString("recorded_at").replace("Z", ""); // 2014-03-26T11:42:01Z
			JSONArray loc = payload.optJSONArray("loc");

			Integer gpsDir = null;
			Integer gpsSpeed = null;
			Integer zBegin = null;
			Integer zEnd = null;
			Integer zPeak = null;
			Integer yBegin = null;
			Integer yEnd = null;
			Integer yPeak = null;
			Integer xBegin = null;
			Integer xEnd = null;
			Integer xPeak = null;
			JSONObject fields = payload.optJSONObject("fields");
			if (fields != null) {
				JSONObject gpsDirJsonObject = fields.optJSONObject("GPS_DIR");
				if (gpsDirJsonObject != null) {
					gpsDir = decodeBase64Integer(gpsDirJsonObject.getString("b64_value"));
				}

				JSONObject gpsSpeedJsonObject = fields.optJSONObject("GPS_SPEED");
				if (gpsSpeedJsonObject != null) {
					gpsSpeed = decodeBase64Integer(gpsSpeedJsonObject.getString("b64_value"));
				}

				JSONObject xBeginJsonObject = fields.optJSONObject("BEHAVE_ACC_X_BEGIN");
				if (xBeginJsonObject != null) {
					xBegin = decodeBase64Integer(xBeginJsonObject.getString("b64_value"));
				}

				JSONObject xEndJsonObject = fields.optJSONObject("BEHAVE_ACC_X_END");
				if (xEndJsonObject != null) {
					xEnd = decodeBase64Integer(xEndJsonObject.getString("b64_value"));
				}

				JSONObject xPeakJsonObject = fields.optJSONObject("BEHAVE_ACC_X_PEAK");
				if (xPeakJsonObject != null) {
					xPeak = decodeBase64Integer(xPeakJsonObject.getString("b64_value"));
				}

				JSONObject yBeginJsonObject = fields.optJSONObject("BEHAVE_ACC_Y_BEGIN");
				if (yBeginJsonObject != null) {
					yBegin = decodeBase64Integer(yBeginJsonObject.getString("b64_value"));
				}

				JSONObject yEndJsonObject = fields.optJSONObject("BEHAVE_ACC_Y_END");
				if (yEndJsonObject != null) {
					yEnd = decodeBase64Integer(yEndJsonObject.getString("b64_value"));
				}

				JSONObject yPeakJsonObject = fields.optJSONObject("BEHAVE_ACC_Y_PEAK");
				if (yPeakJsonObject != null) {
					yPeak = decodeBase64Integer(yPeakJsonObject.getString("b64_value"));
				}

				JSONObject zBeginJsonObject = fields.optJSONObject("BEHAVE_ACC_Z_BEGIN");
				if (zBeginJsonObject != null) {
					zBegin = decodeBase64Integer(zBeginJsonObject.getString("b64_value"));
				}

				JSONObject zEndJsonObject = fields.optJSONObject("BEHAVE_ACC_Z_END");
				if (zEndJsonObject != null) {
					zEnd = decodeBase64Integer(zEndJsonObject.getString("b64_value"));
				}

				JSONObject zPeakJsonObject = fields.optJSONObject("BEHAVE_ACC_Z_PEAK");
				if (zPeakJsonObject != null) {
					zPeak = decodeBase64Integer(zPeakJsonObject.getString("b64_value"));
				}

			}

			StringBuilder sb = new StringBuilder();
			sb.append("|asset=");
			sb.append(asset);
			sb.append("|recorded_at=");
			sb.append(recorded_at);
			if (loc != null) {
				double lat = loc.getDouble(0);
				double lon = loc.getDouble(1);

				sb.append("|lat=");
				sb.append(lat);
				sb.append("|lon=");
				sb.append(lon);
			}
			if (gpsDir != null) {
				sb.append("|GPS_DIR=");
				sb.append(gpsDir);
			}
			if (gpsSpeed != null) {
				sb.append(gpsSpeed);
			}
			if (xBegin != null) {
				sb.append("|xbegin=");
				sb.append(xBegin);
			}
			if (xEnd != null) {
				sb.append("|xend=");
				sb.append(xEnd);
			}
			if (xPeak != null) {
				sb.append("|xpeak=");
				sb.append(xPeak);
			}
			if (yBegin != null) {
				sb.append("|ybegin=");
				sb.append(yBegin);
			}
			if (yEnd != null) {
				sb.append("|yend=");
				sb.append(yEnd);
			}
			if (yPeak != null) {
				sb.append("|ypeak=");
				sb.append(yPeak);
			}
			if (zBegin != null) {
				sb.append("|zbegin=");
				sb.append(zBegin);
			}
			if (zEnd != null) {
				sb.append("|zend=");
				sb.append(zEnd);
			}
			if (zPeak != null) {
				sb.append("|zpeak=");
				sb.append(zPeak);
			}

			return sb.toString();
		}
		return null;
	}

	private int decodeBase64Integer(String value) {
		byte[] bytes = Base64.decode(value);
		int result = 0;
		for (int i = 0; i < bytes.length; i++) {
			result <<= 8;
			result += bytes[i];
		}
		return result;
	}
}
