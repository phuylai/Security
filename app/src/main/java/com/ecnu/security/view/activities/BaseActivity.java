package com.ecnu.security.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ecnu.security.Helper.ActivitiesManager;
import com.ecnu.security.Util.ToastUtil;
import com.ecnu.security.view.fragments.BaseFragment;

/**
 * Created by Phuylai on 2017/4/26.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected int layoutId = -1;
    protected Menu menu;
    protected ActionBar actionBar;

    protected void findViews(){
        initActionBar();
    }

    protected abstract void initActionBar();
    protected abstract void setListeners();
    private void initFragment(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
        if(layoutId > 0){
            setContentView(layoutId);
            findViews();
            setListeners();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public Menu getMenu(){
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivitiesManager.getInstance().addActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivitiesManager.getInstance().removeActivity(this);
    }

    protected void showToast(int resID) {
        ToastUtil.showToast(resID);
    }

    protected void setTitle(String title){
        getSupportActionBar().setTitle(title);
    }



}
