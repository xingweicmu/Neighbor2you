package com.cmu.neighbor2you.model;


import java.io.Serializable;

/**
 * Created by xing on 3/28/15.
 */


public class User implements Serializable {
    private String email;       //Key
    private String userName;
    private String password;
    private String address;         //Optional when register
    private String phoneNumber;     //Optional when register
    private String location;        //Optional
//    private boolean loggedIn;       //To indicate if the user is logged in

    public User(){}

    public User(String userName, String password, String address, String phoneNumber, String location) {
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
//        this.location = location;
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

    public String toString(){
        return "email: "+email+"\tuserName: "+userName+"\tpassword: "+password+"\taddress: "+address+"\tphoneNumber: "+phoneNumber+"\tlocation: "+location;
    }

}
