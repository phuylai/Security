package com.ecnu.security;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ecnu.security.Controller.FragmentFactory;
import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.view.activities.BaseActivity;
import com.ecnu.security.view.activities.LoginActivity;
import com.ecnu.security.view.fragments.BaseFragment;
import com.ecnu.security.view.fragments.MainPageFragment;
import com.ecnu.security.view.fragments.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    public static final String tag = MainActivity.class.getSimpleName();
    //TODO: model to load in

    public Toolbar toolbar = null;
    public BottomNavigationView bottomNavigationView;

    protected BaseFragment currentFragment = null;
    private MainPageFragment mainPageFragment;
    private SettingFragment settingFragment;
    protected FragmentManager fragmentManager = null;
    private List<BaseFragment> topFragments = new ArrayList<>();
    protected FragmentTransaction transaction = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    goToMainPageFragment();
                    return true;
                case R.id.navigation_mode:
                    //do s.th
                    return true;
                case R.id.navigation_setting:
                    if(currentFragment != null && currentFragment == settingFragment){
                        return true;
                    }else {
                        settingFragment = (SettingFragment) FragmentFactory.getFragment(Constants.
                                FRAG_SETTING,null,null);
                        goToFragment(settingFragment);
                        return true;
                    }
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutId = R.layout.activity_main;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findViews() {
        super.findViews();
        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        initFragment();
    }

    protected void initFragment(){
        goToMainPageFragment();
    }

    @Override
    protected void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void setListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



    public void goToMainPageFragment(){
        if(currentFragment != null && currentFragment == mainPageFragment){
            return;
        }
        if(mainPageFragment != null){
            fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return;
        }
        mainPageFragment = new MainPageFragment();
        skipToFragmentByContentId(mainPageFragment,R.id.content,false, Constants.POP_TO_HOME,0,0);
    }


    @Override
    public void onBackPressed() {
        if(!actionBarClick()){
            super.onBackPressed();
        }
        overridePendingTransition(R.animator.back_in,R.animator.back_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                actionBarClick();
                break;
            default:
                break;
        }
        if (currentFragment != null) {
            if (currentFragment.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public synchronized boolean actionBarClick() {
        if (currentFragment != null && currentFragment.onBackPressed()) {
            return true;
        }
        if (topFragments.size() > 0) {
            backToTopFragment();
            backToPrevious();
            return true;
        }
        if(currentFragment != mainPageFragment){
            backToPrevious();
            return true;
        }
        return false;
    }

    protected void backToTopFragment() {
        if (topFragments == null) {
            return;
        }
        if (topFragments.size() == 0) {
            return;
        }
        BaseFragment lasttopFragment = topFragments
                .get(topFragments.size() - 1);
        if (lasttopFragment == null) {
            return;
        }
        lasttopFragment.changeTitle();
        onFragmentResume(lasttopFragment);
        lasttopFragment.setBackIndicator();
        topFragments.remove(lasttopFragment);
    }

    private void backToPrevious() {
        super.onBackPressed();
        overridePendingTransition(R.animator.back_in,
                R.animator.back_out);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(currentFragment != null){
            currentFragment.initMenu(this,menu);
        }
        //setSearchViewStatus(menu,null);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Confirm which one fragment is currently located
     *
     * @param baseFragment
     */
    public synchronized void onFragmentResume(BaseFragment baseFragment) {
        currentFragment = baseFragment;
        invalidateOptionsMenu();// creates call to onPrepareOptionsMenu()
    }

    public void goToFragment(Fragment fragment, boolean isaddback) {
        skipToFragmentByContentId(fragment, R.id.content, isaddback, null);
    }

    public void goToFragment(Fragment fragment) {
        skipToFragmentByContentId(fragment, R.id.content, true, null);
    }

    public void goToFragment(Fragment fragment, boolean isaddback,
                             String backName) {
        skipToFragmentByContentId(fragment, R.id.content, isaddback,
                backName);
    }

    public void goToFragment(Fragment fragment, String backName) {
        skipToFragmentByContentId(fragment, R.id.content, true, backName);
    }

    public void goToSelectFragment(BaseFragment topFragment, Fragment fragment) {
        addSelectedFragment(topFragment);
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.back_in,
                R.animator.back_out, R.animator.back_in,
                R.animator.back_out);
        transaction.add(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void skipToFragmentByContentId(Fragment fragment, int contentId,
                                          boolean addToStack, String backName) {
        skipToFragmentByContentId(fragment, contentId, addToStack, backName,
                R.animator.back_in, R.animator.back_out);
    }

    public void skipToFragmentByContentId(Fragment fragment, int contentId,
                                          boolean addToStack, String backName, int moveInAnimateId,
                                          int moveOutAnimateId) {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(moveInAnimateId,moveOutAnimateId,R.animator.back_in,R.animator.back_out);
        transaction.replace(contentId, fragment);
        if (addToStack) {
            transaction.addToBackStack(backName);
        }
        transaction.commitAllowingStateLoss();
    }

    public void goBackByName(String backName) {
        fragmentManager.popBackStack(backName,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void addSelectedFragment(BaseFragment topFragment) {
        if (topFragment == null) {
            return;
        }
        topFragments.add(topFragment);
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView()
                    .getWindowToken(), 0);
        }
    }

    public void changeTitle(String title){
        if(StringUtil.isNull(title)){
            return;
        }
        if(toolbar != null){
            toolbar.setTitle(title);
        }
    }

    public void setBackIndicator(boolean enable){
        actionBar.setDisplayUseLogoEnabled(enable);
        actionBar.setDisplayHomeAsUpEnabled(enable);
        actionBar.setHomeButtonEnabled(enable);
        actionBar.setDisplayShowHomeEnabled(enable);
    }

    public void setBottomNavigationView(boolean enable){
        if(enable){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }else{
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    public void logOut() {

        // CLEAN PASSWORD FROM SHARED PREFERENCES
        MyPreference preference = MyPreference.getInstance(this);
        preference.setPassword("");

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();

        MLog.e("CMD_LOGOUT","SUCCESS");
    }

}
