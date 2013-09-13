package br.com.androidzin.pontopro.test.notification.workdaycomplete;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.notification.CheckinNotificationManager;
import br.com.androidzin.pontopro.test.notification.Utils;

public abstract class WorkdayBasic extends AndroidTestCase {
	
	protected Context mContext;
	protected CheckinNotificationManager notificationManager;
	protected SharedPreferences sharedPreferences;
	protected AlarmManager mAlarmManager;
	
	static final String KEY_NOTIFICATION_ENABLED = "pref_key_notification_enabled";
	static final String KEY_WORKDAY_COMPLETE_NOTIFICATION = "pref_key_notification_bussineshour_enabled";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		initializeContext();
		setUpSharedPreference();
		cancelPastAlarmsAndNotifications();
	}


	private void initializeContext() {
		mContext = getContext();
		notificationManager = new CheckinNotificationManager(mContext);
		mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
	}
	

	protected abstract void setUpSharedPreference();
	
	protected void cancelPastAlarmsAndNotifications() {
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		Utils.cancelAlarm(mContext, intent, mAlarmManager);
		Utils.cancelScheduledNotifications(mContext);
	}

	public void testNotificationIntent(){
		Intent intent = notificationManager.getWorkdayCompleteIntent();
		
		assertEquals(CheckinNotificationManager.WORKDAY_COMPLETE,
				intent.getAction());
		assertEquals(CheckinNotificationManager.class.getName(),
				intent.getComponent().getClassName());
		
	}
	
	protected void doCheckinCauseSchedule() {
		long workedHours = 14400000; // four hours
		notificationManager.onCheckinDone(CheckinType.AFTER_LUNCH, System.currentTimeMillis(), workedHours);
	}
	
	protected void doCheckinCauseCancel() {
		notificationManager.onCheckinDone(CheckinType.LEAVING, System.currentTimeMillis(), 0);
	}

}
