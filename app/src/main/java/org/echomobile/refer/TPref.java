package org.echomobile.refer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.digits.sdk.android.Digits;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class TPref {
	//Shared Preferences
	SharedPreferences pref;
	//Editor for Shared preferences
	SharedPreferences.Editor editor;
	//Context
	Context context;
	//Shared pref mode
	int PRIVATE_MODE=0;
	//Shared preferences file name
	private static final String PREF_NAME="EchoRefer";

	// All Shared Preferences Keys

	public static final String KEY_IS_WAITING_FOR_SMS ="IsWaitingForSMS";
	public static final String KEY_MOBILE_NUMBER="mobile_number";
	public static final String KEY_IS_LOGGED_IN="isLoggedIn";
	public static final String TOKEN="token";
	public static final String IMEI="imei";
	public static final String PHONE="phone";

	public static String logged_uid;
	public static String client_phone;
	public static String token;

	public TPref (Context context){
		this.context = context;
		pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setIsWaitingForSms(boolean isWaiting){
		editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
		editor.commit();
	}

	public boolean isWaitingForSms(){
		return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
	}
	public void setMobileNumber(String mobileNumber) {
		editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
		editor.commit();
	}

	public String getMobileNumber() {
		return pref.getString(KEY_MOBILE_NUMBER, null);
	}

	public void createLogin( String phone, String token, String imei) {
		editor.putString(PHONE, phone);
		editor.putString(TOKEN, token);
		editor.putString(IMEI, imei);
		editor.putBoolean(KEY_IS_LOGGED_IN, true);
		editor.commit();
	}

	public boolean isLoggedIn() {
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}

	public void clearSession() {
		editor.clear();
		editor.commit();
	}

	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> profile = new HashMap<>();
		profile.put("phone", pref.getString(PHONE, null));
		profile.put("token", pref.getString(TOKEN, null));
		profile.put("imei", pref.getString(IMEI, null));
		return profile;
	}







	public static void loadPreferences( App app ) {
		SharedPreferences sp = app.getSharedPreferences("sp", MODE_PRIVATE);
		logged_uid = sp.getString(KEY_IS_LOGGED_IN, null);
		client_phone=sp.getString(KEY_MOBILE_NUMBER,"");
		token=sp.getString(TOKEN,"");

	}

	public static void setStringPreference(App app, String key, String val) {
        SharedPreferences sp = app.getSharedPreferences("sp", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.putString(key,val);
		edit.commit();
	}

	public static void setIntPreference(App app, String key, int val) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(app);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, val);
		editor.commit();
	}

	public static void setBooleanPreference(App app, String key, boolean val) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(app);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, val);
		editor.commit();
	}
	public static void cleanPreferences (App app){
		//loadPreferences(app);
		SharedPreferences sp= app.getSharedPreferences("sp", MODE_PRIVATE);
		SharedPreferences.Editor editor=sp.edit();
		String phone = sp.getString(KEY_MOBILE_NUMBER,"");
        sp.edit().clear().commit();

        Digits.clearActiveSession();
		editor.clear();
		editor.commit();

	}
}