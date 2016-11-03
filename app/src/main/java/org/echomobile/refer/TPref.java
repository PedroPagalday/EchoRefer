package org.echomobile.refer;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.digits.sdk.android.DigitsSession;
import com.digits.sdk.android.Digits;

import static android.content.Context.MODE_PRIVATE;

public class TPref {

	public static String logged_uid;
	public static String client_phone;
	public static String token;




	public static final String CLIENT_PHONE="CLIENT_PHONE";
	public static final String LOGGED_UID = "LOGGED_IN_UID";
	public static final String TOKEN="TOKEN";


	public static void loadPreferences( App app ) {
		SharedPreferences sp = app.getSharedPreferences("sp", MODE_PRIVATE);
		logged_uid = sp.getString(LOGGED_UID, null);
		client_phone=sp.getString(CLIENT_PHONE,"");
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
		String phone = sp.getString(CLIENT_PHONE,"");
        sp.edit().clear().commit();

        Digits.clearActiveSession();
		editor.clear();
		editor.commit();

	}
}