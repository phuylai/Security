package com.ecnu.security.view.activities;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Model.AlertDevice;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phuylai on 2017/4/27.
 */

public class JsonHelper {

    public static String getFogToken(String message) {
        try {
            JSONObject object = new JSONObject(message);
            if(object.has(Constants.PARAM_TOKEN)){
                return object.getString(Constants.PARAM_TOKEN);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getClientId(String message) {
        try {
            JSONObject object = new JSONObject(message);
            if(object.has(Constants.PARAM_CLIENTID)){
                return object.getString(Constants.PARAM_CLIENTID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getNickName(String message) {
        try {
            JSONObject object = new JSONObject(message);
            if(object.has(Constants.PARAM_DATA)){
                String data = object.getString(Constants.PARAM_DATA);
                JSONObject info = new JSONObject(data);
                String nickname = info.getString(Constants.PARAM_NICKNAME);
                return nickname;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getNote(String message) {
        try {
            JSONObject object = new JSONObject(message);
            if(object.has(Constants.PARAM_DATA)){
                String data = object.getString(Constants.PARAM_DATA);
                JSONObject info = new JSONObject(data);
                String note = info.getString(Constants.PARAM_NOTE);
                return note;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getRealname(String message) {
        try {
            JSONObject object = new JSONObject(message);
            if(object.has(Constants.PARAM_DATA)){
                String data = object.getString(Constants.PARAM_DATA);
                JSONObject info = new JSONObject(data);
                String realname = info.getString(Constants.PARAM_REALNAME);
                return realname;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDeviceId(String message) {
        try {
            JSONObject object = new JSONObject(message);
            if(object.has(Constants.PARAM_DATA)){
                String data = object.getString(Constants.PARAM_DATA);
                JSONObject info = new JSONObject(data);
                String id = info.getString(Constants.PARAM_DEVICE_ID);
                return id;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getIR(String message) {
        try {
            JSONObject object = new JSONObject(message);
            if(object.has(Constants.PARAM_PAYLOAD)){
                String data = object.getString(Constants.PARAM_PAYLOAD);
                JSONObject info = new JSONObject(data);
                String deviceinfo = info.getString(Constants.PARAM_DEVICEINFO);
                JSONObject device = new JSONObject(deviceinfo);
                int ir = device.getInt(Constants.PARAM_IR);
                return ir;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static AlertDevice getAlertDevice(String message){
        AlertDevice alertDevice = new AlertDevice();
        JSONObject object = null;
        try {
            object = new JSONObject(message);
            if(object.has(Constants.PARAM_PAYLOAD)){
                String data = object.getString(Constants.PARAM_PAYLOAD);
                JSONObject info = new JSONObject(data);
                String deviceinfo = info.getString(Constants.PARAM_DEVICEINFO);
                JSONObject device = new JSONObject(deviceinfo);
                String id = device.getString(Constants.PARAM_DEV_ID);
                String module = info.getString(Constants.MODULE);
                alertDevice.setDevice_id(id);
                alertDevice.setModule(module);
                return alertDevice;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getData(String message) {
        try {
            JSONObject object = new JSONObject(message);
            if(object.has(Constants.PARAM_DATA)){
               return object.getString(Constants.PARAM_DATA);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
