package org.echomobile.refer.objects;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.echomobile.refer.Constants;
import org.echomobile.refer.contentprovider.MyProvider;
import org.echomobile.refer.helper.DatabaseHelper;

/**
 * Created by Pedro on 26/10/2016.
 */

public class Campaign {
    public String id;
    public String details;
    public int influencer_award_type;
    public String influencer_award_amount;
    public int referred_award_type;
    public String referred_award_amount;
    public String influencer_succes_message;
    public String referred_success_message;
    public int conversion_type;
    public int status; // 0= EXPIRED, 1= ACTIVE, 2= INACTIVE
    public String dt_expires;
    public String dt_start;
    public String enterprise_id;
    public int n_referrals;
    public int n_conversions;

    public static final String COL_CAMPAIGN_ID="id";
    public static final String COL_DETAILS="details";
    public static final String COL_INFLUENCER_AWARD_TYPE="influencer_award_type";
    public static final String COL_INFLUENCER_AWARD_AMOUNT="influencer_award_amount";
    public static final String COL_REFERRED_AWARD_TYPE="referred_award_type";
    public static final String COL_REFERRED_AWARD_AMOUNT ="Referred_award_amount";
    public static final String COL_INFLUENCER_SUCCESS_MESSAGE="influencer_success_message";
    public static final String COL_REFERRED_SUCCESS_MESSAGE="referred_success_message";
    public static final String COL_CONVERSION_TYPE="conversion_type";
    public static final String COL_STATUS="status";
    public static final String COL_EXPIRE_DATE="expire date";
    public static final String COL_START_DATE="start date";
    public static final String COL_ENTERPRISE_ID="enterprise_id";
    public static final String COL_NUM_REFERRALS="n_referrals";
    public static final String COL_NUM_CONVERSIONS="n_conversions";

    public static final String [] ALL_COL = {COL_CAMPAIGN_ID, COL_DETAILS,COL_INFLUENCER_AWARD_TYPE,COL_INFLUENCER_AWARD_AMOUNT, COL_REFERRED_AWARD_AMOUNT,COL_REFERRED_AWARD_TYPE,COL_INFLUENCER_SUCCESS_MESSAGE,COL_REFERRED_SUCCESS_MESSAGE,COL_CONVERSION_TYPE,COL_STATUS,COL_EXPIRE_DATE,COL_START_DATE,COL_ENTERPRISE_ID,COL_NUM_REFERRALS,COL_NUM_REFERRALS};

    public static final String TABLE = "Campaigns";
    public Campaign(){};

    public Campaign (String s_campaign_id, String s_details, int s_influencer_award_type, String s_influencer_award_amount, int s_referred_award_type, String s_referred_award_amount, String s_influencer_succes_message, String s_referred_success_message, int s_conversion_type, int s_status, String s_dt_expires, String s_dt_start, String s_enterprise_id, int s_n_referrals, int s_n_conversions){
        id =s_campaign_id;
        details=s_details;
        influencer_award_type=s_influencer_award_type;
        influencer_award_amount=s_influencer_award_amount;
        referred_award_amount=s_referred_award_amount;
        referred_award_type=s_referred_award_type;
        influencer_succes_message=s_influencer_succes_message;
        referred_success_message=s_referred_success_message;
        conversion_type=s_conversion_type;
        status=s_status;
        dt_expires=s_dt_expires;
        dt_start=s_dt_start;
        enterprise_id=s_enterprise_id;
        n_referrals=s_n_referrals;
        n_conversions=s_n_conversions;


    }
    /*Static Methods*/

