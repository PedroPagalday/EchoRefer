package org.echomobile.refer.objects;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import org.echomobile.refer.Constants;
import org.echomobile.refer.contentprovider.MyProvider;
import org.echomobile.refer.helper.DatabaseHelper;

/**
 * Created by Pedro on 27/10/2016.
 */

public class Referral extends Campaign {
    public String id;
    public String influencer_id;
    public String referee_id;
    public String referee_number;
    public String dt_referred;
    public String dt_converted;
    public String campaign_id;
    public String enterprise_id;
    public int method; // 0:MESSAGE (IN-APP), 1: MESSAGE (OUT OF APP), 2: PHONE CALL, 3: IN PERSON
    public int status; // 0: REFERRED, 1: CONVERTED

    public static final String TABLE= "Referrals";
    public static final String COL_ID="_id";
    public static final String COL_INFLUENCER_ID="influencerID";
    public static final String COL_REFEREE_ID="refereeID";
    public static final String COL_REFEREE_NUMBER="refereeNumber";
    public static final String COL_DATE_REFERRED="date_referred";
    public static final String COL_DATE_CONVERTED="date_converted";
    public static final String COL_CAMPAIGN_ID="campaing_id";
    public static final String COL_ENTERPRISE_ID="enterprise_id";
    public static final String COL_METHOD="method";
    public static final String COL_STATUS="status";

    public static final String [] ALL_COLS ={COL_ID, COL_INFLUENCER_ID, COL_REFEREE_ID, COL_REFEREE_NUMBER, COL_DATE_REFERRED, COL_DATE_CONVERTED, COL_CAMPAIGN_ID, COL_ENTERPRISE_ID, COL_METHOD, COL_STATUS};
    public Referral (String s_influencer_id, String s_referee_id, String s_referee_number, String s_dt_referred, String s_dt_converted, String s_campaign_id, String s_enterprise_id, int s_method, int s_status ){
        influencer_id=s_influencer_id;
        referee_id=s_referee_id;
        referee_number=s_referee_number;
        dt_referred=s_dt_referred;
        dt_converted=s_dt_converted;
        campaign_id=s_campaign_id;
        enterprise_id=s_enterprise_id;
        method=s_method;
        status=s_status;
    }

    /*Static Methods*/

    public static void CreateTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE+" ("+COL_ID+" VARCHAR PRIMARY KEY, "+
                COL_INFLUENCER_ID + " NVARCHAR, " +
                COL_REFEREE_ID+" NVARCHAR,  " +
                COL_REFEREE_NUMBER+" NVARCHAR, " +
                COL_DATE_REFERRED+" NVARCHAR,  " +
                COL_DATE_CONVERTED + " NVARCHAR, " +
                COL_CAMPAIGN_ID+" NVARCHAR, " +
                COL_ENTERPRISE_ID+" NVARCHAR, " +
                COL_METHOD+" INTEGER);"+
                COL_STATUS+" INTEGER);";

        db.execSQL(sql);
        Log.d("MYLOG",sql);
    }
    public Referral (Cursor c){
        id = c.getString(c.getColumnIndexOrThrow(COL_ID));
        influencer_id=c.getString(c.getColumnIndexOrThrow(COL_INFLUENCER_ID));
        referee_id=c.getString(c.getColumnIndexOrThrow(COL_REFEREE_ID));
        referee_number=c.getString(c.getColumnIndexOrThrow(COL_REFEREE_NUMBER));
        dt_referred=c.getString(c.getColumnIndexOrThrow(COL_DATE_REFERRED));
        dt_converted=c.getString(c.getColumnIndexOrThrow(COL_DATE_CONVERTED));
        campaign_id=c.getString(c.getColumnIndexOrThrow(COL_CAMPAIGN_ID));
        enterprise_id=c.getString(c.getColumnIndexOrThrow(COL_ENTERPRISE_ID));
        method=c.getInt(c.getColumnIndexOrThrow(COL_METHOD));
        status=c.getInt(c.getColumnIndexOrThrow(COL_STATUS));

    };

    public void SaveToDB(Activity a, DatabaseHelper dbh) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, this.id);
        cv.put(COL_INFLUENCER_ID, this.influencer_id);
        cv.put(COL_REFEREE_ID, this.referee_id);
        cv.put(COL_REFEREE_NUMBER, this.referee_number);
        cv.put(COL_DATE_REFERRED, this.dt_referred);
        cv.put(COL_DATE_CONVERTED, this.dt_converted);
        cv.put(COL_CAMPAIGN_ID, this.campaign_id);
        cv.put(COL_ENTERPRISE_ID, this.enterprise_id);
        cv.put(COL_METHOD, this.method);
        cv.put(COL_STATUS, this.status);
        a.getContentResolver().update(MyProvider.CLIENT_CONTENT_URI, cv, null, null);
        if (Constants.verbose) Log.d(Constants.TAG,"Saving Referral [ " + influencer_id + " , "+referee_id+"]");
    }

    public static Referral GetByID(Context cx, String id) {
        Referral referral = null;
        Cursor c = cx.getContentResolver().query(Uri.withAppendedPath(MyProvider.REFERRAL_CONTENT_URI, id),
                ALL_COLS, null, null, null);

        if (c.moveToFirst()) {
            referral = new Referral(c);
        }
        c.close();
        return referral;
    }


}
