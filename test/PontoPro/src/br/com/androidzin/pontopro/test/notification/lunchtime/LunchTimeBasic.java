package br.com.androidzin.pontopro.test.notification.lunchtime;

import android.content.Intent;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import br.com.androidzin.pontopro.test.notification.NotificationsBasic;
import br.com.androidzin.pontopro.util.Constants;

public abstract class LunchTimeBasic extends NotificationsBasic {
	
	protected static final long EATING_TIME_TEST = Constants.hoursInMilis*12;
	
	protected abstract void setUpSharedPreference();
	
	public void testNotificationIntent(){
		Intent intent = notificationManager.getLunchTimeIntent();
		
		assertEquals(CheckinNotificationManager.LUNCH_TIME,
				intent.getAction());
		assertEquals(CheckinNotificationManager.class.getName(),
				intent.getComponent().getClassName());
		
	}
	
}
