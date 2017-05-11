package com.ecnu.security;

import android.app.Application;
import android.content.Context;

import com.ecnu.security.Model.LanguageType;
import com.ecnu.security.Util.LanguageUtil;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ResourceUtil;

import net.sqlcipher.database.SQLiteDatabase;

import io.fog.helper.MiCO;

/**
 * Created by Phuylai on 2017/4/25.
 */

public class SecurityApp extends Application {
    public static String TAG = SecurityApp.class.getSimpleName();
    private static SecurityApp instance;
    private static boolean activityVisible;

    public static synchronized SecurityApp getInstance(){
        return instance;
    }
    public void onCreate(){
        super.onCreate();
        SQLiteDatabase.loadLibs(this);
        instance = this;
        ResourceUtil.initConfig(getApplicationContext());
        setPreferenceChange();
    }

    private void setPreferenceChange() {
        Context context = getApplicationContext();
        MyPreference myPreference = MyPreference.getInstance(context);
        int currentPreferenceVersion = myPreference.getVersion();
        if (currentPreferenceVersion != MyPreference.VERSION) {
            myPreference.setUsername("");
            myPreference.setPassword("");
        }
        myPreference.putVersion();
        LanguageType languageType = myPreference.getLanguageType();
        if (languageType != null) {
            LanguageUtil.changeLanguage(context, languageType);
        }
    }
    public static boolean isActivityVisible(){
        return activityVisible;
    }
    public static void activityResumed(){
        activityVisible = true;
    }
    public static void activityPaused(){
        activityVisible = false;
    }
}
