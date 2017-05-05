package com.ecnu.security.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.Model.AccountModel;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.Util.ToastUtil;
import com.ecnu.security.view.activities.JsonHelper;

import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCOUser;

/**
 * Created by Phuylai on 2017/4/27.
 */

public class RegisterFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private static final String TAG = RegisterFragment.class.getName();

    private Button btnCaptcha;
    private Button btnNext;
    private EditText etPhone;
    private EditText etCaptcha;

    private boolean isGettingCaptcha = false;
    private static final int ONE_SECOND = 1000;
    private static final int MINUTE_SECOND = 60;
    private int secondCount = MINUTE_SECOND;

    protected String getCaptchaPhone;

    private MiCOUser miCOUser = new MiCOUser();

    protected MyPreference myPreference;

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            RegisterFragment registerFragment = null;
            Button button = null;
            switch (msg.what){
                case Constants.SECOND_TICK:
                    registerFragment = (RegisterFragment) msg.obj;
                    button = registerFragment.btnCaptcha;
                    button.setClickable(false);
                    button.setBackgroundResource(R.color.line_grey);
                    Activity activity = registerFragment.getActivity();
                    if(activity == null)
                        return;
                    String strFormat = getString(R.string.resend_second);
                    String strResult = String.format(strFormat,registerFragment.getSecondCount());
                    button.setText(strResult);
                    break;
                case Constants.TIMER_END:
                    registerFragment = (RegisterFragment) msg.obj;
                    button = registerFragment.getButtonGetCaptcha();
                    button.setClickable(true);
                    button.setBackgroundResource(R.color.colorPrimary);
                    button.getBackground().mutate().setAlpha(Constants.DEF_OPAQUE);
                    button.setText(R.string.get_captcha);
                    registerFragment.setGettingCaptcha(false);
                    registerFragment.setSecondCount(MINUTE_SECOND);
                    break;
            }
        }
    };

    private void setSecondCount(int secondCount) {
        this.secondCount = secondCount;
    }

    public void setGettingCaptcha(boolean isGettingCaptcha) {
        this.isGettingCaptcha = isGettingCaptcha;
    }

    private Button getButtonGetCaptcha() {
        return btnCaptcha;
    }

    private int getSecondCount() {
        return secondCount;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPreference = MyPreference.getInstance(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_forget_password;
        backIndicator = true;
        bottomView = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void findViews(View rootView) {
        btnNext = (Button) rootView.findViewById(R.id.btn_reset_psd_reset);
        btnCaptcha = (Button) rootView.findViewById(R.id.btn_account_activate_get_captcha);
        etPhone = (EditText) rootView.findViewById(R.id.et_forget_psd_phone);
        etCaptcha = (EditText) rootView.findViewById(R.id.et_forget_psd_input_captcha);
        setActionButtonDisable(checkCaptcha(),btnCaptcha);
        setActionButtonDisable(checkForm(),btnNext);
    }

    protected boolean checkForm(){
        String capcha = etCaptcha.getText().toString();
        String phone = etPhone.getText().toString();
        return !(StringUtil.isNull(capcha) || StringUtil.isNull(phone));
    }

    protected boolean checkCaptcha(){
        String phoneNumber = etPhone.getText().toString();
        return !StringUtil.isNull(phoneNumber);
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.register);
    }

    protected void setActionButtonDisable(boolean isEnable,Button button) {

        button.setEnabled(isEnable);

        if (isEnable) {
            button.getBackground().mutate().setAlpha(Constants.DEF_OPAQUE);
        } else {
            button.getBackground().mutate().setAlpha(Constants.DISABLE_OPAQUE);
        }
    }

    @Override
    public void setListener() {
        btnNext.setOnClickListener(this);
        btnCaptcha.setOnClickListener(this);
        etPhone.addTextChangedListener(this);
        etCaptcha.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reset_psd_reset:
                setPassword();
                break;
            case R.id.btn_account_activate_get_captcha:
                String phone = etPhone.getText().toString();
                getCaptchaPhone = phone;
                getVerificationCode(phone);
                break;
            default:
                break;
        }
    }

    private void setPassword(){
        String captcha = etCaptcha.getText().toString();
        final String phone = etPhone.getText().toString();
        miCOUser.checkVerifyCode(phone, captcha, Constants._APPID(), new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                goOnNext(phone,message);
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(getActivity(),message);
            }
        });
    }

    protected void goOnNext(String phone,String message){
        String token = JsonHelper.getFogToken(message);
        String clientId = JsonHelper.getClientId(message);
        myPreference.setUsername(phone);
        myPreference.setClientID(clientId);
        myPreference.setToken(token);
        AccountModel model = new AccountModel(phone,token,clientId);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAM_ACCOUNT,model);
        goToSetPassword(bundle);
    }

    protected void goToSetPassword(Bundle bundle){
        SetPsdFragment setPsdFragment = new SetPsdFragment();
        setPsdFragment.setArguments(bundle);
        activity.goToFragment(setPsdFragment);
    }

    private void getVerificationCode(String phone){
        miCOUser.getVerifyCode(phone, Constants._APPID(), new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                ToastUtil.showToastShort(getActivity(),R.string.sms_sent);
                setGettingCaptcha(true);
                countTime();
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(getActivity(),message);
            }
        });
    }

    public void countTime() {
        new Thread(new timerCounter()).start();
    }

    private class timerCounter implements Runnable {
        @Override
        public void run() {
            while (true) {
                Message message = new Message();
                message.obj = RegisterFragment.this;
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setActionButtonDisable(checkCaptcha(),btnCaptcha);
        setActionButtonDisable(checkForm(),btnNext);
    }


    @Override
    public void afterTextChanged(Editable editable) {

    }

}
