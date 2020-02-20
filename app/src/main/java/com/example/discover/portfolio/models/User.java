package com.example.discover.portfolio.models;

public class User {

    //Private data members to hold user information
    private int id;
    private String avatar;
    private String username;
    private String skills;
    private String education;
    private String jobTitle;
    private String bio;
    private String email;
    private String phone;
    private String webUrl = "";
    private String facebookUrl = "";
    private String linkedInUrl = "";
    private String latitude = "";
    private String longitude = "";

    //Empty constructor
    public User() {
    }

    //Constructor to initialize user information
    public User(int id, String avatar, String username, String jobTitle, String bio, String education, String skills, String email, String phone, String webUrl, String facebookUrl, String linkedInUrl, String latitude, String longitude) {
        this.id = id;
        this.avatar = avatar;
        this.username = username;
        this.jobTitle = jobTitle;
        this.bio = bio;
        this.education = education;
        this.skills = skills;
        this.email = email;
        this.phone = phone;
        this.webUrl = webUrl;
        this.facebookUrl = facebookUrl;
        this.linkedInUrl = linkedInUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Get user id
    public int getId() {
        return id;
    }

    //Set user id
    public void setId(int id) {
        this.id = id;
    }

    //Get user location latitude
    public String getLatitude() {
        return latitude;
    }

    //Set user location latitude
    public void setLatitude(String latitude) { this.latitude = latitude; }

    //Get user location longitude
    public String getLongitude() { return longitude; }

    //Set user location longitude
    public void setLongitude(String longitude) { this.longitude = longitude; }

    //Get user avatar
    public String getAvatar() {
        return avatar;
    }

    //Set user avatar
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    //Getter function to get Username
    public String getUsername() {
        return username;
    }

    //Getter function to set Username
    public void setUsername(String username) {
        this.username = username;
    }

    //Getter function to get user Skills
    public String getSkills() {
        return skills;
    }

    //Getter function to set user Skills
    public void setSkills(String skills) {
        this.skills = skills;
    }

    //Getter function to get user Education Background
    public String getEducation() {
        return education;
    }

    //Getter function to set user education background
    public void setEducation(String education) {
        this.education = education;
    }

    //Getter function to get user Job Title
    public String getJobTitle() {
        return jobTitle;
    }

    //Getter function to set user Job Title
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    //Getter function to get user Bio
    public String getBio() {
        return bio;
    }

    //Getter function to set user Bio
    public void setBio(String bio) {
        this.bio = bio;
    }

    //Getter function to get user Email Address
    public String getEmail() {
        return email;
    }

    //Getter function to set user Email Address
    public void setEmail(String email) {
        this.email = email;
    }

    //Getter function to get user Phone Number
    public String getPhone() {
        return phone;
    }

    //Getter function to set user Phone Number
    public void setPhone(String phone) {
        this.phone = phone;
    }

    //Getter function to get user Website
    public String getWebUrl() {
        return webUrl;
    }

    //Getter function to set user Website
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    //Getter function to get user Facebook url
    public String getFacebookUrl() {
        return facebookUrl;
    }

    //Getter function to set user Facebook url
    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    //Getter function to set user LinkedIn url
    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    //Getter function to get user LinkedIn url
    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }
}
