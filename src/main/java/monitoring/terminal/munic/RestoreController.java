package monitoring.terminal.munic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class RestoreController extends MultiActionController {
	private RestoreProcessor restoreProcessor;
	
	public ModelAndView restore(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("restore");

		if (request.getMethod() == METHOD_GET) {
			model.addObject("inject", request.getRequestURL().toString());

		} else if (request.getMethod() == METHOD_POST) {
			String date1String = request.getParameter("date1");
			String date2String = request.getParameter("date2");
			
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			try {
				Date date1 = dateFormatter.parse(date1String);
				Date date2 = dateFormatter.parse(date2String);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date1);
				Date cursor1 = date1;
				Date cursor2 = date1;
				
				while(cursor1.before(date2)) {
					cursor1 = calendar.getTime();
					calendar.add(Calendar.DATE, 1);
					cursor2 = calendar.getTime();
					
					restoreProcessor.restore(cursor1, cursor2);
				}
				
				model.addObject("result", "Restored");
			} catch (ParseException e) {
				model.addObject("result", e.toString());
			}
		}
		return model;
	}

	public void setRestoreProcessor(RestoreProcessor restoreProcessor) {
		this.restoreProcessor = restoreProcessor;
	}

}
