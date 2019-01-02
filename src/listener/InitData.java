package listener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import entities.LineEntity;

public class InitData implements ServletContextListener {

	private static final long PERIOD_TIME = 12 * 60 * 60 * 1000;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(final ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("contextInitialized");
		Calendar firstCalendar = Calendar.getInstance();
		firstCalendar.set(Calendar.HOUR_OF_DAY, 8);
		firstCalendar.set(Calendar.MINUTE, 0);
		firstCalendar.set(Calendar.SECOND, 0);
		Date firstDate = firstCalendar.getTime();

		Calendar secondCalendar = Calendar.getInstance();
		secondCalendar.set(Calendar.HOUR_OF_DAY, 20);
		secondCalendar.set(Calendar.MINUTE, 0);
		secondCalendar.set(Calendar.SECOND, 0);
		Date secondDate = secondCalendar.getTime();

		Date date = null;
		Date nowDate = new Date();
		if (firstDate.before(nowDate) && secondDate.after(nowDate)) {
			date = secondDate;
		} else {
			date = firstDate;
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("InitData");
				@SuppressWarnings("unchecked")
				HashMap<String, LineEntity> lineEntities = (HashMap<String, LineEntity>) arg0
						.getServletContext().getAttribute("lineEntities");
				if (lineEntities != null) {
					lineEntities.clear();
				}
			}
		}, date, PERIOD_TIME);
	}

}
