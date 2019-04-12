package exodiasolutions.bikepool;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import exodiasolutions.bikepool.Custom.Store;

public class ShowMaps extends AppCompatActivity  implements OnMapReadyCallback{
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    boolean mLocationPermissionGranted = false;
    boolean mPermissionDenied = false;
Thread thread;
    String s_long,s_lat,d_long,d_lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_maps);

        s_long = getIntent().getStringExtra("s_long");
        s_lat = getIntent().getStringExtra("s_lat");
        d_long = getIntent().getStringExtra("d_long");
        d_lat = getIntent().getStringExtra("d_lat");

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LatLng trailLocation = new LatLng(Double.parseDouble(s_lat),Double.parseDouble(s_long));
                Marker m = mMap.addMarker(new MarkerOptions().position(trailLocation).title("Source"));

                LatLng trailLocation2 = new LatLng(Double.parseDouble(d_lat),Double.parseDouble(d_long));
                Marker m2 = mMap.addMarker(new MarkerOptions().position(trailLocation2).title("Destination"));

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(m.getPosition());
                builder.include(m2.getPosition());
                LatLngBounds bounds = builder.build();
                int padding = 150; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);
                /*
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(trailLocation2));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trailLocation2, 15));
*/



            }
        }, 100);



/*
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(m.getPosition());
        builder.include(m2.getPosition());
        LatLngBounds bounds = builder.build();

        int padding = 150; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding, padding, padding);
        googleMap.animateCamera(cu);*/
    }


}

