package br.com.androidzin.pontopro.data;

import static br.com.androidzin.pontopro.data.provider.PontoProContract.CHECKINS_TABLE;
import static br.com.androidzin.pontopro.data.provider.PontoProContract.WORKDAY_TABLE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "pontoPro";
	private static final int DB_VERSION = 1;
	private Context mContext;

	private String createWorkdayTable = "create table workday (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "workDate DATE DEFAULT (datetime('now','localtime')),"
			+ "workedHours INTEGER,"
			+ "isClosed BOOLEAN,"
			+ "dailyMark INTEGER" + ");";
	/*private String createCheckinTable = "create table checkins (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "workdayID LONG,"
			+ "checkinHour DATE DEFAULT (datetime('now','localtime')),"
			+ "FOREIGN KEY (workdayID) REFERENCES workday(_id) ON DELETE CASCADE ON UPDATE CASCADE"
			+ ");";*/
	
	private String createCheckinTable = "create table checkins (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "workdayID LONG,"
			+ "checkinHour LONG,"
			+ "FOREIGN KEY (workdayID) REFERENCES workday(_id) ON DELETE CASCADE ON UPDATE CASCADE"
			+ ");";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, factory, DB_VERSION);
		mContext = context;
	}

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createWorkdayTable);
		db.execSQL(createCheckinTable);
		
		//insertFromFile(db, mContext);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("drop table " + CHECKINS_TABLE);
			db.execSQL("drop table " + WORKDAY_TABLE);

			db.execSQL(createWorkdayTable);
			db.execSQL(createCheckinTable);
		}
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	/**
	 * Load a sql file from assets and execute it's inserts to fill database 
	 * @param db
	 * @param context
	 * @return
	 */
	public int insertFromFile(SQLiteDatabase db, Context context) {
		int result = 0;

		AssetManager am = context.getAssets();
		InputStream is;
		try {
			is = am.open("pontodata.sql");
			BufferedReader insertReader = new BufferedReader(new InputStreamReader(is));
			// Iterate through lines (assuming each insert has its own line and
			// theres no other stuff)
			while (insertReader.ready()) {
				String insertStmt = insertReader.readLine();
				db.execSQL(insertStmt);
				result++;
			}
			insertReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}