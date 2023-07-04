package com.example.ru_foody.chefFoodPanel;

public class Chef {

    private String FName, EmailId, MobileNo, Password, ConfirmPassword, ChefId;

    public Chef() {
        // Default constructor
    }

    public String getChefId() {
        return ChefId;
    }

    public void setChefId(String chefId) {
        ChefId = chefId;
    }

    public Chef(String chefId, String fName, String emailId, String mobileNo, String password, String confirmPassword) {
        ChefId = chefId;
        FName = fName;
        EmailId = emailId;
        MobileNo = mobileNo;
        Password = password;
        ConfirmPassword = confirmPassword;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
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
