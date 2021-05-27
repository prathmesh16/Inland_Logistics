package com.iwlpl.connectme.data_handler;

public class DataMR {


    String FromLocation;
    String InvoiceNo;
    String Mramount;
    String Mrdate;
    String Mrno;
    String Packages;
    String Privatemark;
    String ReciverName;
    String Saidtocontain;
    String SenderName;
    String ToLocation;
    String Weight;
    String loadtype;
    String paybasis;

    public DataMR(String docketdate, String docketno, String doordelivery, String freight, String fromLocation, String invoiceNo, String packages, String privatemark, String reciverName, String saidtocontain, String senderName, String toLocation, String weight, String loadtype, String paybasis) {
        Mrdate = docketdate;
        Mrno = docketno;
        Mramount = doordelivery;
        Privatemark = freight;
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

    public String getMramount() {
        return Mramount;
    }

    public void setMramount(String mramount) {
        Mramount = mramount;
    }

    public String getMrdate() {
        return Mrdate;
    }

    public void setMrdate(String mrdate) {
        Mrdate = mrdate;
    }

    public String getMrno() {
        return Mrno;
    }

    public void setMrno(String mrno) {
        Mrno = mrno;
    }

    public String getPrivatemark() {
        return Privatemark;
    }

    public void setPrivatemark(String privatemark) {
        Privatemark = privatemark;
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

    public void setToLocation(String toLocation) {
        ToLocation = toLocation;
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

    public String getPackages() {
        return Packages;
    }

    public void setPackages(String packages) {
        Packages = packages;
    }


    public String getReciverName() {
        return ReciverName;
    }


    public String getToLocation() {
        return ToLocation;
    }


    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

}
