package br.com.androidzin.pontopro.test.notification;

import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;

public class WorkdayComplete extends AndroidTestCase {
	
	private Context mContext;
	private CheckinNotificationManager notificationManager;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		mContext = getContext();
		notificationManager = new CheckinNotificationManager(mContext);
	}
	
	public void testNotificationIntent(){
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		
		assertEquals(CheckinNotificationManager.WORKDAY_COMPLETE,
				intent.getAction());
		assertEquals(CheckinNotificationManager.class.getName(),
				intent.getComponent().getClassName());
		
	}
	
	public void testBasicSchedule(){
		long when = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), when);
		
		AlarmManager alarmManager = 
				(AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		
		PendingIntent notifier = PendingIntent.getBroadcast(mContext, 0, intent,
				PendingIntent.FLAG_NO_CREATE);
		
		assertNotNull("Fuuuu", notifier);
		
		alarmManager.cancel(notifier);
	}

}
