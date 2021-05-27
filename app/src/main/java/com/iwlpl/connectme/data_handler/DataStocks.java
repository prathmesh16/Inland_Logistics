package com.iwlpl.connectme.data_handler;

public class DataStocks {
    private String DocketNo;
    private String Date;
    private String Sender;
    private String Receiver;
    private String From;
    private String To;
    private String wharehouse;

    public DataStocks(String docketNo, String date, String sender, String receiver, String from, String to, String packages, String weight,String wha) {
        DocketNo = docketNo;
        Date = date;
        Sender = sender;
        Receiver = receiver;
        From = from;
        To = to;
        Packages = packages;
        Weight = weight;
        wharehouse = wha;
    }

    private String Packages;
    private String Weight;

    public String getDocketNo() {
        return DocketNo;
    }

    public void setDocketNo(String docketNo) {
        DocketNo = docketNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getPackages() {
        return Packages;
    }

    public void setPackages(String packages) {
        Packages = packages;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getWharehouse() {
        return wharehouse;
    }

    public void setWharehouse(String wharehouse) {
        this.wharehouse = wharehouse;
    }
}
