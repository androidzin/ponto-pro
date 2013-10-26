package br.com.androidzin.pontopro.test.notification.gobactowork;

import android.content.Intent;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;
import br.com.androidzin.pontopro.test.notification.workdaycomplete.WorkdayComplete;

public class GobackToWorkAllNotificaionDisabled extends GobackToWorkBasic {

	private final long TEST_EATING_TIME = 3000;
	@Override
	protected void setUpSharedPreference() {
		sharedPreferences.
		edit().
		putString(BusinessHourCommom.EATING_TIME_KEY, String.valueOf(TEST_EATING_TIME)).
		putBoolean(WorkdayComplete.KEY_NOTIFICATION_ENABLED, false).
		commit();
	}
	
	public void testScheduleThatShouldNotScheduled(){
		notificationManager.onCheckinDone(CheckinType.LUNCH, System.currentTimeMillis(), 0, 0);
		Intent intent = notificationManager.getGobackToWorkIntent();
		
		assertAlarmIsNotScheduled(mContext, intent, mAlarmManager);
	}
	
	public void testGobackToWorkNofiticationWontShow(){
		notificationManager.onCheckinDone(CheckinType.LUNCH, System.currentTimeMillis(), 0, 0);
		
		try {
			Thread.sleep(TEST_EATING_TIME + 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertNotificationIsNotScheduled(mContext);

	}


}
