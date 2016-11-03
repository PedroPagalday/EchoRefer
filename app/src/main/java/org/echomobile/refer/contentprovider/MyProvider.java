package org.echomobile.refer.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import org.echomobile.refer.Constants;
import org.echomobile.refer.helper.DatabaseHelper;
import org.echomobile.refer.objects.Campaign;
import org.echomobile.refer.objects.Client;
import org.echomobile.refer.objects.Referral;

public class MyProvider extends ContentProvider {
	DatabaseHelper dbHelper;
	
	private static final String AUTHORITY = "org.echomobile.refer.contentprovider.MyProvider";
	public static final int CAMPAIGNS_CODE = 100;
	public static final int CAMPAIGNS_ID_CODE = 110;
	public static final int CLIENTS_CODE = 200;
	public static final int CLIENT_ID_CODE = 210;
	public static final int REFERRAL_CODE = 300;
	public static final int REFERRAL_ID_CODE = 310;
	public static final int RECORDS_CODE = 400;
	public static final int RECORDS_ID_CODE = 410;

	public static final Uri CAMPAIGN_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/campaigns");
	public static final Uri CLIENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/clients");
	public static final Uri REFERRAL_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/referrals");

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, "campaigns/", CAMPAIGNS_CODE);
		sURIMatcher.addURI(AUTHORITY, "campaigns/*", CAMPAIGNS_ID_CODE);
		sURIMatcher.addURI(AUTHORITY, "clients/", CLIENTS_CODE);
		sURIMatcher.addURI(AUTHORITY, "clients/#", CLIENT_ID_CODE);
		sURIMatcher.addURI(AUTHORITY, "referral/", REFERRAL_CODE);
		sURIMatcher.addURI(AUTHORITY, "referrals/*", REFERRAL_ID_CODE);

	}

	@Override
	public boolean onCreate() {
		dbHelper = DatabaseHelper.getInstance(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    int rowsAffected = 0;
	    switch (uriType) {
	    case CAMPAIGNS_CODE:
	        rowsAffected = dbHelper.mdb.delete(Campaign.TABLE, selection, selectionArgs);
	        break;
		case RECORDS_CODE:
			rowsAffected = dbHelper.mdb.delete(Referral.TABLE, selection, selectionArgs);
			break;
		default:
	        throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsAffected;

	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		long id = 0;
	/*	switch (uriType) {
		case CAMPAIGNS_CODE:
			Log.d(Constants.TAG,"Inserting campaign: " + values.toString());
			id = dbHelper.mdb.insert(Campaign.TABLE, Campaign.COL_CAMPAIGN_ID, values);
			break;
        case REFERRAL_CODE:
            Log.d(Constants.TAG,"Inserting referral: " + values.toString());
            id = dbHelper.mdb.insert(Referral.TABLE, Referral.COL_ID, values);
            break;
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}*/
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(CLIENT_CONTENT_URI+"/"+id);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(Client.TABLE);
		int uriType = sURIMatcher.match(uri);
		/*switch (uriType) {
		case CAMPAIGNS_CODE:
			queryBuilder.setTables(Campaign.TABLE);
			break;
		case CAMPAIGNS_ID_CODE:
			queryBuilder.setTables(Campaign.TABLE);
			queryBuilder.appendWhere(Campaign.COL_CAMPAIGN_ID + "='" + uri.getLastPathSegment() + "'");
			break;
		case CLIENTS_CODE:
			queryBuilder.setTables(Client.TABLE);
			break;
		case CLIENT_ID_CODE:
			queryBuilder.setTables(Client.TABLE);
			String uid = uri.getLastPathSegment();
			queryBuilder.appendWhere(Client.COL_ID + "=" + uid);
			break;
        case REFERRAL_CODE:
            queryBuilder.setTables(Referral.TABLE);
            break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri.toString());
		}*/
		Cursor cursor = queryBuilder.query(dbHelper.mdb,
				projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		double id = 0;
		/*switch (uriType) {
		case CAMPAIGNS_CODE:
			id = dbHelper.mdb.insertWithOnConflict(Campaign.TABLE, Campaign.COL_CAMPAIGN_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
			break;
		case CAMPAIGNS_ID_CODE:
			id = dbHelper.mdb.insertWithOnConflict(Campaign.TABLE, Campaign.COL_CAMPAIGN_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
			break;
		case CLIENTS_CODE:
			id = dbHelper.mdb.insertWithOnConflict(Client.TABLE, Client.COL_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
			break;
		case REFERRAL_CODE:
			id = dbHelper.mdb.insertWithOnConflict(Referral.TABLE, Referral.COL_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
			break;
       default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}*/
		getContext().getContentResolver().notifyChange(uri, null);
		return (id == -1) ? 0 : 1;
	}
}
