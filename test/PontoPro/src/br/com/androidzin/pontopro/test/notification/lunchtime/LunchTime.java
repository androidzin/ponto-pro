package br.com.androidzin.pontopro.test.notification.lunchtime;

import android.content.Intent;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;
import br.com.androidzin.pontopro.util.Constants;

public class LunchTime extends LunchTimeBasic {
	
	protected void setUpSharedPreference() {
		sharedPreferences.
			edit().
			putString(BusinessHourCommom.EATING_TIME_KEY, String.valueOf(Constants.hoursInMilis*2)).
			putLong(BusinessHourCommom.LUNCH_CHECKIN_ERROR, EATING_TIME_TEST).
			putBoolean(LunchTime.KEY_NOTIFICATION_ENABLED, true).
			commit();
	}
	
	public void testBasicSchedule(){
		notificationManager.onCheckinDone(CheckinType.ENTERED, 0L, 0L, 0L);
		Intent intent = notificationManager.getLunchTimeIntent();
		
		assertAlarmIsScheduled(mContext, intent, mAlarmManager);
	}


	public void testCancel(){
		notificationManager.onCheckinDone(CheckinType.ENTERED, 0L, 0L, 0L);
		notificationManager.onCheckinDone(CheckinType.LUNCH, EATING_TIME_TEST - Constants.hoursInMilis , 0L, 0L);
		Intent intent = notificationManager.getLunchTimeIntent();
		
		assertAlarmNotificationWasCancelled(mContext, intent);
	}

	public void testLunchTimeNofitication(){
		int delay = 3000;
		long when = System.currentTimeMillis();
		
		sharedPreferences.edit().putLong(BusinessHourCommom.LUNCH_CHECKIN_KEY, when + delay).commit();
		notificationManager.onCheckinDone(CheckinType.ENTERED, 0L, 0L, 0L);
		
		
		try {
			Thread.sleep(delay + 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertNotificationIsScheduled(mContext);

	}


	
	
}
