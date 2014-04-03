package ru.gm.munic.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.gm.munic.domain.Message;
import ru.gm.munic.service.MessageService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private MessageService messageService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void home(HttpServletRequest request, HttpServletResponse response) {
		try {
			JSONTokener jsonTokener = new JSONTokener(request.getInputStream());
			logger.info(jsonTokener.toString());

			JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				Message message = new Message();
				message.setId(getMessageId(jsonObject));
				message.setValue(jsonObject.toString());
				messageService.process(message);

				response.setStatus(200);
			}
		} catch (UnsupportedEncodingException e) {
			response.setStatus(403);
		} catch (IOException e) {
			response.setStatus(500);
		}
	}

	private Long getMessageId(JSONObject jsonObject) {
		JSONObject payload = jsonObject.getJSONObject("payload");
		return payload.getLong("id");
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		return "test";
	}

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String test(@RequestParam("value") String value, Model model) {
		String url = "http://localhost:8080/munic/";
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
}
