package com.ecnu.security.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ecnu.security.R;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.Util.ToastUtil;
import com.ecnu.security.view.activities.JsonHelper;

import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCOUser;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class ChangePhoneNumberFragment extends ResetPasswordFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary));
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.change_phonne);
    }

    @Override
    protected void goOnNext(final String phone, String message) {
        MiCOUser miCOUser = new MiCOUser();
        final String token = JsonHelper.getFogToken(message);
        final String clientId = JsonHelper.getClientId(message);
        String password = myPreference.getPassword();
        miCOUser.setPassword(password, new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                myPreference.setUsername(phone);
                myPreference.setClientID(clientId);
                myPreference.setToken(token);
                ToastUtil.showToastShort(activity,R.string.changed);
                getFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(activity,message);
            }
        },token);
    }
}
