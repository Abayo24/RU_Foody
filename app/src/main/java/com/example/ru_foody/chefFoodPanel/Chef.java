package com.example.ru_foody.chefFoodPanel;

import com.google.firebase.database.PropertyName;

public class Chef {

    public String fname, emailId, mobileNo, password, confirmPassword, chefId;

    public Chef() {
        // Default constructor
    }

    public Chef(String fname, String emailId, String mobileNo, String password, String confirmPassword, String chefId) {
        this.fname = fname;
        this.emailId = emailId;
        this.mobileNo = mobileNo;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.chefId = chefId;
    }

    @PropertyName("Full Name")
    public String getFname() {
        return fname;
    }

    @PropertyName("Full Name")
    public void setFname(String fname) {
        this.fname = fname;
    }

    @PropertyName("Email")
    public String getEmailId() {
        return emailId;
    }

    @PropertyName("Email")
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @PropertyName("Mobile Number")
    public String getMobileNo() {
        return mobileNo;
    }

    @PropertyName("Mobile Number")
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getConfirmPassword() {
        return confirmPassword;
    }


    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getChefId() {
        return chefId;
    }

    public void setChefId(String chefId) {
        this.chefId = chefId;
    }
}