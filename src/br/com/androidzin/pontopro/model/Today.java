package br.com.androidzin.pontopro.model;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import br.com.androidzin.pontopro.data.provider.PontoProContract;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;

public class Today extends Workday {
	
	private String workdayPrefID = "workdayID";
	private String workdayPrefOpenCheckin  = "workdayOpenCheckin";
	private String workdayPrefWorkedTime = "workdayWorkedTime";
	private int checkinCount;
	
	public Today() {
		super();
		checkinCount = 0;
	}
	 
	public void store(SharedPreferences mSharedPreferences) {
		mSharedPreferences.edit().putLong(workdayPrefID, getWorkdayID());
		mSharedPreferences.edit().putBoolean(workdayPrefOpenCheckin, hasOpenCheckin());
		mSharedPreferences.edit().putInt(workdayPrefWorkedTime, getWorkedTime());
		
		mSharedPreferences.edit().commit();
	}
	
	public void retrieve(SharedPreferences mSharedPreferences){
		long workdayID = mSharedPreferences.getLong(workdayPrefID, 0);
		if ( workdayID != 0 ) {
			setWorkdayID(workdayID);
			setHasOpenCheckin(mSharedPreferences.getBoolean(workdayPrefOpenCheckin, true));
		}
	}

	public void refreshData(SharedPreferences mSharedPreferences) {
		Checkin checkin = new Checkin(mSharedPreferences.getLong(workdayPrefID, 0));
		checkin.setTimeStamp(String.valueOf(System.currentTimeMillis()));
		addCheckin(checkin);
		updateWorkdayStatus();
	}
	
	@Override
	public void updateWorkdayStatus() {
		super.updateWorkdayStatus();
		checkinCount++;
		// TODO condicao para verificar ultimo checkin do dia
	}
	
	public CheckinType getCheckinType(){
		switch (checkinCount) {
		case 1:
			return CheckinType.ENTERED;
		case 2:
			return CheckinType.LUNCH;
		case 3:
			return CheckinType.AFTER_LUNCH;
		case 4:
			return CheckinType.LEAVING;
		default:
			break;
		}
		return CheckinType.ANY;
	}

	public void initData(SharedPreferences mSharedPreferences, String workdayID) {
		setWorkdayID(Long.valueOf(workdayID));
		setHasOpenCheckin(false);
		setWorkedTime(0);
		checkinCount = 0;
		refreshData(mSharedPreferences);
	}
}
