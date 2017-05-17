package com.ecnu.security.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.ecnu.security.R;

/**
 * Created by Phuylai on 2017/5/2.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {

    private AppCompatSeekBar sb_alarm;
    private AppCompatSeekBar sb_led;
    private SwitchCompat sw;
    private AppCompatSpinner spinner;
    private RelativeLayout rl_trusted;


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
        sb_alarm = (AppCompatSeekBar) rootView.findViewById(R.id.s1);
        sb_led = (AppCompatSeekBar) rootView.findViewById(R.id.s2);
        sw = (SwitchCompat) rootView.findViewById(R.id.sw_noti);
        spinner = (AppCompatSpinner) rootView.findViewById(R.id.sp_redirect);
        rl_trusted = (RelativeLayout) rootView.findViewById(R.id.rl_trusted);
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.setting);
    }

    @Override
    public void setListener() {
        sw.setOnClickListener(this);
        sb_led.setOnSeekBarChangeListener(this);
        sb_alarm.setOnSeekBarChangeListener(this);
        spinner.setOnItemSelectedListener(this);
        rl_trusted.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sw_noti:
                //switch on off the noti
                /*if(sw.isChecked()){
                    activity.setNoti(true);
                }else{
                    activity.setNoti(false);
                }*/
                break;
            case R.id.rl_trusted:
                //go to add/edit trusted contact
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()){
            case R.id.s1:
                String commandJson = "{\"led\":1,\"sound\":1}";
                break;
            case R.id.s2:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
