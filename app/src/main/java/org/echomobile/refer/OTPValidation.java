package org.echomobile.refer;

import android.*;
import android.Manifest;
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



    TPref pref;

    // Sent to server
    String phone;
    String otpNumber;
    String imei;


    //Permission Request SMS

    public static final int REQUEST_CODE_FOR_SMS=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = App.getInstance();
        Bundle extras= getIntent().getExtras();
        if(extras !=null) {
            phone = extras.getString("phone");
            imei=extras.getString("imei");
        }

        setContentView(R.layout.activity_otpvalidation);
        et_otp=(EditText)findViewById(R.id.et_otp);
        otpBtn=(Button)findViewById(R.id.otpBtn);
        otpBtn.setOnClickListener(this);
        validatePhone(phone);
        int permissionCheck = ContextCompat.checkSelfPermission(OTPValidation.this,
                Manifest.permission.SEND_SMS);
        if(permissionCheck==PackageManager.PERMISSION_GRANTED) {
            //Register SMS Receiver
            registerReceiver(smsReceiver, new IntentFilter("SmsReceiver"));
        }
        else{
        checkSMSpermission();
        }
    }

    BroadcastReceiver mSmsReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b=intent.getExtras();
            String otp=b.getString("otp");
            Log.e("otpreceived",""+otp);
            serverOtpCall(otp);

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
            serverOtpCall(otp);


        } else{Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();}

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

    BroadcastReceiver smsReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b=intent.getExtras();
            String otp=b.getString("otp");
            Log.e("otpreceived",""+otp);
            serverOtpCall(otp);

        }
    };
    public void login(String token, String imei) {
        pref.createLogin(phone, token,imei);
      //  client.SaveToDB(this, app.dbHelper);
      //  app.client = client;
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
        finish();
    }



    @Override
    public void onClick(View v) {
        verify_otp();

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkSMSpermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_SMS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read SMS permission is necessary to verify your number, only the message with authentication code will be read");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.READ_SMS}, REQUEST_CODE_FOR_SMS);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS}, REQUEST_CODE_FOR_SMS);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_FOR_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent =new Intent(OTPValidation.this, MainActivity.class);
                    startActivity(intent);


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    verify_otp();
                    Intent intent =new Intent(OTPValidation.this, MainActivity.class);
                    startActivity(intent);
                }

            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
