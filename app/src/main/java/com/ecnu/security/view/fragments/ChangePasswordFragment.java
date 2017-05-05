package com.ecnu.security.view.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Model.ActionType;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.Util.ToastUtil;

import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCOUser;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    protected EditText inputNew;
    protected EditText inputConfirm;
    protected EditText inputOldPass;
    protected ProgressBar progressBar;
    protected String defValue;
    protected ActionType actionType;
    protected TextView tv_warning;
    protected Button btn_submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backIndicator = true;
        bottomView = false;
        getArgument();
    }

    protected void getArgument(){
        Bundle bundle = getArguments();
        if(bundle != null){

            defValue = bundle.getString(Constants.PARAM_VALUE);
            actionType = ActionType.getType(bundle.getString(Constants.PARAM_ACTION));
            if( defValue != null && getString(R.string.not_set).equals(defValue)){
                defValue = "";
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.profile_edit;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void findViews(View rootView) {
        tv_warning = (TextView) rootView.findViewById(R.id.tv_warning);
        btn_submit = (Button) rootView.findViewById(R.id.btn_verificationBtn);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        inputOldPass = (EditText) rootView.findViewById(R.id.et_old_password);
        inputNew = (EditText) rootView.findViewById(R.id.et_new_password);
        inputConfirm = (EditText) rootView.findViewById(R.id.et_new_password_again);
        inputNew.setHint(R.string.enter_new_psd);
        inputConfirm.setHint(R.string.confirm_new_psd);
        inputOldPass.setHint(R.string.input_old_psd);
        setActionButtonDisable(checkInputInfo());
    }

    protected void setActionButtonDisable(boolean isEnable) {
        btn_submit.setEnabled(isEnable);
        if (isEnable) {
            btn_submit.getBackground().setAlpha(Constants.DEF_OPAQUE);
        } else {
            btn_submit.getBackground().setAlpha(
                    Constants.DISABLE_OPAQUE);
        }
    }

    protected boolean checkInputInfo() {
        String newPsd = inputNew.getText().toString();
        String confirm = inputConfirm.getText().toString();
        String oldPsd = inputOldPass.getText().toString();
        if (StringUtil.isNull(oldPsd)) {
            return false;
        }
        if (StringUtil.isNull(newPsd)
                || !StringUtil.checkPassWord(newPsd)) {
            return false;
        }
        if (StringUtil.isNull(confirm)
                || !confirm.equals(newPsd)) {
            return false;
        }
        return !StringUtil.isNull(oldPsd);
    }

    protected void setEnable(boolean enable){
        inputNew.setEnabled(enable);
        inputOldPass.setEnabled(enable);
        inputConfirm.setEnabled(enable);
        btn_submit.setEnabled(enable);
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.change_password);
    }

    @Override
    public void setListener() {
        btn_submit.setOnClickListener(this);
        inputNew.addTextChangedListener(this);
        inputConfirm.addTextChangedListener(this);
        inputOldPass.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_verificationBtn:
                final String password = inputNew.getText().toString();
                final String oldInput = inputOldPass.getText().toString();
                final MyPreference myPreference = MyPreference.getInstance(activity);
                final String oldPass = myPreference.getPassword();
                if(!oldPass.equals(oldInput)){
                    ToastUtil.showToastShort(activity,R.string.old_incorrect);
                    return;
                }
                setEnable(false);
                progressBar.setVisibility(View.VISIBLE);
                String token = myPreference.getToken();
                MiCOUser miCOUser = new MiCOUser();

                miCOUser.resetPassword(password, new MiCOCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        ToastUtil.showToastShort(activity,R.string.changed);
                        myPreference.setPassword(password);
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        setEnable(true);
                        ToastUtil.showToastShort(activity,message);
                    }
                },token);
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setActionButtonDisable(showInputInfo());
        setActionButtonDisable(checkInputInfo());
    }

    protected boolean showInputInfo() {
        String newPassword = inputNew.getText().toString();
        if(StringUtil.notNull(newPassword) && !StringUtil.checkPassWord(newPassword)){
            ResourceUtil.setWarningText(tv_warning,
                    R.string.psd_exp);
            ResourceUtil.setWarningTopMarginByRelativeLayout(
                    activity, tv_warning);
            return false;
        }
        String confirmPassWord = inputConfirm.getText().toString();
        if (StringUtil.notNull(confirmPassWord)
                && !confirmPassWord.equals(newPassword)) {
            ResourceUtil.setWarningText(tv_warning,
                    R.string.psd_match);
            ResourceUtil.setWarningTopMarginByRelativeLayout(
                    activity, tv_warning);
            return false;
        }
        ResourceUtil.hiddenWarning(tv_warning);
        if (StringUtil.isNull(newPassword)) {
            return false;
        }
        return !StringUtil.isNull(confirmPassWord);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
