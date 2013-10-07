package br.com.androidzin.pontopro.test.notification.dailygoal;

import android.content.Intent;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;

public class DailyGoalCompleteAllNotificationDisabled extends DailyGoalCompleteBasic {
	
		
	@Override
	protected void setUpSharedPreference() {
		sharedPreferences.
		edit().
		putString(BusinessHourCommom.WORKING_TIME_KEY, mContext.getString(R.string.eight_hour_value)).
		putBoolean(DailyGoalComplete.KEY_NOTIFICATION_ENABLED, false).
		commit();
	}
	
	public void testScheduleThatShouldNotScheduled(){
		long workedHours = 14400000; // four hours
		notificationManager.scheduleDailyGoalCompleteNotification(System.currentTimeMillis(), workedHours, workedHours);
		Intent intent = notificationManager.getDailyGoalCompleteTimeIntent();
		
		assertAlarmIsNotScheduled(mContext, intent, mAlarmManager);
	}
	
	
	public void testDailyGoalCompleteNofiticationWontShow(){
		long workedHours = 14400000; // four hours
		long dailyMark = workedHours + 5000;
		notificationManager.scheduleDailyGoalCompleteNotification(System.currentTimeMillis(), workedHours, dailyMark);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertNotificationIsNotScheduled(mContext);

	}
	
}
