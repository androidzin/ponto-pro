package br.com.androidzin.pontopro.test.notification;

import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;

public class WorkdayCompleteDisabled extends WorkdayCompleteAllDisabled {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		sharedPreferences.
		edit().
		putString(BusinessHourCommom.WORKING_TIME_KEY, mContext.getString(R.string.eight_hour_value)).
		putBoolean(WorkdayComplete.KEY_NOTIFICATION_ENABLED, true).
		putBoolean(WorkdayComplete.KEY_WORKDAY_COMPLETE_NOTIFICATION, false).
		commit();
		
	}

}
