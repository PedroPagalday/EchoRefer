package org.echomobile.refer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {
	public ActionBar actionBar;
	public AlertDialog dialog;
	App app;
    public boolean any_changes;


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		app = App.getInstance();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class PrefsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
		App app;
		Activity a;
		private PreferenceManager pm;
	    private AlertDialog change_pw_dialog;


		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
            PreferenceScreen ps = getPreferenceScreen();
			ps.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
			app = App.getInstance();
			a = getActivity();

            pm = getPreferenceManager();
			initSummary();


		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			Preference preference = findPreference(key);
            SettingsActivity a = (SettingsActivity) getActivity();

		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void initSummary() {
			PreferenceScreen ps = getPreferenceScreen();
			int cats = ps.getPreferenceCount();
			for (int i = 0; i < cats; i++) {
				PreferenceCategory pc = (PreferenceCategory) ps.getPreference(i);
				int prefs = pc.getPreferenceCount();
				for (int j = 0; j < prefs; j++) {
					Preference p = pc.getPreference(j);
					updatePrefSummary(p);
				}
			}
		}

		private void updatePrefSummary(Preference p) {
			if (p instanceof ListPreference) {
				p.setSummary(((ListPreference) p).getEntry());
			}
			if (p instanceof EditTextPreference) {
				String show;
				if (p.getKey().equals("PASSWORD")) show = "*********";
				else show = ((EditTextPreference) p).getText();
				p.setSummary(show);
			}
		}




		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onResume() {
			super.onResume();
			getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onPause() {
			super.onPause();
			getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		}
	}

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void saveSettings() {

        }

/*

	public void logout() {
        // Currently only deletes records & alarms
		// SQLite on Android doesn't support delete with LIMIT, hence _id select

    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSettings();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				saveSettings();
				finish();
				return true;
			case R.id.logout:
				TPref.cleanPreferences(app);
				Intent i = new Intent(this, AuthenticateActivity.class);
				app.startActivity(i);
				finish();
				return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


}