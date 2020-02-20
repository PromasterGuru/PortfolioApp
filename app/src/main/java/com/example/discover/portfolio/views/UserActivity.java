package com.example.discover.portfolio.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.discover.R;
import com.example.discover.databinding.ActivityUserBinding;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import static com.example.discover.portfolio.constants.Constant.AVATAR;
import static com.example.discover.portfolio.constants.Constant.CURRENT_EMAIL;
import static com.example.discover.portfolio.constants.Constant.CURRENT_USERNAME;
import static com.example.discover.portfolio.constants.Constant.SHARED_PREFERENCE;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        preferences = getSharedPreferences(SHARED_PREFERENCE, 0);

        //Get user profile information
        String imgUrl = preferences.getString(AVATAR, "");
        String email = preferences.getString(CURRENT_EMAIL, "");
        String username = preferences.getString(CURRENT_USERNAME, "");

        try {
            //Display user profile information
            Picasso.get().load(imgUrl).into(binding.avatar);
            binding.name.setText(username);
            binding.email.setText(email);
        } catch (Exception ex) {
            Toast.makeText(UserActivity.this, "Unable to load profile information", Toast.LENGTH_LONG).show();
        }

        //Navigate to previous screen
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Logout
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logout user
                if(AccessToken.getCurrentAccessToken() != null){
                    LoginManager.getInstance().logOut();
                }
                //Redirect user to login activity
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
            }
        });
    }
}
