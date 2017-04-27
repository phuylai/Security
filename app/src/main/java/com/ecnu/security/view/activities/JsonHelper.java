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
}
