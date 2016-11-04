package org.echomobile.refer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import org.echomobile.refer.objects.Client;
import org.echomobile.refer.responses.AuthenticateUserResponseJSON;
import org.echomobile.refer.responses.TokenJSONResponse;


import com.squareup.okhttp.Response;

import java.util.HashMap;

public class AuthenticateActivity extends AppCompatActivity {
	private EditText login_et;
    public App app;
	ActionBar actionbar;
	Client client;
    Button loginBtn;
    Context context;
    TelephonyManager mTelephonyManager;

    //Permission Request SMS


    public static final int REQUEST_IMEI=1;

    private TPref pref;



	// Sent to server
	String phone;
    String otpNumber;
    String imei;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authenticate);
        app = App.getInstance();
        //TPref.loadPreferences(app);
        login_et=(EditText)findViewById(R.id.phoneEt);
		if (TPref.client_phone!=null)login_et.setText(TPref.client_phone);
        loginBtn=(Button)findViewById(R.id.loginBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                phone = login_et.getText().toString();
                validateForm(phone);

                //Check if IMEI permissions have been granted before:
                int permissionCheck = ContextCompat.checkSelfPermission(AuthenticateActivity.this,
                        Manifest.permission.READ_PHONE_STATE);
                if (permissionCheck==PackageManager.PERMISSION_GRANTED){
                    imei=getDeviceImei();
                    Intent intent= new Intent (AuthenticateActivity.this, OTPValidation.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("imei",imei);
                    startActivity(intent);
                } else{
                checkIMEIpermission();
                Intent intent= new Intent (AuthenticateActivity.this, OTPValidation.class);
                intent.putExtra("phone", phone);
                startActivity(intent);}


            }
        });
        pref=new TPref(this);
        if(pref.isLoggedIn()){
            Intent intent =new Intent(AuthenticateActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }




    }
    public void validateForm(String phone){
        // phone = login_et.getText().toString();
        boolean ok = true;
        if (phone.length() < 12) {
            login_et.setError("Login too short");
        }
        if (phone.length() > 12) {
            login_et.setError("Login too long");
        }
        if (phone.length() == 0) {
            login_et.setError("Include a phone number");
        }
        if (phone.contains(" ") | phone.contains("\\W")) {
            login_et.setError("Spaces or Special characters are not allowed");
        }
        if (phone.matches("[a-zA-Z.?]*")) {
            login_et.setError("Letters are not allowed");
        }
        if (phone.contains("@")  && phone.startsWith("07")) phone=phone.replace("07", "2547");
        }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkIMEIpermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale( this, android.Manifest.permission.READ_PHONE_STATE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read Phone IMEI permission is necessary to verify your number");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_IMEI);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_IMEI);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private String getDeviceImei() {
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return mTelephonyManager.getDeviceId();
    }
    @Override

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IMEI:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    imei=getDeviceImei();
                    Intent intent =new Intent(AuthenticateActivity.this, OTPValidation.class);
                    Bundle extras = new Bundle();
                    extras.putString("imei",imei);
                    extras.putString("phone",phone);
                    intent.putExtras(extras);
                    startActivity(intent);
                }else{

                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }





	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {

        super.onStop();
	}

	@Override
	public void onResume(){
		super.onResume();
	}





} // class