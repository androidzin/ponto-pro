package br.com.androidzin.pontopro.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DatabaseManager {

	private Context context;
	private SQLiteDatabase mDataBase;

	private final String CHECKINS_ID = "_id";
	private final String CHECKINS_TABLE = "checkins";
	private final String CHECKINS_WORKDAY_ID = "workdayID";
	private final String CHECKINS_CHECKIN_HOUR = "checkinHour";
	
	private final String WORKDAY_TABLE = "workday";
	private final String WORKDAY_ID = "_id";
	private final String WORKDAY_WORK_DATE = "workDate";
	private final String WORKDAY_WORKED_HOURS = "workedHours";
	private final String WORKDAY_IS_CLOSED = "isClosed";
	private final String WORKDAY_DAILY_MARK = "dailyMark";
	
	private final String TABLE_ROW_ID = "id";
	private final String TABLE_ROW_ONE = "table_row_one";
	private final String TABLE_ROW_TWO = "table_row_two";
	

	public DatabaseManager(Context context) {
		this.context = context;

		// create or open the database
		DatabaseHelper helper = new DatabaseHelper(context);
		this.mDataBase = helper.getWritableDatabase();
	}
	
	/**
	 * Insert a new row into checkin table
	 * @param workdayID
	 * @return the row ID of the newly inserted row, or -1 if failure
	 */
	public long addCheckin(int workdayID){
		long value = -1;
		ContentValues values = new ContentValues();
		values.put(CHECKINS_WORKDAY_ID, workdayID);
		values.put(CHECKINS_CHECKIN_HOUR, System.currentTimeMillis());

		try {
			value = mDataBase.insert(CHECKINS_TABLE, null, values);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * @param workDay
	 * @return the number of affected rows affected if a where clause is passed
	 */
	public int deleteCheckin(int workDay){
		int value = mDataBase.delete(CHECKINS_TABLE, CHECKINS_WORKDAY_ID + "=" + workDay, null); 
		return value;
	}
	
	/**
	 * Insert a new row into workday table
	 * @param dailyMark
	 * @return the row ID of the newly inserted row, or -1 if failure
	 */
	public long addWorkday(int dailyMark){
		long value = -1;
		ContentValues values = new ContentValues();
		values.put(WORKDAY_DAILY_MARK, dailyMark);
		values.put(WORKDAY_WORK_DATE, System.currentTimeMillis());
		values.put(WORKDAY_WORKED_HOURS, 0);
		values.put(WORKDAY_IS_CLOSED, 0);

		try {
			value =  mDataBase.insert(WORKDAY_TABLE, null, values);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return value;
	}
	
	public int deleteWorkday(long id) {
		int value = mDataBase.delete(WORKDAY_TABLE, WORKDAY_ID + "=" + id, null);
		return value;
	}

	/**********************************************************************
	 * UPDATING A ROW IN THE DATABASE TABLE
	 * 
	 * This is an example of how to update a row in the database table using
	 * this class. You should edit this method to suit your needs.
	 * 
	 * @param rowID
	 *            the SQLite database identifier for the row to update.
	 * @param rowStringOne
	 *            the new value for the row's first column
	 * @param rowStringTwo
	 *            the new value for the row's second column
	 */
	public void updateRow(long rowID, String rowStringOne, String rowStringTwo) {
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues();
		values.put(TABLE_ROW_ONE, rowStringOne);
		values.put(TABLE_ROW_TWO, rowStringTwo);

		// ask the database object to update the database row of given rowID
		try {
			mDataBase.update("", values, TABLE_ROW_ID + "=" + rowID, null);
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}

	/**********************************************************************
	 * RETRIEVING A ROW FROM THE DATABASE TABLE
	 * 
	 * This is an example of how to retrieve a row from a database table using
	 * this class. You should edit this method to suit your needs.
	 * 
	 * @param rowID
	 *            the id of the row to retrieve
	 * @return an array containing the data from the row
	 */
	public ArrayList<Object> getRowAsArray(long rowID) {
		// create an array list to store data from the database row.
		// I would recommend creating a JavaBean compliant object
		// to store this data instead. That way you can ensure
		// data types are correct.
		ArrayList<Object> rowArray = new ArrayList<Object>();
		Cursor cursor;

		try {
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			cursor = mDataBase.query("", new String[] { TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO }, TABLE_ROW_ID
					+ "=" + rowID, null, null, null, null, null);

			// move the pointer to position zero in the cursor.
			cursor.moveToFirst();

			// if there is data available after the cursor's pointer, add
			// it to the ArrayList that will be returned by the method.
			if (!cursor.isAfterLast()) {
				do {
					rowArray.add(cursor.getLong(0));
					rowArray.add(cursor.getString(1));
					rowArray.add(cursor.getString(2));
				} while (cursor.moveToNext());
			}

			// let java know that you are through with the cursor.
			cursor.close();
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList containing the given row from the database.
		return rowArray;
	}

	/**********************************************************************
	 * RETRIEVING ALL ROWS FROM THE DATABASE TABLE
	 * 
	 * This is an example of how to retrieve all data from a database table
	 * using this class. You should edit this method to suit your needs.
	 * 
	 * the key is automatically assigned by the database
	 */

	public ArrayList<ArrayList<Object>> getAllRowsAsArrays() {
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

		// this is a database call that creates a "cursor" object.
		// the cursor object store the information collected from the
		// database and is used to iterate through the data.
		Cursor cursor;

		try {
			// ask the database object to create the cursor.
			cursor = mDataBase.query("", new String[] { TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO }, null, null,
					null, null, null);

			// move the cursor's pointer to position zero.
			cursor.moveToFirst();

			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getLong(0));
					dataList.add(cursor.getString(1));
					dataList.add(cursor.getString(2));

					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList that holds the data collected from
		// the database.
		return dataArrays;
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
							+ "workdayID INTEGER,"
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

	public void close() {
		if (mDataBase.isOpen()) {
			mDataBase.close();
		}
	}
}
