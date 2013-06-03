package br.com.androidzin.pontopro.data.provider;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.ContentValues;

public final class PontoProContract {
	public static final String CHECKINS_ID = "_id";
	public static final String CHECKINS_TABLE = "checkins";
	public static final String CHECKINS_WORKDAY_ID = "workdayID";
	public static final String CHECKINS_CHECKIN_HOUR = "checkinHour";

	public static final String WORKDAY_TABLE = "workday";
	public static final String WORKDAY_ID = "_id";
	public static final String WORKDAY_WORK_DATE = "workDate";
	public static final String WORKDAY_WORKED_HOURS = "workedHours";
	public static final String WORKDAY_IS_CLOSED = "isClosed";
	public static final String WORKDAY_DAILY_MARK = "dailyMark";

	public static DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * @param dailyMark
	 * @param workedHours
	 * @param isClosed
	 * @return
	 */
	public static ContentValues createWorkdayValues(int dailyMark, int workedHours, boolean isClosed) {
		ContentValues values = new ContentValues();
		if ( dailyMark < 0 || workedHours < 0) 
			throw new NumberFormatException();

		values.put(WORKDAY_DAILY_MARK, dailyMark);
		//values.put(WORKDAY_WORK_DATE, System.currentTimeMillis()); - Database puts timestamp automatically
		values.put(WORKDAY_WORKED_HOURS, workedHours);
		values.put(WORKDAY_IS_CLOSED, isClosed); 
		
		return values;
	}
	
	public static ContentValues createCheckinValues(long workdayID) {
		ContentValues values = new ContentValues();
		values.put(CHECKINS_WORKDAY_ID, workdayID);
		//values.put(CHECKINS_CHECKIN_HOUR, System.currentTimeMillis()); - Database puts timestamp automatically
		return values;
	}
}
