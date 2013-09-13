package br.com.androidzin.pontopro.test.notification.workdaytimeviolation;


import android.app.PendingIntent;
import android.content.Intent;
import br.com.androidzin.pontopro.MainActivity;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;

public class WorkingTimeViolation extends WorkdayTimeViolationBasic {
	
	static final String KEY_WORKING_TIME_VIOLATION_NOTIFICATION = "pref_key_notification_maxhour_enabled";
	
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
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours);
		
		Intent intent = notificationManager.getWorkingTimeViolationIntent();
		
		PendingIntent notifier = PendingIntent.getBroadcast(mContext, 0, intent,
				PendingIntent.FLAG_NO_CREATE);
		
		assertNotNull("Schedule did not work", notifier);
		
		mAlarmManager.cancel(notifier);
	}
	
	public void testCancel(){
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours);

		notificationManager.onCheckinDone(CheckinType.LEAVING, System.currentTimeMillis(), 0);
		
		Intent intent = notificationManager.getWorkingTimeViolationIntent();
		
		PendingIntent notifier = PendingIntent.getBroadcast(mContext, 0, intent,
					PendingIntent.FLAG_NO_CREATE);
		
		
		assertNull("Cancel notification did not work", notifier);
		
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
		
		Intent resultIntent = new Intent(mContext, MainActivity.class);
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    mContext,
		    0,
		    resultIntent,
		    PendingIntent.FLAG_NO_CREATE
		);
		
		
		assertNotNull("Notification is not scheduled", resultPendingIntent);

	}
	
}
