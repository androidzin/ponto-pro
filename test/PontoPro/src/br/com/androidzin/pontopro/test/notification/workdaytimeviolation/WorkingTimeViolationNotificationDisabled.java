package br.com.androidzin.pontopro.test.notification.workdaytimeviolation;


import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;

public class WorkingTimeViolationNotificationDisabled extends WorkingTimeViolationAllNotificationDisabled {
	
	
	
	protected void setUpSharedPreference() {
		sharedPreferences.
		edit().
		putString(BusinessHourCommom.WORKING_TIME_KEY, mContext.getString(R.string.eight_hour_value)).
		putBoolean(KEY_NOTIFICATION_ENABLED, true).
		putBoolean(KEY_WORKING_TIME_VIOLATION_NOTIFICATION, false).
		commit();
	}
	
}
