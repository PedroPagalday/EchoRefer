package org.echomobile.refer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.echomobile.refer.App;
import org.echomobile.refer.TPref;

public class BootReceiver extends BroadcastReceiver {
    App app;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            app = App.getInstance();
            TPref.loadPreferences(app);
           // if (app != null && TPref.nsv_reminder) app.scheduleAlarm(TPref.nsv_reminder_time);
        }
    }
}