package th.co.thiensurat.data.controller;

import java.util.concurrent.atomic.AtomicInteger;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

	private AtomicInteger mOpenCounter = new AtomicInteger();

	private static DatabaseManager instance;
	private static DatabaseHelper mDatabaseHelper;
	private SQLiteDatabase mDatabase;

	public static synchronized DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
			mDatabaseHelper = new DatabaseHelper();
		}

		return instance;
	}

	public synchronized SQLiteDatabase openDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			mDatabase = mDatabaseHelper.getWritableDatabase();
		}

		return mDatabase;
	}

	public synchronized void closeDatabase() {
		if (mOpenCounter.decrementAndGet() == 0) {
			mDatabase.close();
		}
	}
	
	public synchronized void forceCloseDatabase() {
		/*if(mDatabase != null)
			mDatabase.close();*/

		if(mDatabase != null) {
			mDatabase.close();
			mOpenCounter =  new AtomicInteger();
		}
	}
	
	public synchronized String getDatabasePath() { 
		return mDatabaseHelper.getDatabasePath();
	}

	public synchronized String getDatabaseName() {
		return mDatabaseHelper.getDatabaseName();
	}

}
