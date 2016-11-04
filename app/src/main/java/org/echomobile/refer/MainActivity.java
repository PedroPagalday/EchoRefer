package org.echomobile.refer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private String[] mDrawerItems;
    private RelativeLayout mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
/*    public Button reg_serial_btn;
    public Button campaign_btn;*/



    App app;

    // Menu item IDs
    public static final int MENU_VEHICLES = 0;
    public static final int MENU_FEEDBACK = 1;
    public static final int MENU_DAILY_REPORT = 2;
    public static final int MENU_SETTINGS = 3;
    public static final int MENU_LOGOUT = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        app = App.getInstance();
        TPref pref;


        TPref.loadPreferences(app);
        pref=new TPref(this);
        if(!pref.isLoggedIn()){
            Intent intent =new Intent(MainActivity.this, AuthenticateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setLogo(R.mipmap.ic_launcher);


        final Button campaign_btn =(Button)findViewById(R.id.campaign_btn);


        //selectItem(goto_menu_position);
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
                finish();
                return true;
            case R.id.logout:
                TPref.cleanPreferences(app);
                Intent i = new Intent(this, AuthenticateActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openCampaigns (View view){
        Intent intent = new Intent(this, Campaign.class);
        startActivity(intent);
    }
    public void openSerials (View view) {
        Intent intent =new Intent(this, SerialRegistration.class);
        startActivity(intent);
    }


} // class