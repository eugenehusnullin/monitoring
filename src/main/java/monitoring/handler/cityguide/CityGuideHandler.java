package monitoring.handler.cityguide;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import monitoring.domain.Message;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;
import monitoring.protocol.nis.NisMessage;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.HttpUtils;

public class CityGuideHandler implements Handler {
	private static final Logger logger = LoggerFactory.getLogger(CityGuideHandler.class);

	private String url;

	@Override
	public void handle(Message message, HandlerStrategy strategy) {
		NisMessage m = (NisMessage) message;

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element pointsElement = doc.createElement("points");
			pointsElement.setAttribute("id", Long.toString(m.getTerminalId()));
			doc.appendChild(pointsElement);

			Element pointElement = doc.createElement("points");
			pointElement.setAttribute("speed", Double.toString(m.getSpeed()));
			pointElement.setAttribute("lat", Double.toString(m.getLat()));
			pointElement.setAttribute("lon", Double.toString(m.getLon()));
			pointElement.setAttribute("isotime", parseToIsoTime(m.getTime()));
			pointsElement.appendChild(pointElement);

			HttpURLConnection con = HttpUtils.postDocumentOverHttp(doc, url, logger);
			if (con.getResponseCode() == 200) {
				logger.debug("CityGuideHandler success send point.");
			} else {
				String reason = IOUtils.toString(con.getInputStream());
				logger.warn("CityGuideHandler error send point. Error code=" + con.getResponseCode() + ", reason: "
						+ reason);
			}
		} catch (ParserConfigurationException | IOException | TransformerException
				| TransformerFactoryConfigurationError e) {
			logger.error("CityGuideHandler error.", e);
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private String parseToIsoTime(Date time) {
		// 2014-10-23T11:01:57+0300
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		return df.format(time);
	}
}
