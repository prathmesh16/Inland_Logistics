package com.iwlpl.connectme.data_handler;

public class DataDoorDelivery {

    String DDchrg;
    String Docketdate;
    String Docketno;
    String Doordelivery;
    String Freight;
    String FromLocation;
    String Packages;
    String ReciverName;
    String Saidtocontain;
    String SenderName;
    String ToLocation;
    String Weight;
    Boolean isSelected;

    public DataDoorDelivery(String DDchrg, String docketdate, String docketno, String doordelivery, String freight, String fromLocation, String packages, String reciverName, String saidtocontain, String senderName, String toLocation, String weight) {
        this.DDchrg = DDchrg;
        Docketdate = docketdate;
        Docketno = docketno;
        Doordelivery = doordelivery;
        Freight = freight;
        FromLocation = fromLocation;
        Packages = packages;
        ReciverName = reciverName;
        Saidtocontain = saidtocontain;
        SenderName = senderName;
        ToLocation = toLocation;
        Weight = weight;
        isSelected = false;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getDDchrg() {
        return DDchrg;
    }

    public void setDDchrg(String DDchrg) {
        this.DDchrg = DDchrg;
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

    public String getPackages() {
        return Packages;
    }

    public void setPackages(String packages) {
        Packages = packages;
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
}
