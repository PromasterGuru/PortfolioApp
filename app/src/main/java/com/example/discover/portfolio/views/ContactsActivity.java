package com.example.discover.portfolio.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.discover.R;
import com.example.discover.databinding.ActivityContactsBinding;
import com.example.discover.portfolio.db.PortfolioDBHandler;
import com.example.discover.portfolio.models.User;
import com.squareup.picasso.Picasso;

import static android.text.TextUtils.isEmpty;
import static com.example.discover.portfolio.constants.Constant.AVATAR;
import static com.example.discover.portfolio.constants.Constant.BIO;
import static com.example.discover.portfolio.constants.Constant.EDUCATION;
import static com.example.discover.portfolio.constants.Constant.IMAGE_URL;
import static com.example.discover.portfolio.constants.Constant.JOT_TITLE;
import static com.example.discover.portfolio.constants.Constant.SHARED_PREFERENCE;
import static com.example.discover.portfolio.constants.Constant.SKILLS;
import static com.example.discover.portfolio.constants.Constant.USERNAME;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;
    private SharedPreferences prefs;
    private String imageUrl;
    private String username;
    private String jobTitle;
    private String bio;
    private String education;
    private String skills;
    private PortfolioDBHandler dbHandler;
    private String latitude;
    private String longitude;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);
        prefs = getSharedPreferences(SHARED_PREFERENCE, 0);

        //Display user profile picture
        imgUrl = prefs.getString(AVATAR, "");
        Picasso.get().load(imgUrl).into(binding.avatar);

        //Load user information from SharedPreference
        imageUrl = prefs.getString(IMAGE_URL, "");
        username = prefs.getString(USERNAME, "");
        jobTitle = prefs.getString(JOT_TITLE, "");
        bio = prefs.getString(BIO, "");
        education = prefs.getString(EDUCATION, "");
        skills = prefs.getString(SKILLS, "");
        latitude = prefs.getString("Latitude", "");
        longitude = prefs.getString("Longitude", "");
        dbHandler = new PortfolioDBHandler(this);

        //Redirect user to the next screen to enter their contact information
        binding.next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String email = binding.email.getText().toString();
                String phone = binding.phone.getText().toString();
                String web = binding.website.getText().toString();
                String facebook = binding.facebook.getText().toString();
                String linkedIn = binding.linkedIn.getText().toString();

                try {
                    //Validate user education and skills entry
                    if (email == null || isEmpty(email)) {
                        binding.email.setError("Please enter your email address");
                    } else if (phone == null || isEmpty(phone)) {
                        binding.phone.setError("Please enter your phone number");
                    } else {
                        //Save portfolio
                        dbHandler.addPortfolio(new User(0, imageUrl, username, jobTitle, bio, education, skills, email, phone, web, facebook, linkedIn, latitude, longitude));
                        Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception ex) {
                    Toast.makeText(ContactsActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //Redirect user to view their profile
        binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactsActivity.this, UserActivity.class));
            }
        });
    }
}
