package com.assignment2ottawa.usertrackingapp;

import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import pl.droidsonroids.gif.GifTextView;

/* Still Activity */

public class Still extends AppCompatActivity implements OnMapReadyCallback
{
    public PolylineOptions mPolylineOptions;
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_still);
        final String[] lastActivityInfo=getIntent().getStringArrayExtra("lastActivityInfo");
        if(lastActivityInfo[0].equals("True"))
        {
            Toast.makeText(getBaseContext(), "This is your first activity", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getBaseContext(), "Your last activity ("+lastActivityInfo[1]+") duration: "+ lastActivityInfo[2], Toast.LENGTH_SHORT).show();
        }

        Long identifier = Calendar.getInstance().getTime().getTime();
        UserActivity ua = new UserActivity();
        ua.setActivity("Still");
        ua.setStartTime(identifier.toString());
        ua.setCustomIdentifier(Calendar.getInstance().getTime().toString());
        UserTrackDB db=new UserTrackDB(getApplicationContext());
        db.addUserActivity(ua);
        TextView clock=(TextView) findViewById(R.id.stillDescription);
        clock.setText("You are still since: "+ Calendar.getInstance().getTime().toString());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapStill);
        mapFragment.getMapAsync(this);

        GifTextView gt=(GifTextView) findViewById(R.id.gifTextView) ;

        gt.setOnTouchListener(new OnSwipeTouchListener(Still.this) {
            public void onSwipeTop() {

            }
            public void onSwipeRight() {

            }
            public void onSwipeLeft() {
                startActivity(new Intent(Still.this, ViewDB.class));
            }
            public void onSwipeBottom() {

            }

        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = { android.Manifest.permission.ACCESS_FINE_LOCATION , android.Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);

        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mPolylineOptions = new PolylineOptions();
        mPolylineOptions.color(Color.BLUE).width(10);

        final double[] longitude = {0};
        final double[] latitude = {0};
        if(location!=null)
        {
            longitude[0] =location.getLongitude();
            latitude[0] =location.getLatitude();
        }
        final MarkerOptions opt=new MarkerOptions();
        opt.position(new LatLng(latitude[0], longitude[0]));
        googleMap.addMarker(opt);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude[0], longitude[0]),16));

        final LocationListener locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location)
            {
                LatLng latLong=new LatLng(location.getLatitude(),location.getLongitude());
                googleMap.clear();
                googleMap.addPolyline(mPolylineOptions.add(latLong));
                googleMap.addMarker(new MarkerOptions().position(latLong));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude[0], longitude[0]),16));
            }

            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }
            public void onProviderDisabled(String provider)
            {

            }
            public void onProviderEnabled(String provider)
            {

            }

        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
    }
}
