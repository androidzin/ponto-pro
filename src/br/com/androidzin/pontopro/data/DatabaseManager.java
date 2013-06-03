package br.com.androidzin.pontopro.data;

import static br.com.androidzin.pontopro.data.provider.PontoProContract.CHECKINS_CHECKIN_HOUR;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.CHECKINS_ID;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.CHECKINS_TABLE;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.CHECKINS_WORKDAY_ID;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.WORKDAY_DAILY_MARK;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.WORKDAY_ID;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.WORKDAY_IS_CLOSED;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.WORKDAY_TABLE;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.WORKDAY_WORKED_HOURS;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.WORKDAY_WORK_DATE;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.androidzin.pontopro.data.provider.PontoProContract;
import br.com.androidzin.pontopro.exception.InvalidDateOrder;
import br.com.androidzin.pontopro.model.Checkin;
import br.com.androidzin.pontopro.model.Workday;

public class DatabaseManager {

	private Context context;
	private SQLiteDatabase mDataBase;
	private static DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	public DatabaseManager(Context context) {
		this.context = context;
		DatabaseHelper helper = new DatabaseHelper(context);
		this.mDataBase = helper.getWritableDatabase();
	}
	
	/**
	 * Close this database connection
	 */
	public void close() {
		if (mDataBase.isOpen()) {
			mDataBase.close();
		}
	}
	
