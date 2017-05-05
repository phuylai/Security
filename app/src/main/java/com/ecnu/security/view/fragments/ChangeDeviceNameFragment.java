package com.ecnu.security.view.fragments;

import android.os.Bundle;
import android.view.View;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ToastUtil;

import io.fog.callbacks.ManageDeviceCallBack;
import io.fog.fog2sdk.MiCODevice;

/**
 * Created by Phuylai on 2017/5/5.
 */

public class ChangeDeviceNameFragment extends ChangeNameFragment {

    private  DeviceModel deviceModel;

    @Override
    protected void getArgument() {
        Bundle bundle = getArguments();
        if(bundle != null){
            deviceModel = (DeviceModel) bundle.getSerializable(Constants.PARAM_VALUE);
            defValue = deviceModel.getName();
            if( defValue != null && getString(R.string.not_set).equals(defValue)){
                defValue = "";
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verificationBtn:
                final String name = inputOldPass.getText().toString();
                final MyPreference myPreference = MyPreference.getInstance(activity);
                final String token = myPreference.getToken();
                progressBar.setVisibility(View.VISIBLE);
                MiCODevice miCODevice = new MiCODevice(activity);
                miCODevice.updateDeviceAlias(deviceModel.getDevId(), name, new ManageDeviceCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        progressBar.setVisibility(View.GONE);
                        ToastUtil.showToastShort(activity,R.string.changed);
                        callItemSelected(name);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        progressBar.setVisibility(View.GONE);
                        ToastUtil.showToastShort(activity,message);
                    }
                },token);

        }
    }
}
