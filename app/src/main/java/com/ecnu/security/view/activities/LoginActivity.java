package com.ecnu.security.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.MainActivity;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.Util.ToastUtil;
import com.ecnu.security.view.fragments.ResetPasswordFragment;

import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCOUser;

/**
 * Created by Phuylai on 2017/5/2.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    public static final String TAG = LoginActivity.class.getSimpleName();

    public static final double ICON_MARGIN_TOP_OFF_SET = 0.099;

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private ProgressBar pb_login_in_progress;
    private String username = "";
    private String password = "";
    private TextView tv_signup;
    private TextView tv_forgetPassword;
    private MyPreference preference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        layoutId = R.layout.activity_login;
        preference = MyPreference.getInstance(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findViews() {
        super.findViews();
        tv_signup = (TextView) findViewById(R.id.tv_signup);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        pb_login_in_progress = (ProgressBar) findViewById(R.id.pb_login_in_progress);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        username = preference.getUsername();
        password = preference.getPassword();
        et_username.setText(username);
        et_password.setText(password);
        setButtonDisable(checkInputInfo());
    }

    private void setButtonDisable(boolean enable) {
        if (enable) {
            btn_login.setEnabled(true);
            btn_login.getBackground().setAlpha(Constants.DEF_OPAQUE);
        } else {
            btn_login.setEnabled(false);
            btn_login.getBackground().setAlpha(Constants.DISABLE_OPAQUE);
        }
    }

    private boolean checkInputInfo() {
        username = et_username.getText().toString();
        password = et_password.getText().toString();

        if (StringUtil.isNull(username)) {
            return false;
        }

        return !StringUtil.isNull(password);
    }

    @Override
    protected void initActionBar() {

    }

    @Override
    protected void setListeners() {
        tv_signup.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        et_username.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        tv_forgetPassword.setOnClickListener(this);
    }

    private boolean enableLogIn() {
        if (pb_login_in_progress.getVisibility() == View.GONE) {
            return false;
        }

        pb_login_in_progress.setVisibility(View.GONE);
        et_username.setEnabled(true);
        et_password.setEnabled(true);
        setButtonDisable(checkInputInfo());

        return true;
    }

    protected void startLoading() {
        username = et_username.getText().toString();
        password = et_password.getText().toString();

        if (StringUtil.isNull(username) || StringUtil.isNull(password)) {
            ToastUtil.showToast(R.string.check_username_password);
        } else if (!StringUtil.matchREGEX(Constants.PASSWORD_RULES_REGEX,
                password)) {
            ToastUtil.showToast(R.string.incorrect_phone_password);
        } else {
            disableLogIn();
            MiCOUser miCOUser = new MiCOUser();
            miCOUser.login(username, password, Constants._APPID(), new MiCOCallBack() {
                @Override
                public void onSuccess(String message) {
                    String token = JsonHelper.getFogToken(message);
                    String clientId = JsonHelper.getClientId(message);
                    if(!StringUtil.isNull(token) && !StringUtil.isNull(clientId)) {
                        preference.setClientID(clientId);
                        preference.setToken(token);
                        MLog.i(TAG, token);
                        goToMainActivity();
                    }else{
                        showToast(R.string.fail_login);
                    }
                }

                @Override
                public void onFailure(int code, String message) {
                    showToast(R.string.fail_login);
                }
            });
        }
    }

    private void goToMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        MyPreference.getInstance(getApplicationContext()).setPassword(password);
        //user model to pass on if there is any
        startActivity(intent);
        finish();
    }

    private void disableLogIn() {
        pb_login_in_progress.setVisibility(View.VISIBLE);
        et_username.setEnabled(false);
        et_password.setEnabled(false);
        btn_login.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_login:
                startLoading();
                break;
            case R.id.tv_signup:
                intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent,
                        RegisterActivity.REQUEST_REGISTER_CODE);
                overridePendingTransition(R.animator.back_in,
                        R.animator.back_out);
                break;
            case R.id.tv_forget_password:
                intent = new Intent(this, ResetPasswordActivity.class);
                startActivityForResult(intent,
                        RegisterActivity.REQUEST_REGISTER_CODE);
                overridePendingTransition(R.animator.back_in,
                        R.animator.back_out);
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
        setButtonDisable(checkInputInfo());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterActivity.REQUEST_REGISTER_CODE
                && resultCode == RegisterActivity.RESULT_REGISTER_SUCCESS) {
            String userName = preference.getUsername();
            String passWord = preference.getPassword();
            et_username.setText(userName);
            et_password.setText(passWord);
        }
    }
}
