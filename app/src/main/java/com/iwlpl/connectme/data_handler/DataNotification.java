package com.iwlpl.connectme.data_handler;

public class DataNotification {
    String title;
    String time;

    public DataNotification(String title, String time, String msg) {
        this.title = title;
        this.time = time;
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    String msg;
}
