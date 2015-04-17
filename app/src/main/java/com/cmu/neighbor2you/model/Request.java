package com.cmu.neighbor2you.model;

import java.io.Serializable;

/**
 * Created by xing on 3/31/15.
 */
public class Request implements Serializable, Comparable<Request>{
    private Long id;       //Key
    private String requester;
    private String acceptor;
    private String itemName;
    private double itemPrice;
    private double latitude;
    private double longitude;
    private long deadline;
    private boolean invalid;
    private String url;
    private String status;
    private double distance;
    private String address;
    private String phoneNumber;

    public Request() {
    }

    public Request(String requester, String itemName, double itemPrice, double latitude, double longitude, long deadline, boolean invalid) {
        this.requester = requester;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.latitude = latitude;
        this.longitude = longitude;
        this.deadline = deadline;
        this.invalid = invalid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequester() {
        return requester;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
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

    public String toString() {
        return "id: " + id + "\trequester: " + requester + "\tacceptor: " + acceptor +
                "\titemName: " + itemName + "\titemPrice: " + itemPrice + "\tlatitude: " +
                latitude + "\tlongitude: " + longitude + "\tdeadline: " + deadline +
                "\tinvalid: " + invalid + url + "\tstatus: " + status + "\tdistance: " + distance;
    }

    public int compareTo(Request request) {

        double distance1 = ((Request) request).getDistance();

        //ascending order
        if((this.distance - distance1)<0)
            return -1;
        else if(this.distance - distance1>0)
            return 1;
        else
            return 0;

        //descending order
        //return compareQuantity - this.quantity;

    }
}
