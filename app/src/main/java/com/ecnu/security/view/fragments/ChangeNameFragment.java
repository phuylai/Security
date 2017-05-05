package com.ecnu.security.view.fragments;

import android.text.InputType;
import android.view.View;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Model.MicoUserExt;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.Util.ToastUtil;

import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCODevice;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class ChangeNameFragment extends ChangePasswordFragment {

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        ResourceUtil.setVisibility(inputNew,View.GONE);
        ResourceUtil.setVisibility(inputConfirm,View.GONE);
        inputOldPass.setInputType(InputType.TYPE_CLASS_TEXT);
        inputOldPass.setHint(R.string.name);
        inputOldPass.setText(defValue);
        ResourceUtil.setEditTextMaxLength(inputOldPass, Constants.FULLNAME_LENGTH);
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.change_name);
    }

    @Override
    protected boolean checkInputInfo() {
        String first = inputOldPass.getText().toString();

        if (StringUtil.isNull(first)) {
            return false;
        }
        return !first.equals(defValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verificationBtn:
                final String name = inputOldPass.getText().toString();
                final MyPreference myPreference = MyPreference.getInstance(activity);
                final String token = myPreference.getToken();
                progressBar.setVisibility(View.VISIBLE);
                MicoUserExt micoUserExt = new MicoUserExt();
                micoUserExt.setNickname(name, new MiCOCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        progressBar.setVisibility(View.GONE);
                        ToastUtil.showToastShort(activity,R.string.changed);
                        myPreference.setNickname(name);
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        progressBar.setVisibility(View.GONE);
                        ToastUtil.showToastShort(activity,message);
                    }
                },token);
                break;
        }
    }
}
