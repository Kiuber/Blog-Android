package com.kiuber.blog.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/12/2.
 */

public class Tint extends BmobObject {
    private String device;
    private String location;
    private String location_all;
    private String address;
    private String content;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private String label;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation_all() {
        return location_all;
    }

    public void setLocation_all(String location_all) {
        this.location_all = location_all;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
