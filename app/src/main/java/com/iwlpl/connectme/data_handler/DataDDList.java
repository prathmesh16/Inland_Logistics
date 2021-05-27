package com.iwlpl.connectme.data_handler;

public class DataDDList {

    String DeliveryDatetime;
    String DocketCount;
    String QuantityCount;
    String WeightCount;

    String docketList[];
    String quantityList[];
    String weightList[];


    public DataDDList(String deliveryDatetime, String docketCount, String quantityCount, String weightCount, String[] docket, String[] quantity, String[] weight) {
        DeliveryDatetime = deliveryDatetime;
        DocketCount = docketCount;
        QuantityCount = quantityCount;
        WeightCount = weightCount;
        this.docketList = docket;
        this.quantityList = quantity;
        this.weightList = weight;
    }

    public String getDeliveryDatetime() {
        return DeliveryDatetime;
    }

    public void setDeliveryDatetime(String deliveryDatetime) {
        DeliveryDatetime = deliveryDatetime;
    }

    public String getDocketCount() {
        return DocketCount;
    }

    public void setDocketCount(String docketCount) {
        DocketCount = docketCount;
    }

    public String getQuantityCount() {
        return QuantityCount;
    }

    public void setQuantityCount(String quantityCount) {
        QuantityCount = quantityCount;
    }

    public String getWeightCount() {
        return WeightCount;
    }

    public void setWeightCount(String weightCount) {
        WeightCount = weightCount;
    }

    public String[] getDocketList() {
        return docketList;
    }

    public void setDocketList(String[] docketList) {
        this.docketList = docketList;
    }

    public String[] getQuantityList() {
        return quantityList;
    }

    public void setQuantityList(String[] quantityList) {
        this.quantityList = quantityList;
    }

    public String[] getWeightList() {
        return weightList;
    }

    public void setWeightList(String[] weightList) {
        this.weightList = weightList;
    }
}


