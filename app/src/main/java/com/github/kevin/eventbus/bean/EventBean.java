package com.github.kevin.eventbus.bean;

public class EventBean {
    private String name;
    private String pw;

    public EventBean(String name, String pw) {
        this.name = name;
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    @Override
    public String toString() {
        return "EventBean【name：" + name + "，pw：" + pw + "】";
    }
}
