package org.echomobile.refer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.echomobile.refer.AuthenticateActivity;
import org.echomobile.refer.Constants;
import org.echomobile.refer.Listener.SmsListener;
import org.echomobile.refer.Manifest;
import org.echomobile.refer.service.HttpService;

/**
 * Created by Pedro on 01/11/2016.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG= SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.contains(Constants.SMS_ORIGIN)) {
                        return;
                    }

                    // verification code from sms
                    String verificationCode = getVerificationCode(message);

                    Log.e(TAG, "OTP received: " + verificationCode);

                    Intent codeintent = new Intent("SmsReceiver");
                    codeintent.putExtra("otp", verificationCode);
                    context.sendBroadcast(codeintent);
                }

            }
        }catch (Exception e) {Log.e(TAG,"Exception: "+e.getMessage());}
    }

    // Get the OTP from the sms body
    private String getVerificationCode (String message){

        String code = message.replaceAll("[^\\d.]", "");
        return code;
    }


}

