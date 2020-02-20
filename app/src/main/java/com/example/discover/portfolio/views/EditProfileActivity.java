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
import com.example.discover.databinding.ActivityEditProfileBinding;
import com.example.discover.portfolio.db.PortfolioDBHandler;
import com.example.discover.portfolio.models.User;
import com.squareup.picasso.Picasso;

import static android.text.TextUtils.isEmpty;
import static com.example.discover.portfolio.constants.Constant.AVATAR;
import static com.example.discover.portfolio.constants.Constant.SHARED_PREFERENCE;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private int REQUEST_CODE_LOAD_IMAGE = 100;
    private Bundle b;
    private Uri uri;
    private PortfolioDBHandler dbHandler;
    private String username;
    private String jobTitle;
    private String bio;
    private String education;
    private String skills;
    private String email;
    private String phone;
    private String web;
    private String facebook;
    private String linkedIn;
    private String latitude;
    private String longitude;
    private SharedPreferences prefs;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        b = getIntent().getExtras();
        prefs = getSharedPreferences(SHARED_PREFERENCE, 0);
        dbHandler = new PortfolioDBHandler(this);

        //Display user profile picture
        imgUrl = prefs.getString(AVATAR, "");
        Picasso.get().load(imgUrl).into(binding.avatar);

        //Display profile data in their respective fields
        binding.profileImage.setImageURI(Uri.parse(b.getString("Avatar")));
        binding.username.setText(b.getString("Username"));
        binding.jobTitle.setText(b.getString("JobTitle"));
        binding.bio.setText(b.getString("Bio"));
        binding.education.setText(b.getString("Education"));
        binding.skills.setText(b.getString("Skills"));
        binding.email.setText(b.getString("Email"));
        binding.phone.setText(b.getString("Phone"));
        binding.website.setText(b.getString("Web"));
        binding.fb.setText(b.getString("Facebook"));
        binding.linkedIn.setText(b.getString("LinkedIn"));
        uri = Uri.parse(b.getString("Avatar"));

        //Load profile picture
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                try {
                    startActivityForResult(intent, REQUEST_CODE_LOAD_IMAGE);
                } catch (Exception ex) {
                    Toast.makeText(EditProfileActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //Update profile
        binding.btnUpdatePortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = b.getInt("Id");
                    latitude = prefs.getString("Latitude", "");
                    longitude = prefs.getString("Longitude", "");
                    username = binding.username.getText().toString();
                    jobTitle = binding.jobTitle.getText().toString();
                    bio = binding.bio.getText().toString();
                    education = binding.education.getText().toString();
                    skills = binding.skills.getText().toString();
                    email = binding.email.getText().toString();
                    phone = binding.phone.getText().toString();
                    web = binding.website.getText().toString();
                    facebook = binding.fb.getText().toString();
                    linkedIn = binding.linkedIn.getText().toString();

                    //Validate user inputs
                    //Validate username
                    if (isEmpty(username)) {
                        binding.username.setError("Please enter your username");
                    }
                    //Validate Job title
                    else if (isEmpty(jobTitle)) {
                        binding.jobTitle.setError("Please enter your job title");
                    }
                    //Validate user bio
                    else if (isEmpty(bio)) {
                        binding.bio.setError("Please enter your bio information");
                    }
                    //Validate user education
                    else if (isEmpty(education)) {
                        binding.education.setError("Please enter your education background");
                    }
                    //Validate user skills
                    else if (isEmpty(skills)) {
                        binding.education.setError("Please enter your skills");
                    }
                    //Validate user email address
                    else if (isEmpty(email)) {
                        binding.email.setError("Please enter your email address");
                    }
                    //Validate user phone number
                    else if (isEmpty(phone)) {
                        binding.phone.setError("Please enter your phone number");
                    } else {
                        int count = dbHandler.updatePortfolio(String.valueOf(id), new User(id, uri.toString(), username, jobTitle, bio, education, skills, email, phone, web, facebook, linkedIn, latitude, longitude));
                        if (count > 0) {
                            Toast.makeText(EditProfileActivity.this, "Portfolio updated successfully ", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(EditProfileActivity.this, "We are unable to update this portfolio, please try again later. ", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(EditProfileActivity.this, "This profile has been deleted from the database", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Redirect user to view their profile
        binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, UserActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == RESULT_OK) {
                uri = data.getData();
                binding.profileImage.setImageURI(uri);
            }
        } catch (Exception ex) {
            Toast.makeText(this, "An error occured when uploading your profile picture, please try again!", Toast.LENGTH_LONG).show();
        }
    }
}
