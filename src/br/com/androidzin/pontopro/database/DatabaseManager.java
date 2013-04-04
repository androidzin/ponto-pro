package br.com.androidzin.pontopro.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.androidzin.pontopro.model.Checkin;
import br.com.androidzin.pontopro.model.Workday;

public class DatabaseManager {

	private Context context;
	private SQLiteDatabase mDataBase;

	public final String CHECKINS_ID = "_id";
	public final String CHECKINS_TABLE = "checkins";
	public final String CHECKINS_WORKDAY_ID = "workdayID";
	public final String CHECKINS_CHECKIN_HOUR = "checkinHour";
	
	public final String WORKDAY_TABLE = "workday";
	public final String WORKDAY_ID = "_id";
	public final String WORKDAY_WORK_DATE = "workDate";
	public final String WORKDAY_WORKED_HOURS = "workedHours";
	public final String WORKDAY_IS_CLOSED = "isClosed";
	public final String WORKDAY_DAILY_MARK = "dailyMark";
	
	private final String TABLE_ROW_ID = "id";
	private final String TABLE_ROW_ONE = "table_row_one";
	private final String TABLE_ROW_TWO = "table_row_two";
	

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
			value = mDataBase.insert(CHECKINS_TABLE, null, values);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return value;
	}

	private ContentValues createCheckinValues(long workdayID) {
		ContentValues values = new ContentValues();
		values.put(CHECKINS_WORKDAY_ID, workdayID);
		values.put(CHECKINS_CHECKIN_HOUR, System.currentTimeMillis());
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
			result.setTimeStamp(cursor.getLong(2));
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
				result.setTimeStamp(cursor.getLong(2));
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
		values.put(WORKDAY_WORK_DATE, System.currentTimeMillis());
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

	class DatabaseHelper extends SQLiteOpenHelper {

		private static final String DB_NAME = "pontoPro";
		private static final int DB_VERSION = 1;
		
		private String createWorkdayTable = "create table workday (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
							+ "workDate DATE,"
							+ "workedHours INTEGER,"
							+ "isClosed BOOLEAN,"
							+ "dailyMark INTEGER"
							+ ");";
		private String createCheckinTable = "create table checkins (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
							+ "workdayID LONG,"
							+ "checkinHour DATE,"
							+ "FOREIGN KEY (workdayID) REFERENCES workday(_id) ON DELETE CASCADE ON UPDATE CASCADE"
							+ ");";

		public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, DB_NAME, factory, DB_VERSION);
		}

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(createWorkdayTable);
			db.execSQL(createCheckinTable);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			if (!db.isReadOnly()) {
				db.execSQL("PRAGMA foreign_keys=ON;");
			}
		}

	}

}
