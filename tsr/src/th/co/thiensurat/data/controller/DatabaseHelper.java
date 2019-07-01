package th.co.thiensurat.data.controller;

import java.util.UUID;

import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHStorage.FolderType;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "tsr.db";
	private static final int DATABASE_VERSION = 16;

	public DatabaseHelper() {

		super(BHApplication.getContext(), DATABASE_NAME, BHStorage.getFolder(FolderType.Database), null, DATABASE_VERSION);

		// TODO Auto-generated constructor stub
		//setForcedUpgrade();
	}

	public String getDatabasePath(){
		return BHStorage.getFolder(FolderType.Database) + "/" + DATABASE_NAME;
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		super.onUpgrade(db, oldVersion, newVersion);
	}
}
