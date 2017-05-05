package com.ecnu.security.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Model.AccountModel;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.Util.ToastUtil;
import com.ecnu.security.view.activities.RegisterActivity;

import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCOUser;

/**
 * Created by Phuylai on 2017/5/2.
 */

public class SetPsdFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = SetPsdFragment.class.getName();

    protected Button register;
    private EditText etNewPsd;
    private EditText etConfirmPsd;
    private TextView warningText;

    private MiCOUser miCOUser = new MiCOUser();

    private AccountModel model;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_set_psd;
        backIndicator = true;
        bottomView = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgument();
    }

    private void parseArgument(){
        Bundle bundle = getArguments();
        if(bundle == null)
            return;
        model = (AccountModel) bundle.getSerializable(Constants.PARAM_ACCOUNT);
    }

    @Override
    protected void findViews(View rootView) {
        register = (Button) rootView.findViewById(R.id.btn_reset_psd_reset);
        warningText = (TextView) rootView.findViewById(R.id.tv_warning_reset);
        etConfirmPsd = (EditText) rootView.findViewById(R.id.et_reset_psd_confirm);
        etNewPsd = (EditText) rootView.findViewById(R.id.et_reset_psd_new);
        setActionButtonDisable(showInputInfo());
    }

    protected void setActionButtonDisable(boolean isEnable) {

        register.setEnabled(isEnable);

        if (isEnable) {
            register.getBackground().setAlpha(Constants.DEF_OPAQUE);
        } else {
            register.getBackground().setAlpha(Constants.DISABLE_OPAQUE);
        }
    }

    protected boolean showInputInfo() {
        String passWord = etNewPsd.getText().toString();
        if (StringUtil.notNull(passWord)
                && !StringUtil.checkPassWord(passWord)) {
            ResourceUtil.setWarningText(warningText,
                    R.string.psd_exp);
            ResourceUtil.setWarningTopMarginByLinearLayout(
                    activity, warningText);
            return false;
        }
        String confirmPassWord = etConfirmPsd.getText().toString();
        if (StringUtil.notNull(confirmPassWord)
                && !confirmPassWord.equals(passWord)) {
            ResourceUtil.setWarningText(warningText,
                    R.string.psd_match);
            ResourceUtil.setWarningTopMarginByLinearLayout(
                    activity, warningText);
            return false;
        }
        ResourceUtil.hiddenWarning(warningText);
        return true;
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.register);
    }

    @Override
    public void setListener() {
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reset_psd_reset:
                final String psd = etConfirmPsd.getText().toString().trim();
                miCOUser.setPassword(psd, new MiCOCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        MyPreference myPreference = MyPreference.getInstance(activity);
                        myPreference.setPassword(psd);
                        if(activity != null){
                            activity.setResult(RegisterActivity.RESULT_REGISTER_SUCCESS);
                            activity.finish();
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        ToastUtil.showToastShort(activity,message);
                    }
                },model.getToken());
                break;
            default:
                break;
        }
    }
}
