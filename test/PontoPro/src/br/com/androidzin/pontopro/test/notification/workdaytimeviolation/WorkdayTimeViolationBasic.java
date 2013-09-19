package br.com.androidzin.pontopro.test.notification.workdaytimeviolation;

import android.content.Intent;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import br.com.androidzin.pontopro.test.notification.NotificationsBasic;

public class WorkdayTimeViolationBasic extends NotificationsBasic {

	@Override
	protected void setUpSharedPreference() {
		// TODO Auto-generated method stub

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
