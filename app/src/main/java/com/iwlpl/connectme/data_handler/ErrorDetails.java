package com.iwlpl.connectme.data_handler;

public class ErrorDetails {


        String Activity_name;
        String CustomerCode;
        String Date;
        String Error_class;
        String Error_message;
        String Pagename;
        String Servicename;
        String UserId;
        int id;


        public ErrorDetails(int id,String activity_name, String customerCode, String date, String error_class, String error_message) {
                Activity_name = activity_name;
                CustomerCode = customerCode;
                Date = date;
                Error_class = error_class;
                Error_message = error_message;
                Pagename = "";
                Servicename = "";
                UserId = "";
                this.id=id;
        }

        public String getActivity_name() {
                return Activity_name;
        }

        public void setActivity_name(String activity_name) {
                Activity_name = activity_name;
        }

        public String getCustomerCode() {
                return CustomerCode;
        }

        public void setCustomerCode(String customerCode) {
                CustomerCode = customerCode;
        }

        public String getDate() {
                return Date;
        }

        public void setDate(String date) {
                Date = date;
        }

        public String getError_class() {
                return Error_class;
        }

        public void setError_class(String error_class) {
                Error_class = error_class;
        }

        public String getError_message() {
                return Error_message;
        }

        public void setError_message(String error_message) {
                Error_message = error_message;
        }

        public String getPagename() {
                return Pagename;
        }

        public void setPagename(String pagename) {
                Pagename = pagename;
        }

        public String getServicename() {
                return Servicename;
        }

        public void setServicename(String servicename) {
                Servicename = servicename;
        }

        public String getUserId() {
                return UserId;
        }

        public void setUserId(String userId) {
                UserId = userId;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }
}
