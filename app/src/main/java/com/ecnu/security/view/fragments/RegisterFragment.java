package com.ecnu.security.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.R;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.Util.StringUtil;

/**
 * Created by Phuylai on 2017/4/27.
 */

public class RegisterFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private EditText et_username;
    private EditText et_password;
    private EditText et_confirm_password;
    protected TextView tvWarning;
    protected EditText et_mobile;
    private Button btn_next;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_register;
        backIndicator = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void findViews(View rootView) {
        tvWarning = (TextView) rootView.findViewById(R.id.tv_warning);
        et_username = (EditText) rootView.findViewById(R.id.et_username);
        et_password = (EditText) rootView.findViewById(R.id.et_password);
        et_confirm_password = (EditText) rootView.findViewById(R.id.et_confirm_password);
        et_mobile = (EditText) rootView.findViewById(R.id.et_mobile);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        StringUtil.setPassWordEditTextHintType(et_password);
        StringUtil.setPassWordEditTextHintType(et_confirm_password);
        setEditTextInputLength();
        setActionButtonDisable(checkInputInfo());
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.register);
    }

    protected void setEditTextInputLength() {
        ResourceUtil.setEditTextMaxLength(et_username,
                Constants.FULLNAME_LENGTH);
        ResourceUtil.setEditTextMaxLength(et_password,
                Constants.PASSWORD_MAXNUM);
        ResourceUtil.setEditTextMaxLength(et_confirm_password,
                Constants.PASSWORD_MAXNUM);
    }

    protected void setActionButtonDisable(boolean isEnable) {

        btn_next.setEnabled(isEnable);

        if (isEnable) {
            btn_next.getBackground().mutate().setAlpha(Constants.DEF_OPAQUE);
        } else {
            btn_next.getBackground().mutate().setAlpha(Constants.DISABLE_OPAQUE);
        }
    }

    protected boolean checkInputInfo() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        String phone = et_mobile.getText().toString();
        String comfirm_password = et_confirm_password.getText().toString();
        if (StringUtil.isNull(username)) {
            return false;
        }
        if (StringUtil.isNull(password)
                || !StringUtil.checkPassWord(password)) {
            return false;
        }
        if (StringUtil.isNull(comfirm_password)
                || !comfirm_password.equals(password)) {
            return false;
        }
        return !StringUtil.isNull(phone);
    }

    @Override
    public void setListener() {
        btn_next.setOnClickListener(this);
        et_username.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_confirm_password.addTextChangedListener(this);
        et_mobile.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                registerAccount();
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setActionButtonDisable(checkInputInfo());
        showInputInfo();
    }


    @Override
    public void afterTextChanged(Editable editable) {

    }

    protected boolean showInputInfo() {
        String passWord = et_password.getText().toString();
        if (StringUtil.notNull(passWord)
                && !StringUtil.checkPassWord(passWord)) {
            ResourceUtil.setWarningText(tvWarning,
                    R.string.psd_exp);
            ResourceUtil.setWarningTopMarginByLinearLayout(
                    activity, tvWarning);
            return false;
        }
        String confirmPassWord = et_confirm_password.getText().toString();
        if (StringUtil.notNull(confirmPassWord)
                && !confirmPassWord.equals(passWord)) {
            ResourceUtil.setWarningText(tvWarning,
                    R.string.psd_match);
            ResourceUtil.setWarningTopMarginByLinearLayout(
                    activity, tvWarning);
            return false;
        }
        ResourceUtil.hiddenWarning(tvWarning);
        return true;
    }

    private void registerAccount(){
        String userName = et_username.getText().toString();
        String mobile = et_mobile.getText().toString();
        String password = et_password.getText().toString();
        setEnabled(false);
        registerUser(userName,mobile,password);
    }

    private void registerUser(String username, String mobile, String password){

    }

    public void setEnabled(boolean enabled) {
        et_username.setEnabled(enabled);
        et_password.setEnabled(enabled);
        et_confirm_password.setEnabled(enabled);
        et_mobile.setEnabled(enabled);
        btn_next.setEnabled(enabled);
    }
}
