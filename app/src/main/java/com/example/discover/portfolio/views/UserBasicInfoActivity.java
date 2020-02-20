package com.example.discover.portfolio.views;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.example.discover.R;
import com.example.discover.databinding.ActivityUserBasicInfoBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;

import static android.text.TextUtils.isEmpty;
import static com.example.discover.portfolio.constants.Constant.*;

public class UserBasicInfoActivity extends AppCompatActivity {
    private int REQUEST_CODE_LOAD_IMAGE = 100;
    private int REQUEST_CODE_CAPTURE_IMAGE = 200;
    private ActivityUserBasicInfoBinding binding;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private Uri uri;
    private Intent pictureIntent;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_basic_info);
        editor = getSharedPreferences(SHARED_PREFERENCE, 0).edit();
        prefs = getSharedPreferences(SHARED_PREFERENCE, 0);

        //Display user profile picture
        imgUrl = prefs.getString(AVATAR, "");
        Picasso.get().load(imgUrl).into(binding.avatar);

        //Load image uri
        if (savedInstanceState != null) {
            if (uri == null && savedInstanceState.getString("uri") != null) {
                uri = Uri.parse(savedInstanceState.getString("uri"));
            }
        }

        //Load profile picture
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            private int REQUEST_IMAGE_CAPTURE = 0;
            private String TAG = "TAG";

            @Override
            public void onClick(View v) {
                try {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    //Allow the user to take a picture
                    PackageManager packageManager = UserBasicInfoActivity.this.getPackageManager();
                    if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        File mainDirectory = new File(Environment.getExternalStorageDirectory(), "PortfolioPics/tmp");
                        if (!mainDirectory.exists())
                            mainDirectory.mkdirs();

                        Calendar calendar = Calendar.getInstance();

                        uri = Uri.fromFile(new File(mainDirectory, "IMG_" + calendar.getTimeInMillis()));
                        pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(pictureIntent, REQUEST_CODE_CAPTURE_IMAGE);
                    }

                    //Allow the user to select image from file
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_LOAD_IMAGE);

                }
                catch (Exception ex){
                    Toast.makeText(UserBasicInfoActivity.this, "Please go to app info and enable all the required permissions to continue...", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Redirect user to the next screen to enter their education and skills information
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.username.getText().toString();
                String jobTitle = binding.jobTitle.getText().toString();
                String bio = binding.bio.getText().toString();
                try {
                    //Validate user basic information
                    if (uri == null || isEmpty(uri.toString())) {
                        Toast.makeText(UserBasicInfoActivity.this, "Please select your profile picture", Toast.LENGTH_LONG).show();
                    } else if (username == null || isEmpty(username)) {
                        binding.username.setError("Please enter your username");
                    } else if (jobTitle == null || isEmpty(jobTitle)) {
                        binding.jobTitle.setError("Please enter your job title");
                    } else if (bio == null || isEmpty(bio)) {
                        binding.bio.setError("Please enter your bio information");
                    } else {
                        Intent intent = new Intent(UserBasicInfoActivity.this, EducationActivity.class);

                        //Store users basic information in SharedPreferences before storing them in the SQLite database
                        editor.putString(IMAGE_URL, uri.toString());
                        editor.putString(USERNAME, username);
                        editor.putString(JOT_TITLE, jobTitle);
                        editor.putString(BIO, bio);
                        editor.apply();
                        startActivity(intent);
                    }
                } catch (Exception ex) {
                    Toast.makeText(UserBasicInfoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //Redirect user to view their profile
        binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserBasicInfoActivity.this, UserActivity.class));
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
            } else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
                binding.profileImage.setImageURI(uri);
            }
        } catch (Exception ex) {
            Toast.makeText(UserBasicInfoActivity.this, "Unable to capture and save image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (uri != null)
            outState.putString("path", uri.toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
