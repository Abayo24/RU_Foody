package com.example.ru_foody;

public class Customer {

    private String FullName, MobileNo, Email, Password, ConfirmPassword;

    public Customer(){

    }

    public Customer(String fullName, String mobileNo, String email, String password, String confirmPassword) {
        FullName = fullName;
        MobileNo = mobileNo;
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
}
