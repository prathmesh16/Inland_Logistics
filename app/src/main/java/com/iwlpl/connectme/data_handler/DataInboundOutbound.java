package com.iwlpl.connectme.data_handler;

public class DataInboundOutbound {
    private String DocketNo;
    private String Source;
    private String date;


    public DataInboundOutbound(String docketNo, String source, String destination, String consignee, String consignor,String dt) {
        DocketNo = docketNo;
        Source = source;
        Destination = destination;
        Consignee = consignee;
        Consignor = consignor;
        date = dt;
    }

    public String getDocketNo() {
        return DocketNo;
    }

    public void setDocketNo(String docketNo) {
        DocketNo = docketNo;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getConsignee() {
        return Consignee;
    }

    public void setConsignee(String consignee) {
        Consignee = consignee;
    }

    public String getConsignor() {
        return Consignor;
    }

    public void setConsignor(String consignor) {
        Consignor = consignor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String Destination;
    private String Consignee;
    private String Consignor;

}
