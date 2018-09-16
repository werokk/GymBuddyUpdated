package cw.gymbuddy;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.*;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;



public class LocationService implements LocationListener {


    public static LocationService instance;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location loc;
    private double longtitude;
    private double latitude;
    Activity act;
    public LocationService(Activity act)
    {
        this.act=act;


    }

    public void askForLocationPermision()
    {

        if (ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                )

        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                    Manifest.permission.ACCESS_FINE_LOCATION )|| ActivityCompat.shouldShowRequestPermissionRationale(act,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(act,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        123);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }



        }
        else
        {

            ActivityCompat.requestPermissions(act,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);

        }

    }


    @SuppressLint("MissingPermission")
    public void getLastKnowLocation()
    {

        System.out.println("Hello");
        mFusedLocationClient= getFusedLocationProviderClient(act);


        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(act, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        System.out.println("Test");
                        if (location != null) {
                            longtitude= location.getLongitude();
                            latitude = location.getLatitude();
                            loc = location;
                            System.out.println("Got new location");
                        }
                        System.out.println("End outside if");
                    }
                });


    }

    public String GetAddress()
    {

        String actAddress = "";
        Geocoder geocoder = new Geocoder(act, Locale.getDefault());
        try {
            List<Address> addresses=  geocoder.getFromLocation(latitude, longtitude, 1);
            System.out.println(latitude);
            System.out.println(longtitude);
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            for (String s : addressFragments) {
                System.out.println(s);
                actAddress = actAddress+s;
            }
            return actAddress;

        }catch(IOException e)
        {

        }
        return null;
    }



    private void onSuccess()
    {

    }
    private void onFail()
    {

    }

    public Location getLoc() {
        return loc;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public static LocationService getInstance( Activity act)
    {
        if(instance == null)
        {
            instance = new LocationService(act);
            return instance;
        }else
        {
            return instance;
        }


    }
}