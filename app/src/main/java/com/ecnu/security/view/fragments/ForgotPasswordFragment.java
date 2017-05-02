package com.ecnu.security.view.fragments;

import android.view.View;

import com.ecnu.security.R;

/**
 * Created by Phuylai on 2017/5/2.
 */

public class ForgotPasswordFragment extends SetPsdFragment {

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        register.setText(R.string.reset);
    }
}
