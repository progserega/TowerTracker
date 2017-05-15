package ru.drsk.progserega.towertracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Serega on 13.05.17.
 */

public class mLocation {
    private static Context context = null;
    private static mLocation lc = null;
    private static LocationManager locationManager = null;
    private static LocationListener locationListener = null;
    private double lat = 0;
    private double lon = 0;
    private double accuracy = 10000000;
    private double ele = 0;
    private long time = 0;

    callback updateLocationCallBack = null;

    interface callback {
        void updateLocation(double lat, double lon, double accurate, double ele, long time);
    }

    public void registerCallback(callback updateLocation) {
        this.updateLocationCallBack = updateLocation;
    }

    public static mLocation getInstance(Context applicationcontext) {
        if (lc == null) {
            lc = new mLocation();
            lc.context = applicationcontext;
            lc.startLocation();
        }
        return lc;
    }

    public void stopLocation() {
        // Remove the listener you previously added
        /*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        }
        //else*/
        locationManager.removeUpdates(locationListener);
    }

    private void startLocation() {
        try {
            // Acquire a reference to the system Location Manager
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Define a listener that responds to location updates
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("onLocationChanged()", String.valueOf(location));
                    if (location!=null)
                    {
                        lat=location.getLatitude();
                        lon=location.getLongitude();
                        accuracy=location.getAccuracy();
                        ele=location.getAltitude();
                        time=location.getTime();

                        if (updateLocationCallBack!=null)
                            updateLocationCallBack.updateLocation(lat, lon, accuracy, ele, time);

                    }
                    else
                    {
                        Log.d("onLocationChanged()", "location==null()");
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.d("startLocation()", "onStatusChanged()");
                }

                public void onProviderEnabled(String provider) {
                    Log.d("startLocation()", "onProviderEnabled()");
                }

                public void onProviderDisabled(String provider) {
                    Log.d("startLocation()", "onProviderDisabled()");
                }
            };
            // Register the listener with the Location Manager to receive location updates
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
                //&& ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
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
                // getting GPS status
                boolean mIsGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                Log.d("startLocation()", "mIsGpsEnabled=" + mIsGpsEnabled);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    Log.i("startLocation()", "lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
                    lat=location.getLatitude();
                    lon=location.getLongitude();
                    accuracy=location.getAccuracy();
                    ele=location.getAltitude();
                    time=location.getTime();
                } else {
                    Log.i("startLocation()", "location==null");
                }
            }
        } catch (Exception e) {
            Log.e("startLocation()", "exceptition error");
            Log.e("exception:", e.getMessage());
        }
    }
/*
    public boolean getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
        {
            Log.e("getLocation()", "no permissions!");
            return false;
        }
        else
        {
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null)
            {
                lat=location.getLatitude();
                lon=location.getLongitude();
            }
            else
            {
                Log.e("getLocation()","location==null");
                return false;
            }
        }
        return true;
    }
*/
    public double getAccuracy()
    {
        return accuracy;
    }
    public double getLat()
    {
        return lat;
    }
    public double getEle() {return  ele; }
    public long getTime() {return time; }
    public double getLon()
    {
        return lon;
    }
}

