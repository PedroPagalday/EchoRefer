package org.echomobile.refer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.echomobile.refer.AuthenticateActivity;
import org.echomobile.refer.Listener.SmsListener;
import org.echomobile.refer.Manifest;

/**
 * Created by Pedro on 01/11/2016.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent)
    {

     Bundle data = intent.getExtras();
        Object[] pdus=(Object[]) data.get("pdus");
        for (int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            //Check here if the sender is the provider
            String messageBody=smsMessage.getMessageBody();
            //Pass on the text to the listener
            mListener.messageReceived(messageBody);
        }

    }
    public static void bindListener (SmsListener listener){
        mListener=listener;
    }

}

