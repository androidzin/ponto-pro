package br.com.androidzin.pontopro.test.notification.workdaytimeviolation;


import android.content.Intent;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;

public class WorkingTimeViolation extends WorkdayTimeViolationBasic {
	
	
	
	protected void setUpSharedPreference() {
		sharedPreferences.
			edit().
			putString(BusinessHourCommom.WORKING_TIME_KEY, mContext.getString(R.string.eight_hour_value)).
			putBoolean(KEY_NOTIFICATION_ENABLED, true).
			putBoolean(KEY_WORKING_TIME_VIOLATION_NOTIFICATION, true).
			commit();
	}
	
	public void testBasicSchedule(){
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours);;
		
		Intent intent = notificationManager.getWorkingTimeViolationIntent();
		
		assertAlarmIsScheduled(mContext, intent, mAlarmManager);
		
	}

	public void testCancel(){
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours);;
		notificationManager.onCheckinDone(CheckinType.LEAVING, System.currentTimeMillis(), 0);
		Intent intent = notificationManager.getWorkingTimeViolationIntent();
		
		assertAlarmNotificationWasCancelled(mContext, intent);
		
	}

	public void testWorkingTimeViolationCompleteNofitication(){
		long when = System.currentTimeMillis();
		long maxWorkingTime = 36000000;;
		long workedHours = maxWorkingTime - 5000;
		
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, when, workedHours);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertNotificationIsScheduled(mContext);

	}
	
}
