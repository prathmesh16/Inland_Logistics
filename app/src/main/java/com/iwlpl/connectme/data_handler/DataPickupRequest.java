package com.iwlpl.connectme.data_handler;

public class DataPickupRequest {
    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getDeclareValue() {
        return DeclareValue;
    }

    public void setDeclareValue(String declareValue) {
        DeclareValue = declareValue;
    }

    public String getPickup_Address() {
        return Pickup_Address;
    }

    public void setPickup_Address(String pickup_Address) {
        Pickup_Address = pickup_Address;
    }

    public String getPickup_AddressID() {
        return Pickup_AddressID;
    }

    public void setPickup_AddressID(String pickup_AddressID) {
        Pickup_AddressID = pickup_AddressID;
    }

    public String getPickup_Date() {
        return Pickup_Date;
    }

    public void setPickup_Date(String pickup_Date) {
        Pickup_Date = pickup_Date;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String Productname) {
        Productname = Productname;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getReceiver_Address() {
        return Receiver_Address;
    }

    public void setReceiver_Address(String receiver_Address) {
        Receiver_Address = receiver_Address;
    }

    public String getReceiver_AddressID() {
        return Receiver_AddressID;
    }

    public void setReceiver_AddressID(String receiver_AddressID) {
        Receiver_AddressID = receiver_AddressID;
    }

    public String getReceiver_ContactMobileNo() {
        return Receiver_ContactMobileNo;
    }

    public void setReceiver_ContactMobileNo(String receiver_ContactMobileNo) {
        Receiver_ContactMobileNo = receiver_ContactMobileNo;
    }

    public String getReceiver_ContactName() {
        return Receiver_ContactName;
    }

    public void setReceiver_ContactName(String receiver_ContactName) {
        Receiver_ContactName = receiver_ContactName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getRequestID() {
        return RequestID;
    }

    public void setRequestID(String requestID) {
        RequestID = requestID;
    }

    public String getRequested_By() {
        return Requested_By;
    }

    public void setRequested_By(String requested_By) {
        Requested_By = requested_By;
    }

    public String getSaidContains() {
        return SaidContains;
    }

    public void setSaidContains(String saidContains) {
        SaidContains = saidContains;
    }

    public String getSender_ContactMobileNo() {
        return Sender_ContactMobileNo;
    }

    public void setSender_ContactMobileNo(String sender_ContactMobileNo) {
        Sender_ContactMobileNo = sender_ContactMobileNo;
    }

    public String getSender_ContactName() {
        return Sender_ContactName;
    }

    public void setSender_ContactName(String sender_ContactName) {
        Sender_ContactName = sender_ContactName;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    String CustomerName;
    String DeclareValue;
    String Pickup_Address;
    String Pickup_AddressID;
    String Pickup_Date;
    String Productname;
    String Quantity;
    String Receiver_Address;
    String Receiver_AddressID;
    String invoiceNo;
    String invoiceValue;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(String invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getLoadType() {
        return LoadType;
    }

    public void setLoadType(String loadType) {
        this.LoadType = loadType;
    }

    String LoadType;

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    String Weight;

    public DataPickupRequest(String pick_id,String customerName, String declareValue, String pickup_Address, String pickup_AddressID,
                             String pickup_Date,String productname, String quantity, String receiver_Address,
                             String receiver_AddressID, String receiver_ContactMobileNo,
                             String receiver_ContactName, String remark, String requestID, String requested_By,
                             String saidContains, String sender_ContactMobileNo, String sender_ContactName,
                             String userID,String weight,String loadType)
    {
        id = pick_id;
        CustomerName = customerName;
        DeclareValue = declareValue;
        Pickup_Address = pickup_Address;
        Pickup_AddressID = pickup_AddressID;
        Pickup_Date = pickup_Date;
        Productname = productname;
        Quantity = quantity;
        Receiver_Address = receiver_Address;
        Receiver_AddressID = receiver_AddressID;
        Receiver_ContactMobileNo = receiver_ContactMobileNo;
        Receiver_ContactName = receiver_ContactName;
        Remark = remark;
        RequestID = requestID;
        Requested_By = requested_By;
        SaidContains = saidContains;
        Sender_ContactMobileNo = sender_ContactMobileNo;
        Sender_ContactName = sender_ContactName;
        UserID = userID;
        Weight=weight;
        LoadType=loadType;
    }

    String Receiver_ContactMobileNo;
    String Receiver_ContactName;
    String Remark;
    String RequestID;
    String Requested_By;
    String SaidContains;
    String Sender_ContactMobileNo;
    String Sender_ContactName;
    String UserID;
    String obj;
}
