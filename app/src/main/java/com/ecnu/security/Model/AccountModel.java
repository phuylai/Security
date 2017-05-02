package com.ecnu.security.Model;

import java.io.Serializable;

/**
 * Created by Phuylai on 2017/5/2.
 */

public class AccountModel implements Serializable{

    private String phone;
    private String password;
    private String token;
    private String clientid;

    public AccountModel(String phone, String token, String clientid) {
        this.phone = phone;
        this.token = token;
        this.clientid = clientid;
    }

    public AccountModel(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }
}
