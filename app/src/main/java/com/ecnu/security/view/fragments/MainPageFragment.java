package com.ecnu.security.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.ecnu.security.R;

/**
 * Created by Phuylai on 2017/4/26.
 */

public class MainPageFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private View mView;

    public ImageView imageView;
    public SwitchCompat s1;
    public SwitchCompat s2;
    public SwitchCompat s3;
    public SwitchCompat s4;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backIndicator = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_main;
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(layoutId, null);
        }
        findViews(mView);
        return mView;
    }

    @Override
    public boolean onBackPressed() {
        activity.finish();
        return true;
    }

    @Override
    protected void findViews(View rootView) {
        imageView = (ImageView) rootView.findViewById(R.id.iv_action);
        s1 = (SwitchCompat) rootView.findViewById(R.id.sw_sys);
        s2 = (SwitchCompat) rootView.findViewById(R.id.sw_alert);
        s3 = (SwitchCompat) rootView.findViewById(R.id.sw_danger);
        s4 = (SwitchCompat) rootView.findViewById(R.id.sw_led);
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.title_home);
    }

    @Override
    public void setListener() {
        s1.setOnCheckedChangeListener(this);
        s2.setOnCheckedChangeListener(this);
        s3.setOnCheckedChangeListener(this);
        s4.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }
}
