package br.com.androidzin.pontopro.test.notification.workdaytimeviolation;

import android.content.Intent;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import br.com.androidzin.pontopro.test.notification.NotificationsBasic;

public abstract class WorkdayTimeViolationBasic extends NotificationsBasic {
	
	static final String KEY_WORKING_TIME_VIOLATION_NOTIFICATION = "pref_key_notification_maxhour_enabled";

	protected void doCheckinCauseSchedule() {
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours);
	}
	
	protected void doCheckinCauseCancel() {
		notificationManager.onCheckinDone(CheckinType.LEAVING, System.currentTimeMillis(), 0);
	}


	@Override
	public void testNotificationIntent(){
		Intent intent = notificationManager.getWorkingTimeViolationIntent();
		
		assertEquals(CheckinNotificationManager.WORKING_TIME_VIOLATION,
				intent.getAction());
		
		assertEquals(CheckinNotificationManager.class.getName(),
				intent.getComponent().getClassName());
		
	}

}
