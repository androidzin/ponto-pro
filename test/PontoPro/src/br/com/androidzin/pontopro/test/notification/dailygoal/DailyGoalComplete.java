package br.com.androidzin.pontopro.test.notification.dailygoal;

import android.content.Intent;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;

public class DailyGoalComplete extends DailyGoalCompleteBasic {
	
	
	protected void setUpSharedPreference() {
		sharedPreferences.
			edit().
			putString(BusinessHourCommom.WORKING_TIME_KEY, mContext.getString(R.string.eight_hour_value)).
			putBoolean(DailyGoalComplete.KEY_NOTIFICATION_ENABLED, true).
			commit();
	}
	
	public void testBasicSchedule(){
		long workedHours = 14400000; // four hours
		notificationManager.scheduleDailyGoalCompleteNotification(System.currentTimeMillis(), workedHours, workedHours);
		Intent intent = notificationManager.getDailyGoalCompleteTimeIntent();
		
		assertAlarmIsScheduled(mContext, intent, mAlarmManager);
	}
	
	public void testBasicSchedule2(){
		long workedHours = 14400000; // four hours
		notificationManager.scheduleDailyGoalCompleteNotification(System.currentTimeMillis(), workedHours, 2*workedHours);
		Intent intent = notificationManager.getDailyGoalCompleteTimeIntent();
		
		assertAlarmIsScheduled(mContext, intent, mAlarmManager);
	}


	public void testCancel(){
		long workedHours = 14400000; // four hours
		notificationManager.scheduleDailyGoalCompleteNotification(System.currentTimeMillis(), workedHours, workedHours);
		notificationManager.onCheckinDone(CheckinType.LEAVING, System.currentTimeMillis(), 0, 0);
		Intent intent = notificationManager.getDailyGoalCompleteTimeIntent();
		
		assertAlarmNotificationWasCancelled(mContext, intent);
	}

	public void testDailyGoalCompleteNofitication(){
		long workedHours = 14400000; // four hours
		long dailyMark = workedHours + 5000;
		notificationManager.scheduleDailyGoalCompleteNotification(System.currentTimeMillis(), workedHours, dailyMark);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertNotificationIsScheduled(mContext);

	}


	
	
}
