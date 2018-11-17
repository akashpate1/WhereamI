package com.akashpate.whereami;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Creating location manager
    LocationManager locationManager;
    GoogleMap map;
    TextView textView;
    Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //CREATING MAP
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //Check Location Permission
        if ((ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED )&& (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION )!= PackageManager.PERMISSION_GRANTED)){
            //Request Location Permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);

        }
        //Initialize LocationManager
        locationManager =(LocationManager) getSystemService(LOCATION_SERVICE);

        if ((ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED )&& (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION )== PackageManager.PERMISSION_GRANTED)){
            //Request Location Update
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    double latitude,longitude;
    Geocoder geocoder;
    List<Address> addresses;
    String name;
    Marker marker;

    //Create Location Listener for monitoring change in location.
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Get Location Co-ordinates
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //Get Address from co-ordinates
            String message = "Address: NOT FOUND";
            geocoder = new Geocoder(MapActivity.this,Locale.getDefault());
            try{
                addresses = geocoder.getFromLocation(latitude,longitude,1);
                String address = addresses.get(0).getAddressLine(0);
                message = "Address: "+address;
            }catch (IOException exception){
                Toast.makeText(MapActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();

            }
            textView = findViewById(R.id.address);

            //Show address
            textView.setText(message);

            //Add location on the map
            if(marker!=null){
                marker.remove();
            }
            //Get User name
            SharedPreferences preferences = getSharedPreferences("UserPref",MODE_PRIVATE);
            name = preferences.getString("name","No name defined");

            marker = map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(name));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16.8f));


            //Create message for sharing

            final String shareMessage =name+" is at \n"+ message+"\nhttps://google.com/maps/search/?api=1&query="+latitude+","+longitude+"\nShared By Where am I?";

            shareButton = findViewById(R.id.shareBtn);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
