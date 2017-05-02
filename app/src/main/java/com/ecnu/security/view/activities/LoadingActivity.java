package com.ecnu.security.view.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.MainActivity;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.Util.StringUtil;

import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCOUser;
import io.fog.helper.MiCO;

/**
 * Created by Phuylai on 2017/4/27.
 */

public class LoadingActivity extends BaseActivity{

    //UI
    private View iv_loading;
    private View rl_background;
    private MyPreference myPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        layoutId = R.layout.activity_loading;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findViews() {
        super.findViews();
        iv_loading = findViewById(R.id.iv_loading);
        rl_background = findViewById(R.id.rl_background);
        try{
            Resources resources = getResources();
            Bitmap loadingBackground = BitmapFactory.decodeResource(resources,R.drawable.loading_background);
            rl_background.setBackground(new BitmapDrawable(loadingBackground));
            //Bitmap logo = BitmapFactory.decodeResource(resources, R.drawable.logo);
            //iv_loading.setBackground(new BitmapDrawable(logo));
            loadingBackground = null;
            //logo = null;
        }catch (OutOfMemoryError e){

        }
        switchUI();
    }

    @Override
    protected void initActionBar() {

    }

    @Override
    protected void setListeners() {

    }

    private void switchUI(){
        myPreference = MyPreference.getInstance(this);
        String fogHost = myPreference.getFogHost();
        String appID = myPreference.getAppID();
        String token = myPreference.getToken();
        if(!StringUtil.isNull(fogHost)){
            MiCO.init(fogHost);
        }
        if(!StringUtil.isNull(appID)){
            Constants._APPID = appID;
        }
        if(!StringUtil.isNull(token)){
            MiCOUser miCOUser = new MiCOUser();
            miCOUser.refreshToken(token, new MiCOCallBack() {
                @Override
                public void onSuccess(String message) {
                    myPreference.setToken(JsonHelper.getFogToken(message));
                    goToMainActivity();
                }

                @Override
                public void onFailure(int code, String message) {
                    goToLogin();
                }
            });
        }else{
            goToLogin();
        }
    }

    private void goToMainActivity(){
        Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToLogin(){
        Intent intent = new Intent(LoadingActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ResourceUtil.recycleBackground(rl_background);
        ResourceUtil.recycleBackground(iv_loading);
    }

}
