package com.ecnu.security.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Model.LanguageType;


/**
 * Created by Administrator on 2016/10/14.
 */

public class MyPreference {

    public static final int VERSION = 1;
    public static final String packName = "runner.preferences";
    private Context app;
    private SharedPreferences settings;

    private static MyPreference prefer = null;

    public static MyPreference getInstance(Context ctx) {
        if (prefer == null) {
            prefer = new MyPreference(ctx);
        }
        return prefer;
    }

    private MyPreference(Context ctx) {
        app = ctx;
        settings = app.getSharedPreferences(packName, Context.MODE_PRIVATE);
    }

    public void putVersion() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Constants.PARAM_VERSION, VERSION);
        editor.apply();
    }
    public int getVersion() {
        return settings.getInt(Constants.PARAM_VERSION, VERSION);
    }

    public void setTutorialRead(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Constants.FIRST_RUN_TAG,false);
        editor.apply();
    }

    public String getUsername() {
        return settings.getString(Constants.PARAM_USERNAME, "");
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PARAM_USERNAME, username);
        editor.apply();
    }

    public void setFogHost(String fogHost) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PARAM_FOG_HOST, fogHost);
        editor.apply();
    }

    public String getFogHost(){
        return settings.getString(Constants.PARAM_FOG_HOST,"");
    }

    public void setAppID(String appID) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PARAM_APP_ID, appID);
        editor.apply();
    }

    public String getAppID(){
        return settings.getString(Constants.PARAM_APP_ID,"");
    }

    public void setToken(String token){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PARAM_TOKEN,token);
        editor.apply();
    }

    public String getToken(){
        return settings.getString(Constants.PARAM_TOKEN,"");
    }

    public void setClientID(String clientID){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PARAM_CLIENTID,clientID);
        editor.apply();
    }

    public String getClientID(){
        return settings.getString(Constants.PARAM_CLIENTID,"");
    }

    public String getPassword() {
        return settings.getString(Constants.PARAM_PASSWORD, "");
    }

    public void setPassword(String password) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PARAM_PASSWORD, password);
        editor.apply();
    }

    public void setNickname(String name){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PARAM_NICKNAME,name);
        editor.apply();
    }

    public String getNickName(){
        return settings.getString(Constants.PARAM_NICKNAME,"");
    }

    public void setMode(String mode){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PARAM_MODE,mode);
        editor.apply();;
    }

    public String getMode(){
        return  settings.getString(Constants.PARAM_MODE,"");
    }

    public LanguageType getLanguageType() {
        String languageStr = settings.getString(Constants.PARAM_LANGUAGE,
                null);
        if (LanguageType.CHINESE.equals(languageStr)) {
            return LanguageType.CHINESE;
        } else if (LanguageType.ENGLISH.equals(languageStr)) {
            return LanguageType.ENGLISH;
        } else {
            return null;
        }
    }

    public void changeLanguage(LanguageType languageType) {
        if (languageType == null) {
            return;
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PARAM_LANGUAGE, languageType.toString());
        editor.apply();
    }

    public boolean getTutorialRead(){
        return settings.getBoolean(Constants.FIRST_RUN_TAG,true);
    }


}
