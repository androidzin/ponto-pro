package br.com.androidzin.pontopro.model;

import android.content.SharedPreferences;
import br.com.androidzin.pontopro.model.Checkin.CheckinListener;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;

public class Today extends Workday {
	
	private String workdayPrefID = "workday.ID";
	private String workdayPrefOpenCheckin  = "workday.openCheckin";
	private String workdayPrefWorkedTime = "workday.workedTime";
	private String workdayPrefDailyMark = "workday.dailyMark";
	private String workdayPrefCheckins = "workday.checkinCount";
	//private String workdayPrefValid= "workday.valid";
	private String workdayPrefInitialTime = "workday.initialTime";
	
	private final String checkinPrefID = "checkin.ID.";
	//private final String checkinStatus = "checkin.status.";
	//private final String checkinCount = "checkin.count";
	private final String checkinHour = "checkin.hour.";
	private final String checkinType = "checkin.type.";
	
	private int checkinCounter;
	private CheckinListener mCheckinListener;
	
	
	public void setCheckinListener(CheckinListener checkinListener){
		mCheckinListener = checkinListener;
	}
	public Today() {
		super();
		checkinCounter = 0;
		initialTime = 0;
	}
	
	public void initData(SharedPreferences mSharedPreferences, String workdayID, int workedTime, int dailyMark) {
		setWorkdayID(Long.valueOf(workdayID));
		setHasOpenCheckin(false);
		setWorkedTime(workedTime);
		setDailyMak(dailyMark);
		initialTime = System.currentTimeMillis();
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
			initialTime = mSharedPreferences.getLong(workdayPrefInitialTime, 0);
			checkinCounter = mSharedPreferences.getInt(workdayPrefCheckins, 0);
			loadCheckins(mSharedPreferences);
		}
	}
	
	private void loadCheckins(SharedPreferences mSharedPreferences) {
		int counter = checkinCounter;
		for ( int i = 1; i <= counter; i++){
			Checkin checkin = new Checkin();
			checkin.setCheckinID(mSharedPreferences.getLong(checkinPrefID + i, 0));
			checkin.setWorkdayID(mSharedPreferences.getLong(workdayPrefID, 0));
			checkin.setTimeStamp(mSharedPreferences.getString(checkinHour + i, ""));
			checkin.setType(mSharedPreferences.getInt(checkinType + i, 0));
			
			// TODO timestamp should be long
			restoreCheckin(checkin);
		}
		
	}
	
	private void restoreCheckin(Checkin checkin) {
		super.addCheckin(checkin);
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
		editor.putLong(workdayPrefInitialTime, initialTime);
		//editor.putBoolean(workdayPrefValid, wasStarted());
		editor.commit();
		
	}

	/*public void refreshCheckinData(SharedPreferences mSharedPreferences) {
		Checkin checkin = new Checkin(mSharedPreferences.getLong(workdayPrefID, 0));
		checkin.setTimeStamp(String.valueOf(System.currentTimeMillis()));
		addCheckin(checkin);
		updateWorkdayStatus();
		storeCheckinData(mSharedPreferences, checkin);
	}*/
	
	private void storeCheckinData(SharedPreferences mSharedPreferences,
			Checkin checkin) {
		SharedPreferences.Editor editor = mSharedPreferences.edit(); 
		editor.putLong(checkinPrefID + checkinCounter, checkin.getCheckinID());
		editor.putString(checkinHour + checkinCounter, checkin.getTimeStamp());
		editor.putInt(checkinType + checkinCounter, checkin.getCheckinIntType());
		
		//editor.putBoolean(checkinStatus, hasOpenCheckin());
		//editor.putInt(checkinCount, checkinCounter);
		
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
	public int getCheckinCounter() {
		return checkinCounter;
	}
	public void saveWorkdayToDB(SharedPreferences mSharedPreferences) {
		// TODO Auto-generated method stub
		
	}
	public void computeWorkedHours() {
		// TODO Auto-generated method stub
		
	}
	public void saveCheckinsToDB(SharedPreferences mSharedPreferences) {
		// TODO Auto-generated method stub
		
	}

	

}
