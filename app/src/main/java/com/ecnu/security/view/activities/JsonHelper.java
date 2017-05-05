package com.ecnu.security.view.activities;

import com.ecnu.security.Helper.Constants;

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
