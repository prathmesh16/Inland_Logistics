package com.iwlpl.connectme.data_handler;

public class DataMaster {

    String name;
    String address;
    String contactPerson;
    String contactNumber;
    String addressID;
    String obj;


    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public DataMaster() {
    }

    public DataMaster(String name, String address, String contactPerson, String contactNumber,String addressID,String obj) {
        this.name = name;
        this.address = address;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
        this.addressID=addressID;
        this.obj=obj;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return name + " " +address;
        }
    }
