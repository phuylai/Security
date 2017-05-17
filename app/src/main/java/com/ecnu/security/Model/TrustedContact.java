package com.ecnu.security.Model;

/**
 * Created by Phuylai on 2017/5/13.
 */

public class TrustedContact {
    private String phonenumber;
    private String name;

    public TrustedContact(String phonenumber, String name) {
        this.phonenumber = phonenumber;
        this.name = name;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getName() {
        return name;
    }
}
