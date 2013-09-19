package br.com.androidzin.pontopro.model;

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
	private SharedPreferences mSharedPreferences;
	
	public void setCheckinListener(CheckinListener checkinListener){
		mCheckinListener = checkinListener;
	}
	
	public Today() {
		super();
		checkinCounter = 0;
		initialTime = 0;
	}
	
	public void initData(SharedPreferences sharedPreferences, long workdayID, int workedTime, int dailyMark) {
		setWorkdayID(workdayID);
		setHasOpenCheckin(false);
		setWorkedTime(workedTime);
		setDailyMak(dailyMark);
		initialTime = System.currentTimeMillis();
		checkinCounter = 0;
		mSharedPreferences = sharedPreferences;
	}
	 
	public void save() {
		refreshData(); 
	}
	
	public void load(SharedPreferences sharedPreferences){
		mSharedPreferences = sharedPreferences;
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
	
	private void refreshData(){
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
	
	private void storeCheckinData(Checkin checkin) {
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
		//// ENTERED, LUNCH, AFTER_LUNCH, LEAVING, ANY_ENTRANCE, ANY_LEAVING, ANY
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

	public void refreshData(Checkin checkin) {
		storeCheckinData(checkin);
		notifyListener();
	}
	
	private void notifyListener(){
		if ( mCheckinListener != null ) {
			mCheckinListener.onCheckinDone(getCheckinType(), System.currentTimeMillis(), getWorkedTime());
		}
	}
	
	public int getCheckinCounter() {
		return checkinCounter;
	}
	
	private void saveWorkdayToDB(Context context) {
		ContentValues values = PontoProContract.createWorkdayValues(getDailyMark(), getWorkedTime(), isClosed());
		context.getContentResolver().update(Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/" + getWorkdayID()), values, null, null);
	}
	
	public void computeWorkedHours() {
		//long total = 0;
		int num = 1;
		if ( isEven(getCheckinCounter()) ) {
			num = 2;
		}
		long[] intervals = new long[getCheckinCounter() - num];
		for ( int i = 0; i < (getCheckinCounter() - num ); i++) {
			if (isEven(i) ) {
				intervals[i] = getCheckinList().get(i+1).getTime() - getCheckinList().get(i).getTime();
			}
		}
		for ( int i = 0; i < intervals.length; i++) {
			workedTime += intervals[i];
		}
		
		// TODO Convert from long to int
		//workedTime = TimeUnit.MILLISECONDS.toMinutes(total);
		//workedTime = TimeUnit.MILLISECONDS.toMinutes(total);
		
	}

	private boolean isEven(int number) {
		return number % 2 != 0;
	}
	private void saveCheckinsToDB(SharedPreferences mSharedPreferences, Context context) {
		for (int i = 1; i <= mCheckinList.size(); i++) {
			ContentValues values = PontoProContract.createCheckinValues(getWorkdayID(), mSharedPreferences.getLong(checkinHour + i, 0));
			context.getContentResolver().insert(Uri.withAppendedPath(PontoProContract.CONTENT_URI, "checkin/insert"), values);
		}
		
	}
	
	public void finish(Context context) {
		computeWorkedHours();
		saveWorkdayToDB(context);
		saveCheckinsToDB(mSharedPreferences, context);
	}

	/*private void startToday() {
		
		Uri created = getActivity().getContentResolver().insert(Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/insert"), values);
		mToday.initData(mSharedPreferences, 
				created.getLastPathSegment(), 
				values.getAsInteger(PontoProContract.WORKDAY_WORKED_HOURS), 
				values.getAsInteger(PontoProContract.WORKDAY_DAILY_MARK));
	}*/
	

}
