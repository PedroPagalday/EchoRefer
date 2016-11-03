package org.echomobile.refer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.google.gson.Gson;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import org.echomobile.refer.helper.DatabaseHelper;
import org.echomobile.refer.objects.Client;
import org.echomobile.refer.receivers.BootReceiver;

import io.fabric.sdk.android.Fabric;

public class App extends MultiDexApplication {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "savUsuOGxj03hMjBhDmTizACe";
    private static final String TWITTER_SECRET = "NSdsXDkVhs0fax15jAgT7Amr027CpuymKqOZXxw95dY9eDmvrT";

	public ConnectivityManager cMgr;
	public NetworkInfo netInfo;
	public TelephonyManager tMgr;
	public RestClient rest;
	private static App sInstance;
	public DatabaseHelper dbHelper;

	// State
	public Client client;
	public Gson gson;

	public static App getInstance() {
		return sInstance;
	}

	@Override
	public void onCreate() {
		try {
			TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
			Fabric.with(this, new Crashlytics(), new TwitterCore(authConfig), new Digits.Builder().build());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(Constants.TAG, "Creating App.");
		sInstance = this;
		sInstance.initializeInstance();
		super.onCreate();
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(App.this);
	}
	protected void initializeInstance() {
		dbHelper = DatabaseHelper.getInstance(this);
		cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		TPref.loadPreferences(sInstance);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		gson = new Gson();
		client = loggedInUser(getApplicationContext());
		rest = new RestClient(this);

	}

	public boolean isNetworkAvailable() {
		NetworkInfo activeNetworkInfo = cMgr.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public Client loggedInUser(Context mContext) {
		Client u = null;
		TPref.loadPreferences(this);
		String uid = TPref.logged_uid;
		Log.d(Constants.TAG, "U id: " + String.valueOf(uid));
		if (uid != null) {
			u = Client.GetByID(mContext, uid);
			if (u!=null) Log.d(Constants.TAG, u.toString());
			return u;
		}
		return null;
	}

	public void logout() {
		TPref.setStringPreference(this, TPref.LOGGED_UID, null);
		client = null;
	}

	public boolean wifiAvailable() {
		WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		return wifi.isWifiEnabled();
	}

	public boolean isNetUp() {
		netInfo = cMgr.getActiveNetworkInfo();
		if (netInfo == null) return false;
		if (netInfo.isAvailable()) return true;
		else {
			Toast.makeText(this, "No Internet!", Toast.LENGTH_LONG).show();
			return false;
		}
	}

}