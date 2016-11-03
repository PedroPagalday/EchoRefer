package org.echomobile.refer.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.echomobile.refer.App;
import org.echomobile.refer.Constants;
import org.echomobile.refer.objects.Client;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper mInstance = null;
	static final String dbName="matatu_tracker_db";
	
	public SQLiteDatabase mdb;
	public App app;

	public DatabaseHelper(Context context) {
		super(context, dbName, null,Constants.dbVersion);
		Log.d(Constants.TAG,"Initializing dbHelper");
		mdb = getWritableDatabase();
	}

	public static DatabaseHelper getInstance(Context ctx) {
	    if (mInstance == null) {
	      mInstance = new DatabaseHelper(ctx.getApplicationContext());
	    }
	    return mInstance;
	  }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		app = App.getInstance();
		Log.d(Constants.TAG, "Creating tables..");
		//Vehicle.CreateTable(db);
		//Alarm.CreateTable(db);
		Client.CreateTable(db);
		//SensorRecord.CreateTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(Constants.TAG, "db onUpgrade " + String.valueOf(oldVersion) + "->" + String.valueOf(newVersion));
		int upgradeTo = oldVersion + 1;
		while (upgradeTo <= newVersion) {
			Log.d(Constants.TAG,"Running V"+String.valueOf(upgradeTo)+" updates.");
			try {
				switch (upgradeTo) {
					case 27:
						eraseAll(db);
						break;
				}
			} catch (Exception e) {
				Log.e(Constants.TAG,"This update failed. " + e.toString());
			}
			upgradeTo++;
		}
	}

	public void eraseAll(SQLiteDatabase db) {
		//dropTable(db, Vehicle.TABLE);
		dropTable(db, Client.TABLE);
		//dropTable(db, Alarm.TABLE);
		//dropTable(db, SensorRecord.TABLE);
		onCreate(db);
	}
	
	void dropTable(SQLiteDatabase db, String table) {
		String sql = "DROP TABLE IF EXISTS "+table;
        Log.d(Constants.TAG, sql);
		db.execSQL(sql);
	}

}