    public static void CreateTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE+" ("+COL_CAMPAIGN_ID+" VARCHAR PRIMARY KEY, "+
                COL_DETAILS + " NVARCHAR, " +
                COL_INFLUENCER_AWARD_TYPE+" INTEGER); " +
                COL_INFLUENCER_AWARD_AMOUNT+" NVARCHAR, " +
                COL_REFERRED_AWARD_TYPE+" INTEGER); " +
                COL_REFERRED_AWARD_AMOUNT + " NVARCHAR, " +
                COL_INFLUENCER_SUCCESS_MESSAGE+" NVARCHAR, " +
                COL_REFERRED_SUCCESS_MESSAGE+" NVARCHAR, " +
                COL_CONVERSION_TYPE+" INTEGER);"+
                COL_STATUS+" INTEGER);"+
                COL_EXPIRE_DATE+" NVARCHAR,;"+
                COL_START_DATE+" NVARCHAR,;"+
                COL_ENTERPRISE_ID+"NVARCHAR,;"+
                COL_NUM_REFERRALS+" INTEGER);"+
                COL_NUM_CONVERSIONS+" INTEGER);";
        db.execSQL(sql);
        Log.d("MYLOG",sql);
    }
    public Campaign (Cursor c){
        id = c.getString(c.getColumnIndexOrThrow(COL_CAMPAIGN_ID));
        details=c.getString(c.getColumnIndexOrThrow(COL_DETAILS));
        influencer_award_type=c.getInt(c.getColumnIndexOrThrow(COL_INFLUENCER_AWARD_TYPE));
        influencer_award_amount=c.getString(c.getColumnIndexOrThrow(COL_INFLUENCER_AWARD_AMOUNT));
        referred_award_type=c.getInt(c.getColumnIndexOrThrow(COL_INFLUENCER_AWARD_TYPE));
        referred_award_amount=c.getString(c.getColumnIndexOrThrow(COL_INFLUENCER_AWARD_AMOUNT));
        influencer_succes_message=c.getString(c.getColumnIndexOrThrow(COL_INFLUENCER_SUCCESS_MESSAGE));
        referred_success_message=c.getString(c.getColumnIndexOrThrow(COL_REFERRED_SUCCESS_MESSAGE));
        conversion_type=c.getInt(c.getColumnIndexOrThrow(COL_CONVERSION_TYPE));
        status=c.getInt(c.getColumnIndexOrThrow(COL_STATUS));
        dt_expires=c.getString(c.getColumnIndexOrThrow(COL_EXPIRE_DATE));
        dt_start=c.getString(c.getColumnIndexOrThrow(COL_START_DATE));
        enterprise_id=c.getString(c.getColumnIndexOrThrow(COL_ENTERPRISE_ID));
        n_referrals=c.getInt(c.getColumnIndexOrThrow(COL_NUM_REFERRALS));
        n_conversions=c.getInt(c.getColumnIndexOrThrow(COL_NUM_CONVERSIONS));

    };
    public void SaveToDB(Activity a, DatabaseHelper dbh) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CAMPAIGN_ID, this.id);
        cv.put(COL_DETAILS, this.details);
        cv.put(COL_INFLUENCER_AWARD_TYPE, this.influencer_award_type);
        cv.put(COL_INFLUENCER_AWARD_AMOUNT, this.influencer_award_amount);
        cv.put(COL_REFERRED_AWARD_TYPE, this.referred_award_type);
        cv.put(COL_REFERRED_AWARD_AMOUNT, this.referred_award_amount);
        cv.put(COL_INFLUENCER_SUCCESS_MESSAGE, this.influencer_succes_message);
        cv.put(COL_REFERRED_SUCCESS_MESSAGE, this.referred_success_message);
        cv.put(COL_CONVERSION_TYPE, this.conversion_type);
        cv.put(COL_STATUS, this.status);
        cv.put(COL_EXPIRE_DATE, this.dt_expires);
        cv.put(COL_START_DATE, this.dt_start);
        cv.put(COL_ENTERPRISE_ID,this.enterprise_id);
        cv.put(COL_NUM_REFERRALS,this.n_referrals);
        cv.put(COL_NUM_CONVERSIONS,this.n_conversions);
        a.getContentResolver().update(MyProvider.CAMPAIGN_CONTENT_URI, cv, null, null);
        if (Constants.verbose) Log.d(Constants.TAG,"Saving Campaign[ " + id + " ]");
    }

}
