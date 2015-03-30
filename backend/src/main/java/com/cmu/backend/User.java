package com.cmu.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by xing on 3/28/15.
 */

@Entity
public class User {
    @Id private String email;       //Key
    private String userName;
    private String password;
    private String address;         //Optional when register
    private String phoneNumber;     //Optional when register
    private String location;        //Optional
    private boolean loggedIn;       //To indicate if the user is logged in
    private boolean canSignUp;

    public boolean isCanSignUp() {
        return canSignUp;
    }

    public void setCanSignUp(boolean canSignUp) {
        this.canSignUp = canSignUp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
