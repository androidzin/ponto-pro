package br.com.androidzin.pontopro.model;

import java.util.concurrent.TimeUnit;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import br.com.androidzin.pontopro.data.provider.PontoProContract;
import br.com.androidzin.pontopro.model.Checkin.CheckinListener;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;

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
		dailyMark = BusinessHourCommom.getWorkingTime(mSharedPreferences);
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
			checkin.setTimeStamp(mSharedPreferences.getLong(checkinHour + i, 0));
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
		editor.putLong(workdayPrefWorkedTime, getWorkedTime());
		editor.putLong(workdayPrefDailyMark, getDailyMark());
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
		editor.putLong(checkinHour + checkinCounter, checkin.getTime());
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
		
		default:
			return null;
		}
	}

	public void refreshData(SharedPreferences mSharedPreferences,
			Checkin checkin) {
		storeCheckinData(mSharedPreferences, checkin);
		notifyListener();
	}
	
	private void notifyListener(){
		if ( mCheckinListener != null ) {
			mCheckinListener.onCheckinDone(getCheckinType(), System.currentTimeMillis(), getWorkedTime(), getDailyMark());
		}
	}
	
	public int getCheckinCounter() {
		return checkinCounter;
	}
	
	public void saveWorkdayToDB(Context context) {
		ContentValues values = PontoProContract.createWorkdayValues(getDailyMark(), getWorkedTime(), isClosed());
		context.getContentResolver().update(Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/" + getWorkdayID()), values, null, null);
	}
	
	public void computeWorkedHours() {
		long total = 0;
		int num = 1;
		if ( getCheckinCounter() % 2 != 0 ) {
			num = 2;
		}
		long[] times = new long[getCheckinCounter() - num];
		for ( int i = 0; i < (getCheckinCounter() - num ); i++) {
			if ( i % 2 == 0 ) {
				times[i] = getCheckinList().get(i+1).getTime() - getCheckinList().get(i).getTime();
			}
		}
		for ( int i = 0; i < times.length; i++) {
			total += times[i];
		}
		
		// TODO Convert from long to int
		//workedTime = TimeUnit.MILLISECONDS.toMinutes(total);
		workedTime = TimeUnit.MILLISECONDS.toMinutes(total);
		
	}
	public void saveCheckinsToDB(SharedPreferences mSharedPreferences, Context context) {
		for (int i = 1; i <= mCheckinList.size(); i++) {
			ContentValues values = PontoProContract.createCheckinValues(getWorkdayID(), mSharedPreferences.getLong(checkinHour + i, 0));
			context.getContentResolver().insert(Uri.withAppendedPath(PontoProContract.CONTENT_URI, "checkin/insert"), values);
		}
		
	}

	/*private void startToday() {
		
		Uri created = getActivity().getContentResolver().insert(Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/insert"), values);
		mToday.initData(mSharedPreferences, 
				created.getLastPathSegment(), 
				values.getAsInteger(PontoProContract.WORKDAY_WORKED_HOURS), 
				values.getAsInteger(PontoProContract.WORKDAY_DAILY_MARK));
	}*/
	

}
