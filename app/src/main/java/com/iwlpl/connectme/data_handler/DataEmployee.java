package com.iwlpl.connectme.data_handler;

public class DataEmployee {

    String name;
    String ID;
    String obj;


    public DataEmployee(String name, String ID, String designation, String level, String mobile, String phone, String department, String email, String related,String Oject) {
        this.name = name;
        this.ID = ID;
        Designation = designation;
        Level = level;
        Mobile = mobile;
        Phone = phone;
        Department = department;
        Email = email;
        Related = related;
        obj = Oject;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRelated() {
        return Related;
    }

    public void setRelated(String related) {
        Related = related;
    }

    String Designation;
    String Level;
    String Mobile;
    String Phone;
    String Department;
    String Email;
    String Related;

    public DataEmployee() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
