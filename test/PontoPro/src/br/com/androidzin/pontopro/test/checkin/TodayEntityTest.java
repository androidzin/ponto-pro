package br.com.androidzin.pontopro.test.checkin;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import android.util.Log;
import br.com.androidzin.pontopro.model.Checkin;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.model.Today;

public class TodayEntityTest extends AndroidTestCase {

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
	
	private Today today;
	private SharedPreferences mSharedPreferences;
	private long workdayID;
	private int workedTime;
	private int dailyMark;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		today = new Today();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		workdayID = 1;
		workedTime = 0;
		dailyMark = 0;
		today.initData(mSharedPreferences, workdayID, workedTime, dailyMark);
	}
	
	public void testTodayCorrectlyInited(){
		assertEquals(System.currentTimeMillis(), today.getInitialTime());
		assertEquals(0, today.getCheckinCounter());
		assertEquals(0, today.getWorkedTime());
		assertEquals(0, today.getDailyMark());
		assertEquals(0, today.getCheckinList().size());
		assertEquals(false, today.hasOpenCheckin());
	}
	
	public void testTodayCorrectlyInitData(){
		//today.initData(mSharedPreferences, workdayID, workedTime, dailyMark);

		assertEquals(workdayID, today.getWorkdayID());
		assertEquals(workedTime, today.getWorkedTime());
		assertEquals(dailyMark, today.getDailyMark());
		assertNotNull(mSharedPreferences);
	}
	
	public void testRefreshData(){
		/*SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putLong(workdayPrefID, getWorkdayID());
		editor.putBoolean(workdayPrefOpenCheckin, hasOpenCheckin());
		editor.putLong(workdayPrefWorkedTime, getWorkedTime());
		editor.putLong(workdayPrefDailyMark, getDailyMark());
		editor.putInt(workdayPrefCheckins, checkinCounter);
		editor.putLong(workdayPrefInitialTime, initialTime);
		//editor.putBoolean(workdayPrefValid, wasStarted());
		editor.commit();*/
		
		//today.initData(mSharedPreferences, workdayID, workedTime, dailyMark);
		today.save();
	
		assertEquals(workdayID, mSharedPreferences.getLong(workdayPrefID, 0));
		assertEquals(false, mSharedPreferences.getBoolean(workdayPrefOpenCheckin, true));
		assertEquals(workedTime, mSharedPreferences.getLong(workdayPrefWorkedTime, -1));
		assertEquals(dailyMark, mSharedPreferences.getLong(workdayPrefDailyMark, -1));
		assertEquals(0, mSharedPreferences.getInt(workdayPrefCheckins, -1));
		//assertEquals(0, mSharedPreferences.getLong(workdayPrefInitialTime, -1));
	}
	
	public void testLoadNoData(){
		//today.initData(mSharedPreferences, 0, workedTime, dailyMark);
		today.save();
		today.load(mSharedPreferences);
		
		assertEquals(workdayID, mSharedPreferences.getLong(workdayPrefID, 0));
	}
	
	public void testLoadData(){
		/*
		 * dailyMark = BusinessHourCommom.getWorkingTime(mSharedPreferences);
		if ( workdayID != 0 ) {
			setWorkdayID(workdayID);
			setHasOpenCheckin(mSharedPreferences.getBoolean(workdayPrefOpenCheckin, false));
			initialTime = mSharedPreferences.getLong(workdayPrefInitialTime, 0);
			checkinCounter = mSharedPreferences.getInt(workdayPrefCheckins, 0);
		 */
		
		
		//today.initData(mSharedPreferences, workdayID, workedTime, dailyMark);
		today.save();
		today.load(mSharedPreferences);
		
		assertEquals(workdayID, mSharedPreferences.getLong(workdayPrefID, 0));
		assertEquals(false, mSharedPreferences.getBoolean(workdayPrefOpenCheckin, true));
		assertEquals(workedTime, mSharedPreferences.getLong(workdayPrefWorkedTime, -1));
		assertEquals(dailyMark, mSharedPreferences.getLong(workdayPrefDailyMark, -1));
		assertEquals(0, mSharedPreferences.getInt(workdayPrefCheckins, -1));
		
	}
	
	public void testLoadDataWithCheckins(){
		/*
		 * dailyMark = BusinessHourCommom.getWorkingTime(mSharedPreferences);
		if ( workdayID != 0 ) {
			setWorkdayID(workdayID);
			setHasOpenCheckin(mSharedPreferences.getBoolean(workdayPrefOpenCheckin, false));
			initialTime = mSharedPreferences.getLong(workdayPrefInitialTime, 0);
			checkinCounter = mSharedPreferences.getInt(workdayPrefCheckins, 0);
		 */
		
		
		//today.initData(mSharedPreferences, workdayID, workedTime, dailyMark);
		today.save();
		today.load(mSharedPreferences);
		
		assertEquals(workdayID, mSharedPreferences.getLong(workdayPrefID, 0));
		assertEquals(false, mSharedPreferences.getBoolean(workdayPrefOpenCheckin, true));
		assertEquals(workedTime, mSharedPreferences.getLong(workdayPrefWorkedTime, -1));
		assertEquals(dailyMark, mSharedPreferences.getLong(workdayPrefDailyMark, -1));
		assertEquals(0, mSharedPreferences.getInt(workdayPrefCheckins, -1));
	}
	
	public void testUpdateCheckinCounter(){
		//today.initData(mSharedPreferences, workdayID, workedTime, dailyMark);
		today.updateWorkdayStatus();
		
		int checkinCounter = 1;
		
		assertEquals(checkinCounter, today.getCheckinCounter());
	}
	
	public void testStoreCheckinData(){
		/*editor.putLong(checkinPrefID + checkinCounter, checkin.getCheckinID());
		editor.putLong(checkinHour + checkinCounter, checkin.getTime());
		editor.putInt(checkinType + checkinCounter, checkin.getCheckinIntType());*/

		//today.initData(mSharedPreferences, workdayID, workedTime, dailyMark);
		
		int checkinCounter = 1;
		Checkin checkin = new Checkin(workdayID, CheckinType.ENTERED);
		today.updateWorkdayStatus();
		today.refreshData(checkin);
		
		assertEquals(CheckinType.ENTERED.ordinal(), mSharedPreferences.getInt(checkinType + checkinCounter, -1));
		assertEquals(checkinCounter, today.getCheckinCounter());
	}
	
	public void testGetCheckinType(){
		//today.initData(mSharedPreferences, workdayID, workedTime, dailyMark);
		// ENTERED, LUNCH, AFTER_LUNCH, LEAVING, ANY_ENTRANCE, ANY_LEAVING, ANY
		int checkinCounter = 0;
		CheckinType checkinValues[] = CheckinType.values();
		int length = checkinValues.length;
		
		while ( checkinCounter < length) {
			today.updateWorkdayStatus();
			assertEquals(checkinValues[checkinCounter].ordinal(), today.getCheckinType().ordinal());
			checkinCounter++;
		}
	}
	
	public void testCalculateWorkedTime(){
		long times1, times2, diff;
		times1 = System.currentTimeMillis();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		times2 = System.currentTimeMillis();
		Checkin one = new Checkin(workdayID);
		one.setTimeStamp(times1);
		Checkin two = new Checkin(workdayID);
		two.setTimeStamp(times2);
		
		diff = times2 - times1;
		
		today.addCheckin(one);
		today.addCheckin(two);
		
		today.computeWorkedHours();
		
		assertEquals(diff, today.getWorkedTime());
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		mSharedPreferences.edit().clear().commit();
		super.tearDown();
	}
	
}
