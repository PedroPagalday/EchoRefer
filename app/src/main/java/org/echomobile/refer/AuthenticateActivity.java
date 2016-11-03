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

import org.echomobile.refer.Listener.SmsListener;
import org.echomobile.refer.objects.Client;
import org.echomobile.refer.receivers.SmsReceiver;
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

    public static final int REQUEST_CODE_FOR_SMS=1;
    public static final int REQUEST_IMEI=2;

	// Sent to server
	String phone;
    String otpNumber;
    String imei;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authenticate);
        app = App.getInstance();
        TPref.loadPreferences(app);
        login_et=(EditText)findViewById(R.id.phoneEt);
		if (TPref.client_phone!=null)login_et.setText(TPref.client_phone);
        loginBtn=(Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                boolean result= checkSMSpermission();
               // boolean result2=checkIMEIpermission();
                if(result) {
                    doAuthenticate();

                }
            }

        });

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text",messageText);
                String otpNumber= messageText.replaceAll("[^0-9]", "");
                boolean permit= checkIMEIpermission();
                if(permit) {
                    verify_otp(otpNumber);
                }
            }

        });

    }

    private void verify_otp(String otp){
        final HashMap<String, String> params = new HashMap();
        params.put("phone", phone);
        params.put("code", otpNumber);
        params.put("imei", getDeviceImei());
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

    public void doAuthenticate() {
        phone = login_et.getText().toString();
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
        if (phone.indexOf("@") == -1 && phone.startsWith("07")) phone = phone.replace("07", "2547");
        if (ok) {
            TPref.setStringPreference(app, TPref.CLIENT_PHONE, phone);
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
    }

    public void login(String token, String imei) {
        TPref.setStringPreference(app, TPref.LOGGED_UID, String.valueOf(phone));
        client.phone = phone;
        client.token=token;
        client.imei=imei;
        client.SaveToDB(this, app.dbHelper);
        app.client = client;
		Intent i = new Intent(this, MainActivity.class);
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

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_FOR_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        doAuthenticate();


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case REQUEST_IMEI:{
//                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    setDeviceImei();
//                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkSMSpermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read SMS permission is necessary to verify your number, only the message with authentication code will be read");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_FOR_SMS);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)this, new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_FOR_SMS);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkIMEIpermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.READ_PHONE_STATE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read Phone IMEI permission is necessary to verify your number");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_FOR_SMS);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_FOR_SMS);
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