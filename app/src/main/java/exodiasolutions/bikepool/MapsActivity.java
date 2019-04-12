package exodiasolutions.bikepool;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity  implements OnMapReadyCallback{
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    double currentlat, currentlong;
    private GoogleMap mMap;
    Marker m,m2;
    Button submit;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    boolean mLocationPermissionGranted = false;
    boolean mPermissionDenied = false;
    String from,to;
    String s_long,s_lat,d_long,d_lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        submit = findViewById(R.id.submit);


        Places.initialize(getApplicationContext(), "AIzaSyDV4PGZHadHl8H3oG0LLFlY5dDAAbQM_9Y");
        PlacesClient placesClient = Places.createClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        


    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
            mLocationPermissionGranted = true;
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }


    // when map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();

        getloc();
    }


    public void getloc() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            currentlat =  location.getLatitude();
                            currentlong = location.getLongitude();


                            LatLng trailLocation = new LatLng(currentlat, currentlong);
                            //  mMap.addMarker(new MarkerOptions().position(trailLocation).title("Marker in Current Location"));
                            //mMap.moveCamera(CameraUpdateFactory.zoomBy(50, tra));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(trailLocation));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trailLocation, 15));


                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng arg0) {

                                    Log.d("arg0", arg0.latitude + "-" + arg0.longitude);



                                }
                            });

                        }
                        else{
                            Toast.makeText(MapsActivity.this, "location not set", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void putMarker(View v){
        if(mMap!=null){
            LatLng trailLocation = new LatLng(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude);
            if(m!=null)
                m.remove();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
            s_long = mMap.getCameraPosition().target.longitude+"";
            s_lat = mMap.getCameraPosition().target.latitude+"";
            m = mMap.addMarker(new MarkerOptions().position(trailLocation).title("From Here"));
            from = m.getId();
            showsubmit();
        }
    }
    public void putMarker2(View v){
        if(mMap!=null){
            LatLng trailLocation = new LatLng(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude);
            if(m2!=null)
                m2.remove();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
            d_long = mMap.getCameraPosition().target.longitude+"";
            d_lat = mMap.getCameraPosition().target.latitude+"";

            m2= mMap.addMarker(new MarkerOptions().position(trailLocation).title("to Here"));
            to  = m2.getId();
            showsubmit();
        }
    }

    public void showsubmit(){
        if(m!=null&&m2!=null){
            submit.setVisibility(View.VISIBLE);
        }
    }

    public void submit(View v){
       // Toast.makeText(this, "DONE.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MapsActivity.this,ShareActivity.class);
        i.putExtra("s_long",s_long);
        i.putExtra("s_lat",s_lat);
        i.putExtra("d_long",d_long);
        i.putExtra("d_lat",d_lat);
        startActivity(i);
        MapsActivity.this.finish();

    }


}