	/**
	 * Insert a new row into checkin table pointing to the specified workday
	 * @param workdayID
	 * @return the row ID of the newly inserted row, or -1 if failure
	 */
	public long addCheckin(long workdayID){
		long value = -1;
		ContentValues values = createCheckinValues(workdayID);
		try {
			value = mDataBase.insert(PontoProContract.CHECKINS_TABLE, null, values);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return value;
	}

	private ContentValues createCheckinValues(long workdayID) {
		ContentValues values = new ContentValues();
		values.put(PontoProContract.CHECKINS_WORKDAY_ID, workdayID);
		//values.put(CHECKINS_CHECKIN_HOUR, System.currentTimeMillis());
		return values;
	}
	
	/**
	 * Removes the specified Checkin from database
	 * @param checkinID
	 * @return the number of affected rows affected if a where clause is passed
	 */
	public int deleteCheckin(long checkinID){
		int value = mDataBase.delete(CHECKINS_TABLE, CHECKINS_ID + "=" + checkinID, null); 
		return value;
	}
	
	/**
	 * @param checkinID
	 * @return the Checkin object containing all it's data
	 */
	public Checkin getCheckinData(long checkinID){
		Checkin result = null;
		Cursor cursor;
		
		cursor = mDataBase.query(CHECKINS_TABLE, 
								 new String [] {CHECKINS_ID, CHECKINS_WORKDAY_ID, CHECKINS_CHECKIN_HOUR}, 
								 CHECKINS_ID + "=" + checkinID, null, null, null, null);
		
		cursor.moveToFirst();
		
		if ( !cursor.isAfterLast() ) {
			result = new Checkin();
			result.setCheckinID(cursor.getLong(0));
			result.setWorkdayID(cursor.getLong(1));
			result.setTimeStamp(cursor.getString(2));
		}
		return result;
	}
	
	/**
	 * @param workdayID
	 * @return A List with all checkins related to the specified workday
	 */
	public List<Checkin> getCheckinListFromWorkday(long workdayID) {
		Checkin result = null;
		ArrayList<Checkin> checkinList = new ArrayList<Checkin>();
		Cursor cursor;
		cursor = mDataBase.query(CHECKINS_TABLE, 
								 new String[] {CHECKINS_ID, CHECKINS_WORKDAY_ID, CHECKINS_CHECKIN_HOUR}, 
								 CHECKINS_WORKDAY_ID + "=" + workdayID, 
								 null, null, null, null);
		
		cursor.moveToFirst();
		if ( !cursor.isAfterLast()) {
			do {
				result = new Checkin();
				result.setCheckinID(cursor.getLong(0));
				result.setWorkdayID(cursor.getLong(1));
				result.setTimeStamp(cursor.getString(2));
				checkinList.add(result);
			} while ( cursor.moveToNext() );
		}
		
		return checkinList;
	}
	
	
	/**
	 * ISO Date Format
	 * 	yyyy-MM-dd HH:mm:ss
	 * @param ISOdate
	 * @return A List with all checkins from the specific workday in ISO Date
	 */
	public List<Checkin> getCheckinListFromWorkday(DateTime ISOdate) {
		String today = parser.print(ISOdate);
		Checkin result = null;
		ArrayList<Checkin> checkinList = new ArrayList<Checkin>();
		Cursor cursor;
		cursor = mDataBase.rawQuery("select * from " + CHECKINS_TABLE + " where " + CHECKINS_CHECKIN_HOUR + "=" + "'" + today + "'", null);
		cursor.moveToFirst();
		if ( !cursor.isAfterLast()) {
			do {
				result = new Checkin();
				result.setCheckinID(cursor.getLong(0));
				result.setWorkdayID(cursor.getLong(1));
				result.setTimeStamp(cursor.getString(2));
				checkinList.add(result);
			} while ( cursor.moveToNext() );
		}
		
		return checkinList;
	}
	
	/**
	 * @param ISOdateFrom
	 * @param ISOdateTo
	 * @return A List with all checkins from the specific workday in ISO Date
	 * @throws Exception
	 */
	public List<Checkin> getCheckinListFromPeriod(DateTime ISOdateFrom, DateTime ISOdateTo) throws InvalidDateOrder {
		if ( ISOdateFrom.isAfter(ISOdateTo)) {
			throw new InvalidDateOrder();
		}
		String from = parser.print(ISOdateFrom);
		String to = parser.print(ISOdateTo);
		Checkin result = null;
		ArrayList<Checkin> checkinList = new ArrayList<Checkin>();
		Cursor cursor;
		cursor = mDataBase.rawQuery("select * from " + CHECKINS_TABLE + " where " 
									+ CHECKINS_CHECKIN_HOUR + ">=" + "'" + from + "'" + " AND "
									+ CHECKINS_CHECKIN_HOUR + "<=" + "'" + to + "'", null);
		cursor.moveToFirst();
		if ( !cursor.isAfterLast()) {
			do {
				result = new Checkin();
				result.setCheckinID(cursor.getLong(0));
				result.setWorkdayID(cursor.getLong(1));
				result.setTimeStamp(cursor.getString(2));
				checkinList.add(result);
			} while ( cursor.moveToNext() );
		}
		
		return checkinList;
	}

	/**
	 * Insert a new row into Workday table
	 * Put's 0 for workedHours, dailyMark and false for isClosed.
	 * @return the row ID of the newly inserted row, or -1 if failure
	 */
	public long addWorkday(){
		long value = -1;
		ContentValues values = createWorkdayValues(0, 0, false);
		try {
			value = mDataBase.insert(WORKDAY_TABLE, null, values);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * Insert a new row into Workday table
	 * Put's false for isClosed.
	 * @param dailyMark
	 *  The goal for the day (in minutes)
	 * @param workedTime
	 * 	The already worked time (in minutes)
	 * @return the row ID of the newly inserted row, or -1 if failure
	 */
	public long addWorkday(int dailyMark, int workedTime){
		long value = -1;
		ContentValues values = createWorkdayValues(dailyMark, workedTime, false);
		try {
			value =  mDataBase.insert(WORKDAY_TABLE, null, values);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * Insert a new row into Workday table
	 * Put's false for isClosed.
	 * @param Workday
	 * Get dailyMark and workedTime from workday instance
	 * @return the row ID of the newly inserted row, or -1 if failure
	 */
	public long addWorkday(Workday workday) {
		long value = -1;
		ContentValues values = createWorkdayValues(workday.getDailyMark(), workday.getWorkedTime(), false);
		value = mDataBase.insert(WORKDAY_TABLE, null, values);
		return value;
	}
	
	/**
	 * @param dailyMark
	 * @param workedHours
	 * @param isClosed
	 * @return
	 */
	public ContentValues createWorkdayValues(int dailyMark, int workedHours, boolean isClosed) {
		ContentValues values = new ContentValues();
		if ( dailyMark < 0 || workedHours < 0) 
			throw new NumberFormatException();

		values.put(WORKDAY_DAILY_MARK, dailyMark);
		//values.put(WORKDAY_WORK_DATE, System.currentTimeMillis());
		values.put(WORKDAY_WORKED_HOURS, workedHours);
		values.put(WORKDAY_IS_CLOSED, isClosed); 
		
		return values;
	}
	
	/**
	 * Removes the specified Workday from database
	 * By Cascade, removes the associated Checkins
	 * @param workDay
	 * @return the number of affected rows affected if a where clause is passed
	 */
	public int deleteWorkday(long workdayID) {
		int value = mDataBase.delete(WORKDAY_TABLE, WORKDAY_ID + "=" + workdayID, null);
		return value;
	}
	
	/**
	 * @param workdayID
	 * @return Workday object with all values
	 */
	public Workday getWorkday(long workdayID){
		Workday result = null;
		Cursor cursor;
		List<Checkin> checkinList = getCheckinListFromWorkday(workdayID);
		
		cursor = mDataBase.query(WORKDAY_TABLE, new String [] {WORKDAY_WORK_DATE, WORKDAY_WORKED_HOURS, WORKDAY_DAILY_MARK, WORKDAY_IS_CLOSED}, 
				WORKDAY_ID + "=" + workdayID, null, null, null, null);
		
		cursor.moveToFirst();
		
		if ( !cursor.isAfterLast() ) {
			result = new Workday();
			result.setDailyMark(cursor.getInt(2));
			result.setHasOpenCheckin(cursor.getInt(3) != 0);
			result.setWorkedTime(cursor.getInt(1));
			result.setCheckinList(checkinList);
		}
		return result;
	}
	
	/**
	 * @param ISOdateFrom
	 * @param ISOdateTo
	 * @return A List with all workdays from the specific workday in ISO Date
	 * @throws Exception
	 */
	public List<Workday> getWorkdayListFromPeriod(DateTime ISOdateFrom, DateTime ISOdateTo) throws InvalidDateOrder {
		if ( ISOdateFrom.isAfter(ISOdateTo)) {
			throw new InvalidDateOrder();
		}
		String from = parser.print(ISOdateFrom);
		String to = parser.print(ISOdateTo);
		Workday result = null;
		ArrayList<Workday> checkinList = new ArrayList<Workday>();
		Cursor cursor;
		cursor = mDataBase.rawQuery("select * from " + WORKDAY_TABLE + " where " 
									+ WORKDAY_WORK_DATE + ">=" + "'" + from + "'" + " AND "
									+ WORKDAY_WORK_DATE + "<=" + "'" + to + "'", null);
		cursor.moveToFirst();
		if ( !cursor.isAfterLast()) {
			do {
				result = new Workday();
				result.setWorkdayID(cursor.getLong(0));
				result.setTimeStamp(cursor.getString(1));	
				result.setWorkedHours(cursor.getInt(2));
				result.setClosed(cursor.getInt(3));
				result.setDailyMak(cursor.getInt(4));
				checkinList.add(result);
			} while ( cursor.moveToNext() );
		}
		
		return checkinList;
	}


	



}
