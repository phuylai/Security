package com.ecnu.security.view.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.R;
import com.ecnu.security.Util.DialogUtil;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.Util.ToastUtil;
import com.ecnu.security.view.Adapter.DeviceAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.fog.callbacks.ManageDeviceCallBack;
import io.fog.fog2sdk.MiCODevice;
import io.fogcloud.easylink.helper.EasyLinkCallBack;
import io.fogcloud.fog_mdns.helper.SearchDeviceCallBack;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class BindFragment extends BaseFragment implements View.OnClickListener,
        DeviceAdapter.DeviceClickListener, TextWatcher, DialogUtil.DialogListener {

    private String TAG = BindFragment.class.getSimpleName();

    private List<DeviceModel> deviceModelList = new ArrayList<>();
    private DeviceAdapter deviceAdapter;

    private EditText et_ssid;
    private EditText et_password;
    private Button btn;
    private RecyclerView recyclerView;

    private MiCODevice miCODevice;
    private DeviceModel foundDevice;
    private static final int ONE_SECOND = 1000;
    private static final int MINUTE_SECOND = 60;
    private int secondCount = MINUTE_SECOND;
    private boolean isGetting = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_bind;
        backIndicator = true;
        bottomView = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            BindFragment bindFragment = null;
            Button button = null;
            switch (msg.what){
                case Constants.SECOND_TICK:
                    bindFragment = (BindFragment) msg.obj;
                    button = bindFragment.btn;
                    button.setClickable(false);
                    button.setBackgroundResource(R.color.line_grey);
                    Activity activity = bindFragment.getActivity();
                    if(activity == null)
                        return;
                    String strFormat = getString(R.string.resend_second);
                    String strResult = String.format(strFormat,bindFragment.getSecondCount());
                    button.setText(strResult);
                    break;
                case Constants.TIMER_END:
                    bindFragment = (BindFragment) msg.obj;
                    button = bindFragment.getButton();
                    button.setClickable(true);
                    button.setBackgroundResource(R.color.colorPrimary);
                    button.getBackground().mutate().setAlpha(Constants.DEF_OPAQUE);
                    button.setText(R.string.search);
                    bindFragment.setGetting(false);
                    bindFragment.setSecondCount(MINUTE_SECOND);
                    bindFragment.stopEasylink();
                    bindFragment.stopSearchDevice();
                    break;
                case Constants.PARAM_FIND:
                    updateDeviceList(msg.obj.toString());
                    break;
            }
        }
    };

    public void stopSearchDevice(){
        miCODevice.stopSearchDevices(new SearchDeviceCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                ToastUtil.showToastShort(activity,message);
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(activity,message);
            }
        });
    }

    public void stopEasylink(){
        miCODevice.stopEasyLink(new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                ToastUtil.showToastShort(activity,message);
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(activity,message);
            }
        });
    }

    public void setGetting(boolean isGetting) {
        this.isGetting = isGetting;
    }

    private void setSecondCount(int secondCount) {
        this.secondCount = secondCount;
    }

    private Button getButton() {
        return btn;
    }

    private int getSecondCount() {
        return secondCount;
    }

    @Override
    protected void findViews(View rootView) {
        miCODevice = new MiCODevice(activity);
        et_password = (EditText) rootView.findViewById(R.id.et_psd);
        et_ssid = (EditText) rootView.findViewById(R.id.et_ssid);
        btn = (Button) rootView.findViewById(R.id.btn);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        et_ssid.setText(miCODevice.getSSID());
        deviceAdapter = new DeviceAdapter(activity,deviceModelList,this);
        recyclerView.setAdapter(deviceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listenwifichange();
        setButtonDisable(checkInputInfo());
    }

    private void setButtonDisable(boolean isEnable) {

        btn.setEnabled(isEnable);

        if (isEnable) {
            btn.getBackground().mutate().setAlpha(Constants.DEF_OPAQUE);
        } else {
            btn.getBackground().mutate().setAlpha(Constants.DISABLE_OPAQUE);
        }
    }

    protected boolean checkInputInfo(){
        String password = et_password.getText().toString();
        String name = et_password.getText().toString();
        return !StringUtil.isNull(password) && !StringUtil.isNull(name);
    }


    @Override
    public void changeTitle() {
        setTitleId(R.string.add_device);
    }

    @Override
    public void setListener() {
        btn.setOnClickListener(this);
        et_ssid.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                setGetting(true);
                countTime();
                String ssid = et_ssid.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                startEasyLink(ssid,password);
                searchDevice();
                break;
        }
    }

    private void startEasyLink(String ssid, String password){
        miCODevice.startEasyLink(ssid, password, true, 900, 20, "", "", new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                MLog.i(TAG,message);
                ToastUtil.showToastShort(activity,message);
            }

            @Override
            public void onFailure(int code, String message) {
                MLog.i(TAG,message);
                ToastUtil.showToastShort(activity,message);
            }
        });
    }

    private void searchDevice(){
        String serviceName = Constants.SERVICE_NAME;
        miCODevice.startSearchDevices(serviceName, new SearchDeviceCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                MLog.i(TAG,message);
                ToastUtil.showToastShort(activity,message);
            }

            @Override
            public void onFailure(int code, String message) {
                MLog.i(TAG,message);
                ToastUtil.showToastShort(activity,message);
            }

            @Override
            public void onDevicesFind(int code, JSONArray deviceStatus) {
                MLog.i(TAG,deviceStatus.toString());
                Message msg = new Message();
                msg.what = Constants.PARAM_FIND;
                msg.obj = deviceStatus.toString();
                handler.sendMessage(msg);
            }
        });
    }

    private void updateDeviceList(String message){
        recyclerView.removeAllViews();
        deviceModelList.clear();
        try {
            JSONArray jsonArray = new JSONArray(message);
            JSONObject temp;
            for(int i=0;i<jsonArray.length();i++){
                temp = (JSONObject) jsonArray.get(i);
                String name = temp.getString(Constants.PARAM_DEV_NAME);
                String IP = temp.getString(Constants.PARAM_IP);
                String port = temp.getString(Constants.PARAM_PORT);
                String MAC = temp.getString(Constants.PARAM_MAC);
                String productId = temp.getString(Constants.PARAM_PRODUCT_ID);
                String isHaveSuper = temp.getString(Constants.PARAM_SUPER_USER);
                String model = temp.getString(Constants.PARAM_MODEL);
                String protocol = temp.getString(Constants.PARAM_PROTOCOL);
                deviceModelList.add(new DeviceModel(name,IP,port,MAC,productId,
                        isHaveSuper,model,protocol));
                if(deviceAdapter != null)
                    deviceAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void countTime() {
        new Thread(new timerCounter()).start();
    }

    @Override
    public void deviceClick(DeviceModel deviceModel) {
        this.foundDevice = deviceModel;
        DialogUtil.showDialog(activity,R.string.reminder,R.string.reminder_bind,this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setButtonDisable(checkInputInfo());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void yes() {
        String token = MyPreference.getInstance(activity).getToken();
        miCODevice.bindDevice(foundDevice.getIP(), foundDevice.getPort(), new ManageDeviceCallBack() {
            @Override
            public void onSuccess(String message) {
                ToastUtil.showToastShort(activity,message);
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(activity,message);
            }
        },token);
    }

    @Override
    public void no() {

    }

    private class timerCounter implements Runnable {
        @Override
        public void run() {
            while (true) {
                Message message = new Message();
                message.obj = BindFragment.this;
                if (secondCount == 0) {
                    message.what = Constants.TIMER_END;
                    handler.sendMessage(message);
                    return;
                }
                secondCount--;
                message.what = Constants.SECOND_TICK;
                handler.sendMessage(message);
                try {
                    Thread.sleep(ONE_SECOND);
                } catch (InterruptedException e) {
                    MLog.e(TAG, e);
                }
            }
        }
    }

    private void listenwifichange() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        getContext().registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    Log.d(TAG, "---heiheihei---");
                    et_ssid.setText(miCODevice.getSSID());
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopEasylink();
        stopSearchDevice();
    }
}
