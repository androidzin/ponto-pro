package br.com.androidzin.pontopro.test.notification;

import br.com.androidzin.pontopro.MainActivity;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;

public class WorkdayComplete extends AndroidTestCase {
	
	private Context mContext;
	private CheckinNotificationManager notificationManager;
	private SharedPreferences sharedPreferences;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		mContext = getContext();
		notificationManager = new CheckinNotificationManager(mContext);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		sharedPreferences.
			edit().
			putString(BusinessHourCommom.WORKING_TIME_KEY, mContext.getString(R.string.eight_hour_value)).
			commit();
	}
	
	public void testNotificationIntent(){
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		
		assertEquals(CheckinNotificationManager.WORKDAY_COMPLETE,
				intent.getAction());
		assertEquals(CheckinNotificationManager.class.getName(),
				intent.getComponent().getClassName());
		
	}
	
	public void testBasicSchedule(){
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours);
		
		AlarmManager alarmManager = 
				(AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		
		PendingIntent notifier = PendingIntent.getBroadcast(mContext, 0, intent,
				PendingIntent.FLAG_NO_CREATE);
		
		assertNotNull("Schedule did not work", notifier);
		
		alarmManager.cancel(notifier);
	}
	
	public void testCancel(){
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours);

		notificationManager.onCheckinDone(CheckinType.LEAVING, System.currentTimeMillis(), 0);
		
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		
		PendingIntent notifier = PendingIntent.getBroadcast(mContext, 0, intent,
					PendingIntent.FLAG_NO_CREATE);
		
		
		assertNull("Cancel notification did not work", notifier);
		
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
		
		Intent resultIntent = new Intent(mContext, MainActivity.class);
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    mContext,
		    0,
		    resultIntent,
		    PendingIntent.FLAG_NO_CREATE
		);
		
		
		assertNotNull("Notification is not scheduled", resultIntent);

	}

}
