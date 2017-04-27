package com.ecnu.security.view.activities;

import com.ecnu.security.MainActivity;
import com.ecnu.security.R;
import com.ecnu.security.Util.ResourceUtil;

/**
 * Created by Phuylai on 2017/4/27.
 */

public class RegisterActivity extends MainActivity {
    public static final int REQUEST_REGISTER_CODE = 1;
    public static final int RESULT_REGISTER_SUCCESS = 2;

    @Override
    protected void initFragment() {
        toolbar.setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary));
        //Register Fragment

    }
}
