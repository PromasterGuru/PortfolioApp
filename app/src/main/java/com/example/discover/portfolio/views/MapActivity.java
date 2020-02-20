package com.example.discover.portfolio.views;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.discover.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.Map;

import static com.example.discover.portfolio.constants.Constant.AVATAR;
import static com.example.discover.portfolio.constants.Constant.SHARED_PREFERENCE;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private SupportMapFragment supportMapFragment;
    private Marker marker;
    private int TAG_CODE_PERMISSION_LOCATION;
    private SharedPreferences sharedPreferences;
    private Button btnBack;
    private ImageView imageView;
    private Double latitude;
    private Double longitude;
    private Intent intent;
    private String portfolioOwner;
    LatLng point;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        intent = getIntent();
        btnBack = findViewById(R.id.back);
        imageView = findViewById(R.id.avatar);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, 0);

        //Display user profile picture
        imgUrl = sharedPreferences.getString(AVATAR, "");
        Picasso.get().load(imgUrl).into(imageView);

        latitude = Double.parseDouble(intent.getStringExtra("Latitude"));
        longitude = Double.parseDouble(intent.getStringExtra("Longitude"));
        point = new LatLng(latitude, longitude);
        portfolioOwner = intent.getStringExtra("User");

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
        supportMapFragment.getMapAsync(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Redirect user to view their profile
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, UserActivity.class));
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setUpMap();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (googleMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap))
                    .getMapAsync(this);
        }
    }

    private void setUpMap() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.getUiSettings().setMapToolbarEnabled(false);

            //place marker where user location
            marker = googleMap.addMarker(new MarkerOptions().position(point).title(portfolioOwner + " Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                //remove previously placed Marker
                if (marker != null) {
                    marker.remove();
                }
                //place marker where user just clicked
                marker = googleMap.addMarker(new MarkerOptions().position(point).title("Marker")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            }
        });

    }
}