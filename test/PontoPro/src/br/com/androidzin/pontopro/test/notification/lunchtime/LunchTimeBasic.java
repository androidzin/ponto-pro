package br.com.androidzin.pontopro.test.notification.lunchtime;

import java.util.Calendar;

import android.content.Intent;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import br.com.androidzin.pontopro.test.notification.NotificationsBasic;
import br.com.androidzin.pontopro.util.Constants;

public abstract class LunchTimeBasic extends NotificationsBasic {
	
	protected static long EATING_TIME_TEST = 0;
	
	public LunchTimeBasic() {
		super();
		EATING_TIME_TEST = getTimeInMilis(12, 0);
	}
	
	protected abstract void setUpSharedPreference();
	
	public void testNotificationIntent(){
		Intent intent = notificationManager.getLunchTimeIntent();
		
		assertEquals(CheckinNotificationManager.LUNCH_TIME,
				intent.getAction());
		assertEquals(CheckinNotificationManager.class.getName(),
				intent.getComponent().getClassName());
		
	}
	
}
