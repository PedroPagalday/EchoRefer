package org.echomobile.refer.objects;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import org.echomobile.refer.Constants;
import org.echomobile.refer.contentprovider.MyProvider;
import org.echomobile.refer.helper.DatabaseHelper;

public class Client {
	public String id;
	public String phone;
	public String token;
	public String imei;


	public static final String COL_ID="_id";
	public static final String COL_PHONE="ClientPhone";
	public static final String COL_TOKEN_OTP="TokenOtp";
	public static final String COL_IMEI="Imei";




	public static final String[] ALL_COLS = {COL_ID,  COL_PHONE, COL_TOKEN_OTP,COL_IMEI};

	public static final String TABLE = "Clients";
	public Client(String s_phone, String s_token,String s_imei) {

		phone = s_phone;
		token=s_token;
		imei=s_imei;

	}
	public Client(String s_phone) {
		phone = s_phone;

	}


	/* Static Methods */

	public static void CreateTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE "+TABLE+" ("+COL_ID+" VARCHAR PRIMARY KEY, "+
				COL_PHONE+ " NVARCHAR, "+
				COL_IMEI+ " NVARCHAR, "+
				COL_TOKEN_OTP+ " NVARCHAR)";
		db.execSQL(sql);
		Log.d("MYLOG",sql);
	}

	public Client (Cursor c) {
		id = c.getString(c.getColumnIndexOrThrow(COL_ID));
		phone = c.getString(c.getColumnIndexOrThrow(COL_PHONE));
		token=c.getString(c.getColumnIndexOrThrow(COL_TOKEN_OTP));
		imei=c.getString(c.getColumnIndexOrThrow(COL_IMEI));

	};

	public void SaveToDB(Activity a, DatabaseHelper dbh) {
		ContentValues cv = new ContentValues();
		cv.put(COL_ID, this.id);
		cv.put(COL_PHONE, this.phone);
		cv.put(COL_TOKEN_OTP,this.token);
		cv.put(COL_IMEI,this.imei);

		a.getContentResolver().update(MyProvider.CLIENT_CONTENT_URI, cv, null, null);
		if (Constants.verbose) Log.d(Constants.TAG,"Saving Client [ " + phone + " ]");
	}
	public static Client GetByID(Context cx, String id) {
		Client client = null;
		Cursor c = cx.getContentResolver().query(Uri.withAppendedPath(MyProvider.CLIENT_CONTENT_URI, id),
				ALL_COLS, null, null, null);

		if (c.moveToFirst()) {
			client = new Client(c);
		}
		c.close();
		return client;
	}

	public static Client GetByPhone(Context cx, String phone, String token) {
		Client client = null;
		String where = COL_PHONE + "=?";
		Cursor c = cx.getContentResolver().query(Uri.withAppendedPath(MyProvider.CLIENT_CONTENT_URI, token), ALL_COLS, where, new String[] {phone}, null);
		if (c.moveToFirst()) client = new Client(c);
		c.close();
		if (client == null) client = new Client(phone);
		return client;
	}

	public void Delete(Activity a) {
		a.getContentResolver().delete(MyProvider.CLIENT_CONTENT_URI, Client.COL_ID + "=?", new String[]{String.valueOf(id)});
	}

}
