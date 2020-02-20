package com.example.discover.portfolio.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.discover.R;
import com.example.discover.databinding.ActivityLoginBinding;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.example.discover.portfolio.constants.Constant.AVATAR;
import static com.example.discover.portfolio.constants.Constant.CURRENT_EMAIL;
import static com.example.discover.portfolio.constants.Constant.CURRENT_USERNAME;
import static com.example.discover.portfolio.constants.Constant.SHARED_PREFERENCE;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private CallbackManager callbackManager;
    private SharedPreferences.Editor editor;
    int TAG_CODE_PERMISSION_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        editor = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE, 0).edit();
        TAG_CODE_PERMISSION_LOCATION = 1;

        //Create a callbackManager to handle login responses
        callbackManager = CallbackManager.Factory.create();

        //Enable permissions to read user email and profile
        binding.loginButton.setPermissions(Arrays.asList("email", "public_profile"));


        //Required permissions
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        //Prompt user to grant permissions if not granted
        if (!hasPermissions(LoginActivity.this, permissions)) {
            ActivityCompat.requestPermissions(LoginActivity.this, permissions, TAG_CODE_PERMISSION_LOCATION);
            LoginManager.getInstance().logOut();
        }
        //Validate user login status
        checkLoginStatus();

        //Register callback with login button to respond to login result
        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {
                binding.message.setText("Login request terminated...");
            }

            @Override
            public void onError(FacebookException error) {
                binding.message.setText(error.getMessage());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Pass the login results to the login button via callbackManager
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * AccessTokenTracker enables the app to keep up with the current access token and profile
     * The class is extended to receive notifications of access token changes
     */
    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null || currentAccessToken.isExpired()) {

                //Display default profile information when the user is not logged In
                binding.email.setText("Please Login!");
                binding.name.setText("");
                binding.message.setText("");
                binding.avatar.setImageResource(R.drawable.default_avatar);
            } else {
                //Load and display user profile information when token is valid
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken accessToken) {

        // Use GraphRequest to get user data from the current user AccessToken
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                //Use Try Catch block to handle runtime exceptions
                try {
                    //Extract user profile information from graph request response
                    String firstName = object.getString("first_name");
                    String lastName = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String imageUrl = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    //Save current user details in SharedPreference
                    editor.putString(AVATAR, imageUrl);
                    editor.putString(CURRENT_EMAIL, email);
                    editor.putString(CURRENT_USERNAME, firstName + " " + lastName);
                    editor.apply();

                    //Display user profile information in their respective fields
                    Picasso.get().load(imageUrl).into(binding.avatar);
                    binding.name.setText(firstName + " " + lastName);
                    binding.email.setText(email);
                    binding.message.setText("Welcome " + firstName);
                    binding.message.setTextColor(Color.parseColor("#386F96"));

                    //Redirect authenticated users to the main activity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Define parameters of fields to fetch from the user facebook profile and
         * display them in the app
         */
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    /**
     * Check the user login status
     * Login the user automatically if their token hasn't expired
     */
    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
