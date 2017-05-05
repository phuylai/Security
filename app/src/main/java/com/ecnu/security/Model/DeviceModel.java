package com.ecnu.security.Model;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Util.StringUtil;

import java.io.Serializable;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class DeviceModel implements Serializable{
    private String name;
    private String IP;
    private String port;
    private String mac;
    private String FogProductId;
    private String haveSuperUser;
    private String model;
    private String protocol;

    private String devPW;
    private String isSub;
    private String role;
    private String online;
    private String devId;

    public DeviceModel(String name, String mac, String devPW, String isSub,
                       String role, String online, String devId) {
        this.name = name;
        this.mac = mac;
        this.devPW = devPW;
        this.isSub = isSub;
        this.role = role;
        this.online = online;
        this.devId = devId;
    }

    public DeviceModel(String name, String IP, String port, String mac, String fogProductId,
                       String haveSuperUser, String model, String protocol) {
        this.name = name;
        this.IP = IP;
        this.port = port;
        this.mac = mac;
        FogProductId = fogProductId;
        this.haveSuperUser = haveSuperUser;
        this.model = model;
        this.protocol = protocol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getFogProductId() {
        return FogProductId;
    }

    public void setFogProductId(String fogProductId) {
        FogProductId = fogProductId;
    }

    public String isHaveSuperUser() {
        return haveSuperUser;
    }

    public void setHaveSuperUser(String haveSuperUser) {
        this.haveSuperUser = haveSuperUser;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getHaveSuperUser() {
        return haveSuperUser;
    }

    public String getDevPW() {
        return devPW;
    }

    public void setDevPW(String devPW) {
        this.devPW = devPW;
    }

    public String getIsSub() {
        return isSub;
    }

    public void setIsSub(String isSub) {
        this.isSub = isSub;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isHaveSuperUser(String user){
        return !user.equals(Constants.PARAM_UNCHECK);
    }
}
