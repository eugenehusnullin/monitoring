package monitoring.protocol.nis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import monitoring.handler.Handler;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import utils.XmlUtils;

public class NisController extends MultiActionController {
	private static final Logger logger = LoggerFactory.getLogger(NisController.class);

	private Handler handler;
	private String putCoordResponse;

	public NisController() {
		try {
			URL url = getClass().getResource("/monitoring/protocol/nis/PutCoordResponse");
			File file = new File(url.getFile());
			FileInputStream fis = new FileInputStream(file);
			putCoordResponse = IOUtils.toString(fis);
		} catch (IOException e) {
			logger.error("Constructor error.", e);
		}
	}

	public void rawdata(HttpServletRequest request, HttpServletResponse response) {
		try {
			String envelope = IOUtils.toString(request.getInputStream());
			logger.debug(envelope);

			NisMessage m = createMessage(envelope);
			handler.handle(m, null);

			String responseMessage = createResponse(putCoordResponse, Long.toString(m.getTerminalId()));
			IOUtils.write(responseMessage, response.getOutputStream(), "UTF-8");
			response.setStatus(200);
		} catch (Exception e) {
			response.setStatus(500);
			logger.error(e.toString());
			e.printStackTrace();
		}
	}

	private String createResponse(String response, String objectId) {
		return response.replace("{0}", objectId);
	}

	private NisMessage createMessage(String envelope) {
		try {
			Document doc = XmlUtils.buildDomDocument(envelope);
			doc.getDocumentElement().normalize();

			String objectId = XmlUtils.getElementContent(doc.getDocumentElement(), "ObjectID");
			Element coord = XmlUtils.getOneElement(doc.getDocumentElement(), "Coord");
			String time = coord.getAttribute("time");
			String lon = coord.getAttribute("lon");
			String lat = coord.getAttribute("lat");
			String speed = coord.getAttribute("speed");

			NisMessage m = new NisMessage();
			m.setTerminalId(Long.parseLong(objectId));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			m.setTime(df.parse(time));
			m.setLat(Double.parseDouble(lat));
			m.setLon(Double.parseDouble(lon));
			m.setSpeed(Double.parseDouble(speed));

			return m;
		} catch (ParserConfigurationException | SAXException | IOException | ParseException e) {
			logger.error("Parse Nis message error. " + envelope, e);
		}
		return null;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
