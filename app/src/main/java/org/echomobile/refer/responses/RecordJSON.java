package org.echomobile.refer.responses;

import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

/**
 * Created by jeremygordon on 2/1/16.
 */
public class RecordJSON {
    public String kn;
    public long ts;
    //public Columns columns;

    public LatLng getLocation() {
       /* if (this.columns.location != null) {
            String[] latlon = TextUtils.split(this.columns.location, ",");
            double lat = Double.valueOf(latlon[0]);
            double lon = Double.valueOf(latlon[1]);
            if (lat != 0.0 || lon != 0.0) return new LatLng(lat, lon);
        }
        return null;*/
        return null;
    }

   /* public double getSpeed() {
        return this.columns.speed;
    }

    public double getBearing() {
        return this.columns.bearing;
    }

    private class Columns {
        public String location;
        public double speed; // kph
        public double bearing; // 0 - 360
        public double ts; // ms datetime recorded
    }

    public String toString() {
        return "<Record location="+String.valueOf(this.getLocation())+" speed="+String.valueOf(this.getSpeed())+" >";
    }

    public String readable() {
        return String.valueOf(new DecimalFormat("#.##").format(this.getSpeed())) + "kph at " + Util.print_datetime(this.ts);
    }
*/
}