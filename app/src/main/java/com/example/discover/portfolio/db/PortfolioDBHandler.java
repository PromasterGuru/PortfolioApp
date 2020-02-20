package com.example.discover.portfolio.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.discover.portfolio.models.User;

import java.util.ArrayList;
import java.util.List;

public class PortfolioDBHandler extends SQLiteOpenHelper {

    // database name and version
    private static final int DB_VER = 1;
    private static final String DB_NAME = "portfolioDB";

    // table
    public static final String TABLE_PORTFOLIO = "portfolios";

    // columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE = "image_url";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_BIO = "bio";
    public static final String COLUMN_JOB_TITLE = "job_title";
    public static final String COLUMN_EDUCATION = "education";
    public static final String COLUMN_SKILLS = "skills";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_WEB = "webUrl";
    public static final String COLUMN_FACEBOOK = "facebook";
    public static final String COLUMN_LINKEDIN = "linkedIn";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    //constructor
    public PortfolioDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    // This method creates the portfolio table when the DB is initialized.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_PORTFOLIO = "CREATE TABLE " +
                TABLE_PORTFOLIO + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_JOB_TITLE + " TEXT, " +
                COLUMN_BIO + " TEXT, " +
                COLUMN_EDUCATION + " TEXT, " +
                COLUMN_SKILLS + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_WEB + " TEXT, " +
                COLUMN_FACEBOOK + " TEXT, " +
                COLUMN_LINKEDIN + " TEXT, " +
                COLUMN_LATITUDE + " TEXT, " +
                COLUMN_LONGITUDE + " TEXT);";
        try {
            db.execSQL(CREATE_TABLE_PORTFOLIO);
        }catch (Exception ex){
            Log.d("TAG", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PORTFOLIO);
        onCreate(db);
    }

    // This method is used to add a portfolio record to the database.
    public void addPortfolio(User user) {
        try {
            ContentValues values = new ContentValues();
            SQLiteDatabase db = this.getWritableDatabase();
            values.put(COLUMN_IMAGE, user.getAvatar());
            values.put(COLUMN_USERNAME, user.getUsername());
            values.put(COLUMN_JOB_TITLE, user.getJobTitle());
            values.put(COLUMN_BIO, user.getBio());
            values.put(COLUMN_EDUCATION, user.getEducation());
            values.put(COLUMN_SKILLS, user.getSkills());
            values.put(COLUMN_EMAIL, user.getEmail());
            values.put(COLUMN_PHONE, user.getPhone());
            values.put(COLUMN_WEB, user.getWebUrl());
            values.put(COLUMN_FACEBOOK, user.getFacebookUrl());
            values.put(COLUMN_LINKEDIN, user.getLinkedInUrl());
            values.put(COLUMN_LATITUDE, user.getLatitude());
            values.put(COLUMN_LONGITUDE, user.getLongitude());

            db.insert(TABLE_PORTFOLIO, null, values);
            db.close();
        }
        catch (Exception ex){
            Log.d("TAG", ex.getMessage());
        }
    }

    // This method is used to get portfolio records from the database.
    public List<User> getPortfolio() {

        List<User> portfolios = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + TABLE_PORTFOLIO;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null) {
                // move cursor to first row
                while (cursor.moveToNext()) {
                    // Get portfolio info from the cursor
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));
                    String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                    String job_title = cursor.getString(cursor.getColumnIndex(COLUMN_JOB_TITLE));
                    String bio = cursor.getString(cursor.getColumnIndex(COLUMN_BIO));
                    String education = cursor.getString(cursor.getColumnIndex(COLUMN_EDUCATION));
                    String skills = cursor.getString(cursor.getColumnIndex(COLUMN_SKILLS));
                    String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                    String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
                    String web = cursor.getString(cursor.getColumnIndex(COLUMN_WEB));
                    String facebook = cursor.getString(cursor.getColumnIndex(COLUMN_FACEBOOK));
                    String linkedIn = cursor.getString(cursor.getColumnIndex(COLUMN_LINKEDIN));
                    String latitude = cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE));
                    String longitude = cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE));

                    // add portfolio to the ArrayList
                    portfolios.add(new User(id, image, username, job_title, bio, education, skills, email, phone, web, facebook, linkedIn, latitude, longitude));
                }
            }
            else {
                Log.d("TAG", "No record found " + cursor.toString());
            }
            db.close();
        }
        catch (Exception ex){
            Log.d("TAG", ex.getMessage());
        }
        return portfolios;
    }

    //This method will allow users to update portfolios
    public int updatePortfolio(String id, User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user.getId());
        values.put(COLUMN_IMAGE, user.getAvatar());
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_JOB_TITLE, user.getJobTitle());
        values.put(COLUMN_BIO, user.getBio());
        values.put(COLUMN_EDUCATION, user.getEducation());
        values.put(COLUMN_SKILLS, user.getSkills());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_WEB, user.getWebUrl());
        values.put(COLUMN_FACEBOOK, user.getFacebookUrl());
        values.put(COLUMN_LINKEDIN, user.getLinkedInUrl());
        values.put(COLUMN_LATITUDE, user.getLatitude());
        values.put(COLUMN_LONGITUDE, user.getLongitude());

        return db.update(TABLE_PORTFOLIO, values, COLUMN_ID + " = ?", new String[] { id });
    }

    //This method will allow the user to delete a portfolio
    public int deletePortfolio(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PORTFOLIO, COLUMN_ID + " = ?", new String[] { id });
    }
}