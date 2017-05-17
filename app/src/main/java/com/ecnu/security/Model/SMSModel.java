package com.ecnu.security.Model;

import com.ecnu.security.Helper.Constants;

/**
 * Created by Phuylai on 2017/5/14.
 */

public class SMSModel {
    private String accountSid;
    private String smsContent;
    private String to;
    private String portNumber;
    private String timestamp;
    private String sig;
    private String respDataType;

    public String toString(){
        return "accountSid=" + getAccountSid() + "&to=" + getTo() + "&smsContent=" + getSmsContent()
                + "&timestamp=" + getTimestamp() + "&sig=" + getSig() + "&respDataType=" + Constants.RESP_DATA_TYPE;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getRespDataType() {
        return respDataType;
    }

    public void setRespDataType(String respDataType) {
        this.respDataType = respDataType;
    }
}
