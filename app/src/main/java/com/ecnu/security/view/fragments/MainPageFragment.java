package com.ecnu.security.view.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecnu.security.Controller.ApiCallInterface;
import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.Model.AlertDevice;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.Model.SMSModel;
import com.ecnu.security.MyReceiver;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.view.activities.JsonHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fog.callbacks.ControlDeviceCallBack;
import io.fog.helper.CommandPara;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * Created by Phuylai on 2017/4/26.
 */

public class MainPageFragment extends BaseFragment implements View.OnClickListener {

    private View mView;

    private ImageView imageView;
    private SwitchCompat s1;
    //public SwitchCompat s2;
    private SwitchCompat s3;
    private SwitchCompat s4;
    private Button sos;
    private TextView tv_action;

    private AlertDevice alertDevice;
    private List<DeviceModel> deviceModelList = new ArrayList<>();

    private MyPreference myPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backIndicator = false;
        alertDevice = null;
        //parseArgument();
    }

    private void parseArgument(){
        Bundle bundle = getArguments();
        if(bundle == null) {
            alertDevice = null;
            return;
        }
        alertDevice = (AlertDevice) bundle.getSerializable(Constants.NOTI_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_main;
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(layoutId, null);
        }
        findViews(mView);
        return mView;
    }

    @Override
    public boolean onBackPressed() {
        activity.finish();
        return true;
    }

    @Override
    protected void findViews(View rootView) {
        parseArgument();
        myPreference = MyPreference.getInstance(activity);
        imageView = (ImageView) rootView.findViewById(R.id.iv_action);
        tv_action = (TextView) rootView.findViewById(R.id.tv_action);
        s1 = (SwitchCompat) rootView.findViewById(R.id.sw_sys);
        s3 = (SwitchCompat) rootView.findViewById(R.id.sw_danger);
        s4 = (SwitchCompat) rootView.findViewById(R.id.sw_led);
        sos = (Button) rootView.findViewById(R.id.bt_sos);
        if(alertDevice != null){
            processMessage(alertDevice);
        }
    }



    public void processMessage(AlertDevice alertDevice) {
        imageView.setImageResource(R.drawable.button_red);
        this.alertDevice = alertDevice;
        deviceModelList.clear();
        deviceModelList.addAll(activity.getDeviceModels());
        String module = alertDevice.getModule();
        for(DeviceModel deviceModel:deviceModelList){
            if(deviceModel.getDevId().equals(alertDevice.getDevice_id())){
                module += "\n"  + deviceModel.getName();
            }
        }
        tv_action.setText(module);
        s1.setChecked(true);
        s3.setChecked(true);
        s4.setChecked(true);
        activity.setAlertNull();
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.title_home);
    }

    @Override
    public void setListener() {
        s1.setOnClickListener(this);
        s3.setOnClickListener(this);
        s4.setOnClickListener(this);
        sos.setOnClickListener(this);
    }



    private void setChecked(SwitchCompat sw){
        if(sw.isChecked()){
            sw.setChecked(false);
        }else{
            sw.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sw_sys:
                if(s1.isChecked()){
                    s3.setChecked(true);
                    s4.setChecked(true);
                    imageView.setImageResource(R.drawable.button_red);
                    String commandJson = "{\"led\":1,\"sound\":1}";
                    sendCommand(commandJson);
                }else{
                    s3.setChecked(false);
                    s4.setChecked(false);
                    imageView.setImageResource(R.drawable.button_green);
                    tv_action.setText("");
                    String commandJson = "{\"led\":0,\"sound\":0}";
                    sendCommand(commandJson);
                }
                break;
            case R.id.sw_danger:
                checkSwitchCompat(s3);
                break;
            case R.id.sw_led:
                checkSwitchCompat(s4);
                break;
            case R.id.bt_sos:
                sendSMS();
                break;
        }
    }

    private void sendSMS(){
        SMSModel smsModel = new SMSModel();
        smsModel.setAccountSid(Constants._SID);
        smsModel.setSmsContent("test");
        smsModel.setTo("13127771810");
        smsModel.setTimestamp(StringUtil.timeStamp());
        smsModel.setSig(StringUtil.getMd5(Constants._SID+Constants.PARAM_TOKEN+smsModel.getTimestamp()));
        /*RequestBody body = RequestBody.create(MediaType.parse("Content-type:application/x-www-form-urlencoded;charset=UTF-8"),smsModel.toString());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants._BASEURL)
                .build();
        ApiCallInterface service = retrofit.create(ApiCallInterface.class);
        Call<ResponseBody> call = service.getSentSMS(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                MLog.i("sms",response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MLog.i("sms",t.getMessage());
            }
        });*/
        String result = StringUtil.post(Constants._BASEURL+Constants._SMSURL,smsModel.toString());
        MLog.i("sms",result);

    }

    private void checkSwitchCompat(SwitchCompat sw){
        if(sw.isChecked()){
            s1.setChecked(true);
            imageView.setImageResource(R.drawable.button_red);
            if(sw.getId() == R.id.sw_danger){
                String commandJson = "{\"led\":1}";
                sendCommand(commandJson);
            }else{
                String commandJson = "{\"sound\":1}";
                sendCommand(commandJson);
            }
        }else if(!s3.isChecked() && !s4.isChecked()){
            s1.setChecked(false);
            imageView.setImageResource(R.drawable.button_green);
            tv_action.setText("");
            if(sw.getId() == R.id.sw_danger){
                String commandJson = "{\"led\":0}";
                sendCommand(commandJson);
            }else{
                String commandJson = "{\"sound\":0}";
                sendCommand(commandJson);
            }
        }
    }

    private void sendCommand(String jsonString){
        CommandPara commandPara = new CommandPara();
        //TODO:
        //commandPara.deviceid = deviceid is sent by the device
        //commandPara.devicepw = deviceid is mapped to get name n pw
        commandPara.command = jsonString;
        miCODevice.sendCommand(commandPara, new ControlDeviceCallBack() {
            @Override
            public void onSuccess(String message) {
                MLog.i("send command",message);

            }
            @Override
            public void onFailure(int code, String message) {
                MLog.i("send command",message);
            }
        },myPreference.getToken());
    }
}
