package br.com.androidzin.pontopro.test.notification.dailygoal;

import android.content.Intent;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import br.com.androidzin.pontopro.test.notification.NotificationsBasic;

public abstract class DailyGoalCompleteBasic extends NotificationsBasic {
	
	protected abstract void setUpSharedPreference();
	
	public void testNotificationIntent(){
		Intent intent = notificationManager.getDailyGoalCompleteTimeIntent();
		
		assertEquals(CheckinNotificationManager.DAILY_GOAL_COMPLETE,
				intent.getAction());
		assertEquals(CheckinNotificationManager.class.getName(),
				intent.getComponent().getClassName());
		
	}
	
}
