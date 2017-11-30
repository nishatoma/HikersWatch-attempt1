package com.example.league95.hikerswatch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    //TextViews
    TextView lat, lng, alt, acc, address1;

    // Check permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Check permission
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat = findViewById(R.id.lat);
        lng = findViewById(R.id.lng);
        alt = findViewById(R.id.altitude);
        acc = findViewById(R.id.accuracy);
        address1 = findViewById(R.id.address);
        // Instantiate Location Manager
        locationManager = (LocationManager) this.getSystemService(AppCompatActivity.LOCATION_SERVICE);
        //Instantiate location listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat.setText(String.valueOf("Lattitude: " + location.getLatitude()));
                lng.setText(String.valueOf("Longtitude: " + location.getLongitude()));
                alt.setText(String.valueOf("Altitude: " + location.getAltitude()));
                acc.setText(String.valueOf("Accuracy: " + location.getAccuracy()));
                //Now in order to get the address we need a geocoder
                //We're in location listener, so we cannot use 'this' as context
                //We use the getApplicationContext instead.
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                //Now get information about the address.
                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String result = "";
                    Address address = addressList.get(0);
                    if (address.getSubThoroughfare() != null) {
                        result += address.getSubThoroughfare() + "\n";
                    }
                    if (address.getThoroughfare() != null) {
                        result += address.getThoroughfare() + "\n";
                    }
                    if (address.getCountryCode() != null) {
                        result += address.getSubThoroughfare() + "\n";
                    }
                    if (address.getCountryName() != null) {
                        result += address.getSubThoroughfare() + "\n";
                    }
                    address1.setText("Address: \n" + result);
                } catch (IOException e) {
                    System.out.println("address not found!");
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        //Check build version
        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //then grant the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

    }
}
