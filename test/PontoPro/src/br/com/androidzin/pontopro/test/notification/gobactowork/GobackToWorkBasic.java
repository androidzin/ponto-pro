package br.com.androidzin.pontopro.test.notification.gobactowork;

import android.content.Intent;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import br.com.androidzin.pontopro.test.notification.NotificationsBasic;

public abstract class GobackToWorkBasic extends NotificationsBasic {

	@Override
	public void testNotificationIntent() {
		Intent intent = notificationManager.getGobackToWorkIntent();
		
		assertEquals(CheckinNotificationManager.GOBACK_TO_WORK,
				intent.getAction());
		assertEquals(CheckinNotificationManager.class.getName(),
				intent.getComponent().getClassName());
		
	}

}
