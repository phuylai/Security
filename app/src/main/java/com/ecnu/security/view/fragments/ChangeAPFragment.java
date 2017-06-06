package com.ecnu.security.view.fragments;

import android.view.View;

import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.R;
import com.ecnu.security.Util.DialogUtil;

/**
 * Created by Phuylai on 2017/6/6.
 */

public class ChangeAPFragment extends BindFragment {

    @Override
    public void changeTitle() {
        setTitleId(R.string.change_ap);
    }

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        btn.setText(R.string.change);
    }

    @Override
    public void deviceClick(DeviceModel deviceModel) {

    }

    @Override
    protected void changeAccessPoint() {
        DialogUtil.doneDialog(activity);
    }
}
