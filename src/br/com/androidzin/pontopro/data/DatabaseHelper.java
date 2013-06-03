package br.com.androidzin.pontopro.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "pontoPro";
	private static final int DB_VERSION = 1;
	
	private String createWorkdayTable = "create table workday (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
						+ "workDate DATE DEFAULT (datetime('now','localtime')),"
						+ "workedHours INTEGER,"
						+ "isClosed BOOLEAN,"
						+ "dailyMark INTEGER"
						+ ");";
	private String createCheckinTable = "create table checkins (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
						+ "workdayID LONG,"
						+ "checkinHour DATE DEFAULT (datetime('now','localtime')),"
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
		db.execSQL("drop table " + CHECKINS_TABLE);
		db.execSQL("drop table " + WORKDAY_TABLE);
		
		db.execSQL(createWorkdayTable);
		db.execSQL(createCheckinTable);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

}