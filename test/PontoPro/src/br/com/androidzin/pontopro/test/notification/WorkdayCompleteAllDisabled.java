package br.com.androidzin.pontopro.test.notification;

import android.app.PendingIntent;
import android.content.Intent;
import br.com.androidzin.pontopro.MainActivity;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;

public class WorkdayCompleteAllDisabled extends WorkdayComplete {
	
		
	@Override
	protected void setUpSharedPreference() {
		sharedPreferences.
		edit().
		putString(BusinessHourCommom.WORKING_TIME_KEY, mContext.getString(R.string.eight_hour_value)).
		putBoolean(WorkdayComplete.KEY_NOTIFICATION_ENABLED, false).
		commit();
	}
	
	@Override
	public void testBasicSchedule(){
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours);
		
		
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		
		PendingIntent notifier = PendingIntent.getBroadcast(mContext, 0, intent,
				PendingIntent.FLAG_NO_CREATE);
		
		assertNull("Notification was scheduled", notifier);
		
		alarmManager.cancel(notifier);
	}
	
	@Override
	public void testCancel() {
	}
	@Override
	public void testNotificationIntent() {
	}
	
	@Override
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
		
		Intent resultIntent = new Intent(mContext, MainActivity.class);
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    mContext,
		    0,
		    resultIntent,
		    PendingIntent.FLAG_NO_CREATE
		);
		
		
		assertNull("Notification is scheduled", resultPendingIntent);

	}
	
}
