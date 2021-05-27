package com.iwlpl.connectme.data_handler;

public class DataProuduct {

    String ProductCode;
    String ProductName;

    public DataProuduct() {
    }

    public DataProuduct(String productCode, String productName) {
        ProductCode = productCode;
        ProductName = productName;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }
}
