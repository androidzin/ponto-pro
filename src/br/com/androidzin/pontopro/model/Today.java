package br.com.androidzin.pontopro.model;

import android.content.SharedPreferences;
import br.com.androidzin.pontopro.model.Checkin.CheckinListener;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;

public class Today extends Workday {
	
	private String workdayPrefID = "workday.ID";
	private String workdayPrefOpenCheckin  = "workday.openCheckin";
	private String workdayPrefWorkedTime = "workday.workedTime";
	private String workdayPrefDailyMark = "workday.dailyMark";
	private String workdayPrefCheckins ="workday.checkinCount";
	private String workdayPrefValid= "workday.valid";
	
	private final String checkinPrefID = "checkin.ID";
	private final String checkinStatus = "checkin.status";
	private final String checkinCount = "checkin.count";
	private final String checkinHour = "checkin.hour";
	
	private int checkinCounter;
	private CheckinListener mCheckinListener;
	
	
	public void setCheckinListener(CheckinListener checkinListener){
		mCheckinListener = checkinListener;
	}
	public Today() {
		super();
		checkinCounter = 0;
	}
	
	public void initData(SharedPreferences mSharedPreferences, String workdayID, int workedTime, int dailyMark) {
		setWorkdayID(Long.valueOf(workdayID));
		setHasOpenCheckin(false);
		setWorkedTime(workedTime);
		setDailyMak(dailyMark);
		checkinCounter = 0;
	}
	 
	public void save(SharedPreferences mSharedPreferences) {
		refreshData(mSharedPreferences); 
	}
	
	public void load(SharedPreferences mSharedPreferences){
		long workdayID = mSharedPreferences.getLong(workdayPrefID, 0);
		if ( workdayID != 0 ) {
			setWorkdayID(workdayID);
			setHasOpenCheckin(mSharedPreferences.getBoolean(workdayPrefOpenCheckin, false));
		}
	}
	
	@Override
	public void addCheckin(Checkin checkin) {
		super.addCheckin(checkin);
		updateWorkdayStatus();
	}
	
	public void refreshData(SharedPreferences mSharedPreferences){
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putLong(workdayPrefID, getWorkdayID());
		editor.putBoolean(workdayPrefOpenCheckin, hasOpenCheckin());
		editor.putInt(workdayPrefWorkedTime, getWorkedTime());
		editor.putInt(workdayPrefDailyMark, getDailyMark());
		editor.putInt(workdayPrefCheckins, checkinCounter);
		editor.putBoolean(workdayPrefValid, started());
		editor.commit();
		
	}

	public void refreshCheckinData(SharedPreferences mSharedPreferences) {
		Checkin checkin = new Checkin(mSharedPreferences.getLong(workdayPrefID, 0));
		checkin.setTimeStamp(String.valueOf(System.currentTimeMillis()));
		addCheckin(checkin);
		updateWorkdayStatus();
		storeCheckinData(mSharedPreferences, checkin);
	}
	
	private void storeCheckinData(SharedPreferences mSharedPreferences,
			Checkin checkin) {
		SharedPreferences.Editor editor = mSharedPreferences.edit(); 
		editor.putLong(checkinPrefID, checkin.getCheckinID());
		editor.putString(checkinHour, checkin.getTimeStamp());
		editor.putInt(checkinCount, checkinCounter);
		editor.putBoolean(checkinStatus, hasOpenCheckin());
		editor.commit();
	}

	@Override
	public void updateWorkdayStatus() {
		super.updateWorkdayStatus();
		checkinCounter++;
		// TODO condicao para verificar ultimo checkin do dia
	}
	
	public CheckinType getCheckinType(){
		switch (checkinCounter) {
		case 1:
			return CheckinType.ENTERED;
		case 2:
			return CheckinType.LUNCH;
		case 3:
			return CheckinType.AFTER_LUNCH;
		case 4:
			return CheckinType.LEAVING;
		case 5:
			return CheckinType.ANY_ENTRANCE;
		case 6:
			return CheckinType.ANY_LEAVING;
		default:
			break;
		}
		return CheckinType.ANY;
	}

	public void refreshData(SharedPreferences mSharedPreferences,
			Checkin checkin) {
		storeCheckinData(mSharedPreferences, checkin);
		notifyListener();
	}
	
	private void notifyListener(){
		if ( mCheckinListener != null ) {
			mCheckinListener.onCheckinDone(getCheckinType(), System.currentTimeMillis());
		}
	}

	

}
