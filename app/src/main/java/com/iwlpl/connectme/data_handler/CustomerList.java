package com.iwlpl.connectme.data_handler;
import android.graphics.drawable.Drawable;

public class CustomerList {

    String CustomerCode;
    String CUstomerName;

    public CustomerList(String customerCode, String CUstomerName) {
        CustomerCode = customerCode;
        this.CUstomerName = CUstomerName;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCUstomerName() {
        return CUstomerName;
    }

    public void setCUstomerName(String CUstomerName) {
        this.CUstomerName = CUstomerName;
    }

}
