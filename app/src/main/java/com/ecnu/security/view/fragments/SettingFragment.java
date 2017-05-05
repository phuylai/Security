package com.ecnu.security.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ecnu.security.R;

/**
 * Created by Phuylai on 2017/5/2.
 */

public class SettingFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backIndicator = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_setting;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initMenu(Context context, Menu menu) {
        clearMenu(menu);
        ((Activity) context).getMenuInflater().inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.ic_profile:
                activity.goToFragment(new ProfileFragment());
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void findViews(View rootView) {

    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.setting);
    }

    @Override
    public void setListener() {

    }
}
