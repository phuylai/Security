package com.ecnu.security.view.fragments;

import android.os.Bundle;

import com.ecnu.security.R;

/**
 * Created by Phuylai on 2017/5/2.
 */

public class ResetPasswordFragment extends RegisterFragment {

    @Override
    public void changeTitle() {
        setTitleId(R.string.reset_psd);
    }

    @Override
    protected void goToSetPassword(Bundle bundle) {
        ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
        forgotPasswordFragment.setArguments(bundle);
        activity.goToFragment(forgotPasswordFragment);
    }
}
