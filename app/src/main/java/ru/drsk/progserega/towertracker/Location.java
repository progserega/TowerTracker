package ru.drsk.progserega.towertracker;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by Serega on 13.05.17.
 */

public class Location {
    private static Context context = null;
    private static Location lc = null;
    private static LocationManager locationManager = null;
    private static LocationListener locationListener = null;

    public static Location getInstance(Context applicationcontext) {
        if (lc == null) {
            lc = new Location();
            lc.context = applicationcontext;
            lc.startLocation();
        }
        return lc;
    }
    private void stopLocation() {
        // Remove the listener you previously added
        locationManager.removeUpdates(locationListener);
    }

    private void startLocation() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("startLocation()","onStatusChanged()");
                msbox("startLocation()","onStatusChanged()");
            }

            public void onProviderEnabled(String provider) {
                Log.d("startLocation()","onProviderEnabled()");
                msbox("startLocation()","onProviderEnabled()");
            }

            public void onProviderDisabled(String provider) {
                Log.d("startLocation()","onProviderDisabled()");
                msbox("startLocation()","onProviderDisabled()");
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
            //&& ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("startLocation()", "no permissions!");
            return;
        } else {
            Log.i("startLocation()", "success permissions!");
            Log.d("startLocation()", "register locationListener");
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void makeUseOfNewLocation(android.location.Location location) {
        Log.d("makeUseOfNewLocation()", String.valueOf(location));
        msbox("makeUseOfNewLocation()", String.valueOf(location));
    }

    private void msbox(String str,String str2)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setTitle(str);
        dlgAlert.setMessage(str2);
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //   finish();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}

