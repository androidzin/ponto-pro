package br.com.androidzin.pontopro.test.notification.workdaytimeviolation;


import android.content.Intent;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;

public class WorkingTimeViolationAllNotificationDisabled extends WorkdayTimeViolationBasic {
	
	
	
	protected void setUpSharedPreference() {
		sharedPreferences.
			edit().
			putString(BusinessHourCommom.WORKING_TIME_KEY, mContext.getString(R.string.eight_hour_value)).
			putBoolean(KEY_NOTIFICATION_ENABLED, false).
			commit();
	}
	
	public void testScheduleThatShouldNotScheduled(){
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours, 0L);
		
		Intent intent = notificationManager.getWorkingTimeViolationIntent();
		
		assertAlarmIsNotScheduled(mContext, intent, mAlarmManager);
		
	}

	public void ttestWorkdayViolationNofiticationWontShow(){
		long when = System.currentTimeMillis();
		long maxWorkingTime = 36000000;;
		long workedHours = maxWorkingTime - 5000;
		
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, when, workedHours, 0L);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertNotificationIsNotScheduled(mContext);

	}
	
}
