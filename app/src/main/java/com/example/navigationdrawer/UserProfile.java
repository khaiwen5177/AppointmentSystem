package com.example.navigationdrawer;

public class UserProfile {

    public String userEmail;
    public String userPhone;
    public String userName;
    public String userType;

    public UserProfile(){

    }

    public UserProfile(String userEmail, String userPhone, String userName, String userType) {
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userName = userName;
        this.userType = userType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}