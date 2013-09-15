package br.com.androidzin.pontopro.test.notification.workdaycomplete;

import android.content.Intent;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import br.com.androidzin.pontopro.test.notification.NotificationsBasic;

public abstract class WorkdayBasic extends NotificationsBasic {
	
	public static final String KEY_WORKDAY_COMPLETE_NOTIFICATION = "pref_key_notification_bussineshour_enabled";
	
	protected abstract void setUpSharedPreference();
	
	public void testNotificationIntent(){
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		
		assertEquals(CheckinNotificationManager.WORKDAY_COMPLETE,
				intent.getAction());
		assertEquals(CheckinNotificationManager.class.getName(),
				intent.getComponent().getClassName());
		
	}
	
}
