package br.com.androidzin.pontopro.test.notification.workdaytimeviolation;


import android.content.Intent;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;
import br.com.androidzin.pontopro.util.Constants;

public class WorkingTimeViolation extends WorkdayTimeViolationBasic {
	
	
	
	protected void setUpSharedPreference() {
		sharedPreferences.
			edit().
			putString(BusinessHourCommom.EATING_TIME_KEY, String.valueOf(Constants.hoursInMilis*2)).
			putLong(BusinessHourCommom.LUNCH_CHECKIN_KEY, getTimeInMilis(12, 0)).
			putString(BusinessHourCommom.WORKING_TIME_KEY, mContext.getString(R.string.eight_hour_value)).
			putBoolean(KEY_NOTIFICATION_ENABLED, true).
			putBoolean(KEY_WORKING_TIME_VIOLATION_NOTIFICATION, true).
			commit();
	}
	
	public void testBasicSchedule(){
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours, 0);
		
		Intent intent = notificationManager.getWorkingTimeViolationIntent();
		
		assertAlarmIsScheduled(mContext, intent, mAlarmManager);
		
	}
	
	public void testScheduleWithMultipleCheckins(){
		long eightOClock = getTimeInMilis(8, 0);
		long eightAndHalf = getTimeInMilis(8, 30);
		long nineOClock = getTimeInMilis(9, 0);
		long twelveOClock = getTimeInMilis(12, 0);
		long fourteenOClock = getTimeInMilis(14, 0);
		long fifteenOClock = getTimeInMilis(15, 0);
		long sixteenOClock = getTimeInMilis(16, 0);
		long twentyOClock = getTimeInMilis(20, 0);
		long workedHours = 0;
				
		notificationManager.onCheckinDone(CheckinType.ENTERED, eightOClock, 0L, 0L);
		workedHours =+ Constants.hoursInMilis/2;
		notificationManager.onCheckinDone(CheckinType.ANY_LEAVING_BEFORE_LUNCH, eightAndHalf, workedHours, 0L);
		
		notificationManager.onCheckinDone(CheckinType.ANY_ENTRANCE_BEFORE_LUNCH, nineOClock, workedHours, 0L);
		workedHours += 3*Constants.hoursInMilis;
		notificationManager.onCheckinDone(CheckinType.LUNCH, twelveOClock, workedHours, 0L);
		
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, fourteenOClock, workedHours, 0L);
		workedHours += Constants.hoursInMilis;
		notificationManager.onCheckinDone(CheckinType.ANY_LEAVING_AFTER_LUNCH, fifteenOClock, workedHours, 0L);
		
		notificationManager.onCheckinDone(CheckinType.ANY_ENTRANCE_AFTER_LUNCH, sixteenOClock, workedHours, 0L);
		workedHours += 4*Constants.hoursInMilis;
		
		Intent intent = notificationManager.getWorkingTimeViolationIntent();
		assertAlarmIsScheduled(mContext, intent, mAlarmManager);

		notificationManager.onCheckinDone(CheckinType.LEAVING, twentyOClock, workedHours, 0L);
		
		assertAlarmNotificationWasCancelled(mContext, intent);
	}


	public void testCancel(){
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours, 0);
		notificationManager.onCheckinDone(CheckinType.LEAVING, System.currentTimeMillis(), 0, 0);
		Intent intent = notificationManager.getWorkingTimeViolationIntent();
		
		assertAlarmNotificationWasCancelled(mContext, intent);
		
	}

	public void testWorkingTimeViolationCompleteNofitication(){
		long when = System.currentTimeMillis();
		long maxWorkingTime = 36000000;;
		long workedHours = maxWorkingTime - 5000;
		
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, when, workedHours, 0);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertNotificationIsScheduled(mContext);

	}
	
}
