package com.example.ru_foody;

import com.google.firebase.database.PropertyName;

public class Chef {

    private String confirmPassword, emailId,fname, mobileNo, password, chefId;

    public Chef() {
        // Default constructor
    }

    public Chef(String confirmPassword, String emailId, String fname, String mobileNo, String password,  String chefId) {
        this.confirmPassword = confirmPassword;
        this.emailId = emailId;
        this.fname = fname;
        this.mobileNo = mobileNo;
        this.password = password;
        this.chefId = chefId;
    }

    @PropertyName("Confirm Password")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @PropertyName("Confirm Password")
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @PropertyName("Email")
    public String getEmailId() {
        return emailId;
    }

    @PropertyName("Email")
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @PropertyName("Full Name")
    public String getFname() {
        return fname;
    }

    @PropertyName("Full Name")
    public void setFname(String fname) {
        this.fname = fname;
    }

    @PropertyName("Mobile Number")
    public String getMobileNo() {
        return mobileNo;
    }

    @PropertyName("Mobile Number")
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @PropertyName("Password")
    public String getPassword() {
        return password;
    }

    @PropertyName("Password")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getChefId() {
        return chefId;
    }

    public void setChefId(String chefId) {
        this.chefId = chefId;
    }
}