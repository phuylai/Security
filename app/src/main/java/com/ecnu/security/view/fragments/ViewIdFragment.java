package com.ecnu.security.view.fragments;

import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.ecnu.security.R;

/**
 * Created by Phuylai on 2017/5/5.
 */

public class ViewIdFragment extends ChangeNameFragment {

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        btn_submit.setVisibility(View.GONE);
        inputOldPass.setVisibility(View.GONE);
        TextView textView = (TextView) rootView.findViewById(R.id.tv_id);
        textView.setVisibility(View.VISIBLE);
        textView.setText(defValue);
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.device_id);
    }
}
