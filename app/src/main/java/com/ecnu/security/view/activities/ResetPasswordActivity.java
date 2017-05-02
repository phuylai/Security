package com.ecnu.security.view.activities;

import com.ecnu.security.MainActivity;
import com.ecnu.security.R;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.view.fragments.ForgotPasswordFragment;
import com.ecnu.security.view.fragments.RegisterFragment;
import com.ecnu.security.view.fragments.ResetPasswordFragment;

/**
 * Created by Phuylai on 2017/5/2.
 */

public class ResetPasswordActivity extends MainActivity {

    @Override
    protected void initFragment() {
        toolbar.setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary));
        setBottomNavigationView(false);
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        goToFragment(resetPasswordFragment,false);
    }
}
