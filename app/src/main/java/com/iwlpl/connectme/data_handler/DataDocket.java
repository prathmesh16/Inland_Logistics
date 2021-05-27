package com.iwlpl.connectme.data_handler;

public class DataDocket {


        String Docketdate;
        String Docketno;
        String Doordelivery;
        String Freight;
        String FromLocation;
        String InvoiceNo;
        String Packages;
        String Privatemark;
        String ReciverName;
        String Saidtocontain;
        String SenderName;
        String ToLocation;
        String Weight;
        String loadtype;
        String paybasis;

    public DataDocket(String docketdate, String docketno, String doordelivery, String freight, String fromLocation, String invoiceNo, String packages, String privatemark, String reciverName, String saidtocontain, String senderName, String toLocation, String weight, String loadtype, String paybasis) {
        Docketdate = docketdate;
        Docketno = docketno;
        Doordelivery = doordelivery;
        Freight = freight;
        FromLocation = fromLocation;
        InvoiceNo = invoiceNo;
        Packages = packages;
        Privatemark = privatemark;
        ReciverName = reciverName;
        Saidtocontain = saidtocontain;
        SenderName = senderName;
        ToLocation = toLocation;
        Weight = weight;
        this.loadtype = loadtype;
        this.paybasis = paybasis;
    }

    public String getDocketdate() {
        return Docketdate;
    }

    public void setDocketdate(String docketdate) {
        Docketdate = docketdate;
    }

    public String getDocketno() {
        return Docketno;
    }

    public void setDocketno(String docketno) {
        Docketno = docketno;
    }

    public String getDoordelivery() {
        return Doordelivery;
    }

    public void setDoordelivery(String doordelivery) {
        Doordelivery = doordelivery;
    }

    public String getFreight() {
        return Freight;
    }

    public void setFreight(String freight) {
        Freight = freight;
    }

    public String getFromLocation() {
        return FromLocation;
    }

    public void setFromLocation(String fromLocation) {
        FromLocation = fromLocation;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getPackages() {
        return Packages;
    }

    public void setPackages(String packages) {
        Packages = packages;
    }

    public String getPrivatemark() {
        return Privatemark;
    }

    public void setPrivatemark(String privatemark) {
        Privatemark = privatemark;
    }

    public String getReciverName() {
        return ReciverName;
    }

    public void setReciverName(String reciverName) {
        ReciverName = reciverName;
    }

    public String getSaidtocontain() {
        return Saidtocontain;
    }

    public void setSaidtocontain(String saidtocontain) {
        Saidtocontain = saidtocontain;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getToLocation() {
        return ToLocation;
    }

    public void setToLocation(String toLocation) {
        ToLocation = toLocation;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getLoadtype() {
        return loadtype;
    }

    public void setLoadtype(String loadtype) {
        this.loadtype = loadtype;
    }

    public String getPaybasis() {
        return paybasis;
    }

    public void setPaybasis(String paybasis) {
        this.paybasis = paybasis;
    }
}
