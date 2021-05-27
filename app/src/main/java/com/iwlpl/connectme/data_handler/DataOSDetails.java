package com.iwlpl.connectme.data_handler;

public class DataOSDetails {

    String Dueamount;
    String Duedate ;
    String Invoiceamount ;
    String Invoicedate;
    String Invoiceno ;
    String PendingDays;
    String Submitteddate ;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    Boolean isChecked=false;

    public DataOSDetails() {
    }

    public DataOSDetails(String dueamount, String duedate, String invoiceamount, String invoicedate, String invoiceno, String pendingDays, String submitteddate) {
        Dueamount = dueamount;
        Duedate = duedate;
        Invoiceamount = invoiceamount;
        Invoicedate = invoicedate;
        Invoiceno = invoiceno;
        PendingDays = pendingDays;
        Submitteddate = submitteddate;
    }

    public String getDueamount() {
        return Dueamount;
    }

    public void setDueamount(String dueamount) {
        Dueamount = dueamount;
    }

    public String getDuedate() {
        return Duedate;
    }

    public void setDuedate(String duedate) {
        Duedate = duedate;
    }

    public String getInvoiceamount() {
        return Invoiceamount;
    }

    public void setInvoiceamount(String invoiceamount) {
        Invoiceamount = invoiceamount;
    }

    public String getInvoicedate() {
        return Invoicedate;
    }

    public void setInvoicedate(String invoicedate) {
        Invoicedate = invoicedate;
    }

    public String getInvoiceno() {
        return Invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        Invoiceno = invoiceno;
    }

    public String getPendingDays() {
        return PendingDays;
    }

    public void setPendingDays(String pendingDays) {
        PendingDays = pendingDays;
    }

    public String getSubmitteddate() {
        return Submitteddate;
    }

    public void setSubmitteddate(String submitteddate) {
        Submitteddate = submitteddate;
    }
}
