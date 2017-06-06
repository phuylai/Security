package com.ecnu.security.Model;

import java.io.Serializable;

/**
 * Created by Phuylai on 2017/5/16.
 */

public class AlertDevice implements Serializable{
    private String device_id;
    private String device_pw;
    private String module;

    public AlertDevice(){

    }

    public AlertDevice(String device_id, String module) {
        this.device_id = device_id;
        this.module = module;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDevice_pw() {
        return device_pw;
    }

    public void setDevice_pw(String device_pw) {
        this.device_pw = device_pw;
    }
}
