package com.example.discover.portfolio.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.discover.R;
import com.example.discover.databinding.ActivityProfileBinding;
import com.example.discover.portfolio.db.PortfolioDBHandler;
import com.squareup.picasso.Picasso;

import static android.text.TextUtils.isEmpty;
import static com.example.discover.portfolio.constants.Constant.AVATAR;
import static com.example.discover.portfolio.constants.Constant.SHARED_PREFERENCE;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private Intent intent;
    private PortfolioDBHandler portfolioDBHandler;
    private SharedPreferences prefs;
    private String latitude;
    private String longitude;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        prefs = getSharedPreferences(SHARED_PREFERENCE, 0);
        intent = getIntent();
        latitude = intent.getStringExtra("Latitude");
        longitude = intent.getStringExtra("Longitude");

        //Display user profile picture
        imgUrl = prefs.getString(AVATAR, "");
        Picasso.get().load(imgUrl).into(binding.avatar);

        portfolioDBHandler = new PortfolioDBHandler(this);

        //Populate portfolio fields
        binding.profileImage.setImageURI(Uri.parse(intent.getStringExtra("Avatar")));
        binding.username.setText(intent.getStringExtra("Username"));
        binding.jobTitle.setText(intent.getStringExtra("JobTitle"));
        binding.bio.setText(intent.getStringExtra("Bio"));
        binding.education.setText(intent.getStringExtra("Education"));
        binding.skills.setText(intent.getStringExtra("Skills"));
        binding.email.setText(intent.getStringExtra("Email"));
        binding.phone.setText(intent.getStringExtra("Phone"));
        binding.website.setText(intent.getStringExtra("Web"));
        binding.fb.setText(intent.getStringExtra("Facebook"));
        binding.linkedIn.setText(intent.getStringExtra("LinkedIn"));

        //Display user location in terms of latitude and longitude
        if (isEmpty(latitude) || isEmpty(longitude)) {
            binding.location.setText("Unknown location");
            binding.location.setEnabled(false);
        } else {
            binding.location.setText(latitude.substring(0, 10) + "," + longitude.substring(0, 10));
            binding.location.setEnabled(true);
        }


        //Open google map
        binding.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(ProfileActivity.this, MapActivity.class);
                mapIntent.putExtra("Latitude", latitude);
                mapIntent.putExtra("Longitude", longitude);
                mapIntent.putExtra("User", intent.getStringExtra("Username"));
                startActivity(mapIntent);
            }
        });
        //Redirect use to a screen where they will enter their information to build a portfolio
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, UserBasicInfoActivity.class));
            }
        });

        //Delete current portfolio
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = intent.getIntExtra("Id", 0);
                int count = portfolioDBHandler.deletePortfolio(String.valueOf(id));
                if (count > 0) {
                    Toast.makeText(ProfileActivity.this, "Portfolio deleted successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(ProfileActivity.this, "Unable to delete this portfolio, please try again later", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Redirect use to a screen where they can update their portfolio
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                Bundle bundle = intent.getExtras();
                newIntent.putExtras(bundle);
                startActivity(newIntent);
            }
        });

        //Redirect user to view their profile
        binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, UserActivity.class));
            }
        });
    }
}
