package com.cmu.backend.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Created by xing on 3/31/15.
 */

@Entity
public class Request implements Serializable, Comparable<Request>{
    @Id
    private Long id;       //Key
    @Index
    private String requester;
    @Index
    private String acceptor;
    private String itemName;
    private double itemPrice;
    @Index
    private double latitude;
    @Index
    private double longitude;
    private long deadline;
    private boolean invalid;
    private String url;
    public enum Status { STARTED, ONTHEWAY, ARRIVED };
    private Status status;
    private double distance;
    private String address;
    private String phoneNumber;
    @Index
    private boolean accepted;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }



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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
