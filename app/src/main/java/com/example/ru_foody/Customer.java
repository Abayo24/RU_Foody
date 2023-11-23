package com.example.ru_foody;

import com.google.firebase.database.PropertyName;

public class Customer {

    private String ConfirmPassword, Email, FullName, MobileNo, Password;

    public Customer(){

    }

    public Customer(String confirmPassword, String email, String fullName, String mobileNo,  String password) {
        ConfirmPassword = confirmPassword;
        Email = email;
        FullName = fullName;
        MobileNo = mobileNo;
        Password = password;
    }

    @PropertyName("Confirm Password")
    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    @PropertyName("Confirm Password")
    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    @PropertyName("Email")
    public String getEmail() {
        return Email;
    }

    @PropertyName("Email")
    public void setEmail(String email) {
        Email = email;
    }

    @PropertyName("Full Name")
    public String getFullName() {
        return FullName;
    }

    @PropertyName("Full Name")
    public void setFullName(String fullName) {
        FullName = fullName;
    }

    @PropertyName("Mobile Number")
    public String getMobileNo() {
        return MobileNo;
    }

    @PropertyName("Mobile Number")
    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    @PropertyName("Password")
    public String getPassword() {
        return Password;
    }

    @PropertyName("Password")
    public void setPassword(String password) {
        Password = password;
    }

}
