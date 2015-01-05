package monitoring.terminal.munic.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitoring.terminal.munic.processing.MessageService;

import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class MunicController extends MultiActionController {

	private MessageService messageService;

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

	public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("test");

		if (request.getMethod() == METHOD_GET) {
			model.addObject("inject", request.getRequestURL().toString());

		} else if (request.getMethod() == METHOD_POST) {

			String url = request.getRequestURL().toString();
			int index = url.lastIndexOf('/');
			url = url.substring(0, index);
			url = url.concat("/rawdata");
			
			URL obj;
			try {
				obj = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

				connection.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

				String value = request.getParameter("value");
				wr.writeBytes(value);
				wr.flush();
				wr.close();

				int responceCode = connection.getResponseCode();

				model.addObject("json", value);
				if (responceCode != 200) {
					model.addObject("result", "Error");
				} else {
					model.addObject("result", "Succes");
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return model;
	}
}
