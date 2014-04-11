package ru.gm.munic.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.gm.munic.service.processing.MessageService;
import ru.gm.munic.service.processing.ProcessFailedItems;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private MessageService messageService;
	@Autowired
	private ProcessFailedItems processFailedItems;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@RequestMapping(value = "/rawdata", method = RequestMethod.POST)
	public void rawdata(HttpServletRequest request, HttpServletResponse response) {
		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(request.getInputStream(), writer);
			messageService.processRawData(writer.toString());

			response.setStatus(200);
		} catch (IOException e) {
			response.setStatus(500);
		}
	}

	// private Message createMessage(JSONObject jsonObject) throws
	// ParseException {
	// Message message = new Message();
	// message.setValue(jsonObject.toString());
	//
	// JSONObject payload = jsonObject.getJSONObject("payload");
	//
	// message.setId(payload.getLong("id"));
	// message.setAsset(Long.parseLong(payload.getString("asset")));
	//
	// // // 2014-03-26T11:42:01Z
	// String recordedAtString = payload.getString("recorded_at").replace("Z",
	// "").replace('T', ' ');
	// String recievedAtString = payload.getString("recieved_at").replace("Z",
	// "").replace('T', ' ');
	//
	// SimpleDateFormat dateFormatter = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// message.setRecordedAt(dateFormatter.parse(recordedAtString));
	// message.setRecievedAt(dateFormatter.parse(recievedAtString));
	//
	// return message;
	// }

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		return "test";
	}

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String test(@RequestParam("value") String value, Model model) {
		String url = "http://localhost:8080/munic/rawdata";
		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

			wr.writeBytes(value);
			wr.flush();
			wr.close();

			int responceCode = connection.getResponseCode();

			model.addAttribute("json", value);
			if (responceCode != 200) {
				model.addAttribute("result", "Error");
			} else {
				model.addAttribute("result", "Succes");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "test";
	}

	@RequestMapping(value = "/reprocess", method = RequestMethod.POST)
	public String reprocess() {
		processFailedItems.processMunicRawData(null, null, false);
		return "test";
	}

}
