package br.com.androidzin.pontopro.test.notification.lunchtime;

import java.util.Calendar;

import android.content.Intent;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;
import br.com.androidzin.pontopro.util.Constants;

public class LunchTime extends LunchTimeBasic {
	
	protected void setUpSharedPreference() {
		sharedPreferences.
			edit().
			putString(BusinessHourCommom.EATING_TIME_KEY, String.valueOf(Constants.hoursInMilis*2)).
			putLong(BusinessHourCommom.LUNCH_CHECKIN_KEY, EATING_TIME_TEST).
			putBoolean(LunchTime.KEY_NOTIFICATION_ENABLED, true).
			commit();
	}
	
	public void testBasicSchedule(){
		notificationManager.onCheckinDone(CheckinType.ENTERED, 0L, 0L, 0L);
		Intent intent = notificationManager.getLunchTimeIntent();
		
		assertAlarmIsScheduled(mContext, intent, mAlarmManager);
	}
	
	public void testScheduleWithMultipleCheckins(){
		long eightOClock = getTimeInMilis(8, 0);
		long eightAndHalf = getTimeInMilis(8, 30);
		long nineOClock = getTimeInMilis(9, 0);
		long nineAndHalf = getTimeInMilis(9, 30);
		long elevenOClock = getTimeInMilis(11, 0);
		
		notificationManager.onCheckinDone(CheckinType.ENTERED, eightOClock, 0L, 0L);
		notificationManager.onCheckinDone(CheckinType.ANY_LEAVING_BEFORE_LUNCH, eightAndHalf, Constants.hoursInMilis/2, 0L);
		
		notificationManager.onCheckinDone(CheckinType.ANY_ENTRANCE_BEFORE_LUNCH, nineOClock, Constants.hoursInMilis/2, 0L);
		notificationManager.onCheckinDone(CheckinType.ANY_LEAVING_BEFORE_LUNCH, nineAndHalf, Constants.hoursInMilis, 0L);
		
		notificationManager.onCheckinDone(CheckinType.ANY_ENTRANCE_BEFORE_LUNCH, elevenOClock, Constants.hoursInMilis, 0L);
		
		Intent intent = notificationManager.getLunchTimeIntent();
		assertAlarmIsScheduled(mContext, intent, mAlarmManager);
		
		notificationManager.onCheckinDone(CheckinType.LUNCH, EATING_TIME_TEST, 2*Constants.hoursInMilis, 0L);
		assertAlarmNotificationWasCancelled(mContext, intent);
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
