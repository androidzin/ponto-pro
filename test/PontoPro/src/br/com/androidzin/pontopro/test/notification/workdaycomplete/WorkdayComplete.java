package br.com.androidzin.pontopro.test.notification.workdaycomplete;

import android.content.Intent;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;
import br.com.androidzin.pontopro.test.notification.Utils;

public class WorkdayComplete extends WorkdayBasic {
	
	
	protected void setUpSharedPreference() {
		sharedPreferences.
			edit().
			putString(BusinessHourCommom.WORKING_TIME_KEY, mContext.getString(R.string.eight_hour_value)).
			putBoolean(KEY_NOTIFICATION_ENABLED, true).
			putBoolean(KEY_WORKDAY_COMPLETE_NOTIFICATION, true).
			commit();
	}
	
	public void testBasicSchedule(){
		doCheckinCauseSchedule();
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		
		Utils.assertAlarmIsScheduled(mContext, intent, mAlarmManager);
	}


	public void testCancel(){
		doCheckinCauseSchedule();
		doCheckinCauseCancel();
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		
		Utils.assertAlarmNotificationWasCancelled(mContext, intent);
	}

	public void testWorkDayCompleteNofitication(){
		long when = System.currentTimeMillis();
		long workingTime = BusinessHourCommom.getWorkingTime(sharedPreferences);
		long workedHours = workingTime - 5000;
		
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, when, workedHours);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Utils.assertNotificationIsScheduled(mContext);

	}


	
	
}
