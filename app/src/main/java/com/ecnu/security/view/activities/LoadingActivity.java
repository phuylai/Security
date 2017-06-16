package com.ecnu.security.view.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.MainActivity;
import com.ecnu.security.Model.AlertDevice;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.Model.MicoUserExt;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.Util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.fog.callbacks.ControlDeviceCallBack;
import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCODevice;
import io.fog.fog2sdk.MiCOUser;
import io.fog.helper.Configuration;
import io.fog.helper.ListenDevParFog;
import io.fog.helper.MiCO;

/**
 * Created by Phuylai on 2017/4/27.
 */

public class LoadingActivity extends BaseActivity{

    private static String TAG = LoadingActivity.class.getSimpleName();

    private List<DeviceModel> deviceModelList = new ArrayList<>();

    //UI
    private View iv_loading;
    private View rl_background;
    private MyPreference myPreference;

    private AlertDevice alertDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        layoutId = R.layout.activity_loading;
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
    }

    protected void onNewIntent(Intent intent){
        if(intent == null)
            return;
        Bundle bundle = intent.getExtras();
        if(bundle == null)
            return;
        alertDevice = (AlertDevice) bundle.getSerializable(Constants.PARAM_VALUE);
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
                    String token = JsonHelper.getFogToken(message);
                    String clientId = JsonHelper.getClientId(message);
                    myPreference.setToken(token);
                    myPreference.setClientID(clientId);
                    getUserInfo(token);
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

    private void getUserInfo(String token){
        MicoUserExt micoUserExt = new MicoUserExt();
        micoUserExt.getUserInfo(new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                String nickname = JsonHelper.getNickName(message);
                String mode = JsonHelper.getRealname(message);
                if(!StringUtil.isNull(mode)){
                    myPreference.setMode(mode);
                }
                myPreference.setNickname(nickname);
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToast(message);
            }
        },token);
        getDevices();
    }

    private void getDevices(){
        String token = MyPreference.getInstance(this).getToken();
        MiCODevice miCODevice = new MiCODevice(this);
        miCODevice.getDeviceList(new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                MLog.i(TAG,message);
                String data = JsonHelper.getData(message);
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject temp = (JSONObject) jsonArray.get(i);
                        String name = temp.getString(Constants.PARAM_DEVNAME);
                        String pw = temp.getString(Constants.PARAM_PW);
                        String isSub = temp.getString(Constants.PARAM_SUB);
                        String mac = temp.getString(Constants.PARAM_mac);
                        String role = temp.getString(Constants.PARAM_ROLE);
                        String online = temp.getString(Constants.PARAM_ONLINE);
                        String proId = temp.getString(Constants.PARAM_DEV_ID);
                        deviceModelList.add(new DeviceModel(name,mac,pw,isSub,role,online,proId));
                    }
                    goToMainActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int code, String message) {
                MLog.i(TAG,message);
                goToMainActivity();
            }
        },token);

    }

    private void goToMainActivity(){
        Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
        if(deviceModelList.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.PARAM_VALUE, (Serializable) deviceModelList);
            bundle.putSerializable(Constants.NOTI_TYPE,alertDevice);
            intent.putExtras(bundle);
        }
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
