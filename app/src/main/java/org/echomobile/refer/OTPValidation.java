package org.echomobile.refer;

import android.*;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import org.echomobile.refer.objects.Client;
import org.echomobile.refer.receivers.SmsReceiver;
import org.echomobile.refer.responses.AuthenticateUserResponseJSON;
import org.echomobile.refer.responses.TokenJSONResponse;

import java.util.HashMap;



public class OTPValidation extends Activity implements View.OnClickListener {
    EditText et_otp;
    Button otpBtn;
    Context context;
    TelephonyManager mTelephonyManager;
    App app;
    SmsReceiver mSmsReceiver;


    TPref pref;

    // Sent to server
    String phone;
    String otpNumber;
    String imei;


    //Permission Request SMS

    public static final int REQUEST_CODE_FOR_SMS=1;
    public static final int REQUEST_IMEI=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = App.getInstance();
        Bundle extras= getIntent().getExtras();
        if(extras !=null) {
            phone = extras.getString("phone");
            imei=extras.getString("imei");
        }
        //Register SMS Receiver
        registerReceiver(smsReceiver,new IntentFilter("SmsReceiver"));
        setContentView(R.layout.activity_otpvalidation);
        et_otp=(EditText)findViewById(R.id.et_otp);
        otpBtn=(Button)findViewById(R.id.otpBtn);
        validatePhone(phone);

        otpBtn.setOnClickListener(this);
    }

    BroadcastReceiver smsReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b=intent.getExtras();
            String otp=b.getString("otp");
            Log.e("otpreceived",""+otp);
            //verify_otp();

        }
    };

    public void validatePhone(String phone) {
        pref=new TPref(this);
        pref.setMobileNumber(phone);
        final HashMap<String, String> params = new HashMap();
        params.put("phone", phone);
        //final ProgressDialog dialog =app.rest.open_loader(this, "Signing in...", true);
        app.rest.apiPost("/api/authenticate/phone/request", params, new RestClient.HttpCallback() {

            @Override
            public void onFailure(Response response, Throwable throwable) {
                Context context = getApplicationContext();
                CharSequence text = "Authentication failed, check phone number";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            @Override
            public void onSuccess(String response) {
                AuthenticateUserResponseJSON _response = app.gson.fromJson(response, AuthenticateUserResponseJSON.class);
                Log.d(Constants.TAG, String.valueOf(_response.success));
            }
        });

    }
    private void verify_otp(){
        String otp= et_otp.getText().toString().trim();
        serverOtpCall(otp);
        if(!otp.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();

        } else{serverOtpCall(otp);}

    }
    public void serverOtpCall (String otp){
        final HashMap<String, String> params = new HashMap();
        params.put("phone", phone);
        params.put("code", otp);
        params.put("imei", imei);
        app.rest.apiPost("/api/authenticate/phone/verify", params, new RestClient.HttpCallback() {
            @Override
            public void onFailure(Response response, Throwable throwable) {
                Context context = getApplicationContext();
                CharSequence text = "Authentification failed, code received is wrong, try again";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onSuccess(String response) {
                TokenJSONResponse _response = app.gson.fromJson(response, TokenJSONResponse.class);
                login(_response.token, imei);
            }

        });

    }


    public void login(String token, String imei) {
        pref.createLogin(phone, token,imei);
      //  client.SaveToDB(this, app.dbHelper);
      //  app.client = client;
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
        finish();
    }


    public void showAbout() {
        String message = getText(R.string.about_detail).toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Echo Refer")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(true)
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onClick(View v) {
        verify_otp();

    }
}
