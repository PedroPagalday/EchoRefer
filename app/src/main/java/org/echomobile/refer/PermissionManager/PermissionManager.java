package org.echomobile.refer.PermissionManager;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Pedro on 02/11/2016.
 */

public class PermissionManager {
    public static void check(Activity activity, String permission, int requestCode){
        //if requested permission isn't Granted yet
        if (ActivityCompat.checkSelfPermission(activity,permission)!= PackageManager.PERMISSION_GRANTED){
            //Request permission from user
            ActivityCompat.requestPermissions(activity, new String[]{permission},requestCode);
        }
    }
}
