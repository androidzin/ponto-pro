package br.com.androidzin.pontopro.test.notification.workdaytimeviolation;

import android.content.Intent;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import br.com.androidzin.pontopro.test.notification.NotificationsBasic;

public abstract class WorkdayTimeViolationBasic extends NotificationsBasic {
	
	static final String KEY_WORKING_TIME_VIOLATION_NOTIFICATION = "pref_key_notification_maxhour_enabled";

	@Override
	public void testNotificationIntent(){
		Intent intent = notificationManager.getWorkingTimeViolationIntent();
		
		assertEquals(CheckinNotificationManager.WORKING_TIME_VIOLATION,
				intent.getAction());
		
		assertEquals(CheckinNotificationManager.class.getName(),
				intent.getComponent().getClassName());
		
	}

}
