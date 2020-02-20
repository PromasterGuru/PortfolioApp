package com.example.discover.portfolio.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.discover.R;
import com.example.discover.databinding.ActivityEducationBinding;
import com.squareup.picasso.Picasso;

import static android.text.TextUtils.isEmpty;
import static com.example.discover.portfolio.constants.Constant.AVATAR;
import static com.example.discover.portfolio.constants.Constant.EDUCATION;
import static com.example.discover.portfolio.constants.Constant.SHARED_PREFERENCE;
import static com.example.discover.portfolio.constants.Constant.SKILLS;

public class EducationActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private ActivityEducationBinding binding;
    private SharedPreferences sharedPreferences;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_education);
        editor = getSharedPreferences(SHARED_PREFERENCE, 0).edit();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, 0);

        //Display user profile picture
        imgUrl = sharedPreferences.getString(AVATAR, "");
        Picasso.get().load(imgUrl).into(binding.avatar);

        //Redirect user to the next screen to enter their contact information
        binding.next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    String education = binding.education.getText().toString();
                    String skills = binding.skills.getText().toString();

                    //Validate user education and skills entry
                    if (education == null || isEmpty(education)) {
                        binding.education.setError("Please enter your education background");
                    }
                    //Validate user education and skills entry
                    else if (skills == null || isEmpty(skills)) {
                        binding.education.setError("Please enter your skills");
                    } else {
                        Intent intent = new Intent(EducationActivity.this, ContactsActivity.class);

                        //Store user education in SharedPreferences before storing them in the SQLite database
                        editor.putString(EDUCATION, education);
                        editor.putString(SKILLS, skills);
                        editor.apply();
                        startActivity(intent);
                    }
                } catch (Exception ex) {
                    Toast.makeText(EducationActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //Redirect user to view their profile
        binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EducationActivity.this, UserActivity.class));
            }
        });

    }
}
