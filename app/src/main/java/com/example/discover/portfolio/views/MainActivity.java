package com.example.discover.portfolio.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discover.R;
import com.example.discover.databinding.ActivityMainBinding;
import com.example.discover.portfolio.adapters.PortifolioAdapter;
import com.example.discover.portfolio.db.PortfolioDBHandler;
import com.example.discover.portfolio.models.User;
import com.squareup.picasso.Picasso;

import java.security.Permissions;
import java.util.List;

import static com.example.discover.portfolio.constants.Constant.AVATAR;
import static com.example.discover.portfolio.constants.Constant.SHARED_PREFERENCE;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private PortifolioAdapter portifolioAdapter;
    private RecyclerView recyclerView;
    private ActivityMainBinding binding;
    private PortfolioDBHandler dbHandler;
    private LinearLayoutManager linearLayoutManager;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private LocationManager locationManager;
    private Location location;
    private String latitude;
    private String longitude;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        dbHandler = new PortfolioDBHandler(this);
        editor = getSharedPreferences(SHARED_PREFERENCE, 0).edit();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, 0);

        //Display user profile picture
        imgUrl = sharedPreferences.getString(AVATAR, "");
        Picasso.get().load(imgUrl).into(binding.avatar);

        try {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                editor.putString("Latitude", latitude);
                editor.putString("Longitude", longitude);
                editor.apply();
            }

            //Load portfolios from the DB
            List<User> portfolios = dbHandler.getPortfolio();

            //If the DB has portfolios, display them, otherwise redirect the user to a screen where they can add new ones
            if (portfolios.size() > 0) {
                //Display each portfolio in a recycler view
                portifolioAdapter = new PortifolioAdapter(this, portfolios);
                linearLayoutManager = new LinearLayoutManager(this);
                recyclerView = binding.recyclerView;
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(portifolioAdapter);
            } else {
                startActivity(new Intent(this, UserBasicInfoActivity.class));
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        //Redirect user to view their profile
        binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
        } catch (Exception ex) {
            Log.d("TAG", "Couldn't read new user location");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "Status changed");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "Location provider enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "Location provider disabled");
    }
}

