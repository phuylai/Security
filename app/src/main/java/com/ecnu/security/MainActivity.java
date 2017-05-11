package com.ecnu.security;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
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
import com.ecnu.security.Model.ActionType;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.Model.MicoUserExt;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.Util.ToastUtil;
import com.ecnu.security.view.activities.BaseActivity;
import com.ecnu.security.view.activities.JsonHelper;
import com.ecnu.security.view.activities.LoginActivity;
import com.ecnu.security.view.fragments.BaseFragment;
import com.ecnu.security.view.fragments.MainPageFragment;
import com.ecnu.security.view.fragments.ModeFragment;
import com.ecnu.security.view.fragments.SettingFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import io.fog.callbacks.ControlDeviceCallBack;
import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCODevice;
import io.fog.helper.Configuration;
import io.fog.helper.ListenDevParFog;
import io.fogcloud.fog_mqtt.service.MqttService;

public class MainActivity extends BaseActivity {

    public static final String tag = MainActivity.class.getSimpleName();
    //TODO: model to load in

    public List<DeviceModel> deviceModels = new ArrayList<>();
    private MyPreference myPreference;

    public Toolbar toolbar = null;
    public BottomNavigationView bottomNavigationView;

    protected BaseFragment currentFragment = null;
    private MainPageFragment mainPageFragment;
    private SettingFragment settingFragment;
    private ModeFragment modeFragment;
    protected FragmentManager fragmentManager = null;
    private List<BaseFragment> topFragments = new ArrayList<>();
    protected FragmentTransaction transaction = null;

    private MiCODevice miCODevice;
    private MyReceiver myReceiver = new MyReceiver();

    public String message = null;

    private final android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.DEVICE_MSG:
                    //int ir = JsonHelper.getIR((String)msg.obj);
                    //if(ir != 0 && ir < 3200){
                        Intent intent = new Intent();
                        intent.setAction("com.ecnu.security");
                        intent.putExtra(Constants.PARAM_VALUE,"success");
                        if(SecurityApp.isActivityVisible()){
                            intent.putExtra(Constants.VISIBLE,"visible");
                        }
                        sendBroadcast(intent);
                    //}
                    if(mainPageFragment != null) {
                        message = (String) msg.obj;
                        mainPageFragment.processMessage(message);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    goToMainPageFragment();
                    return true;
                case R.id.navigation_mode:
                    if(currentFragment != null && currentFragment == modeFragment){
                        return true;
                    }else {
                        modeFragment = (ModeFragment) FragmentFactory.getFragment(Constants.
                                FRAG_MODE,null,null);
                        goToFragment(modeFragment);
                        return true;
                    }
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
        parseArgument();
    }

    private void parseArgument(){
        miCODevice = new MiCODevice(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.ecnu.security");
        registerReceiver(myReceiver,filter);
        myPreference = MyPreference.getInstance(this);
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            String login = bundle.getString(Constants.PARAM_LOGIN);
            if(!StringUtil.isNull(login)){
                getUserInfo();
            }else {
                deviceModels = (List<DeviceModel>) bundle.getSerializable(Constants.PARAM_VALUE);
                listenToDevices();
            }
        }
    }

    public MiCODevice getMiCODevice(){
        if(miCODevice == null)
            return null;
        return miCODevice;
    }

    private void getUserInfo() {
        String token = myPreference.getToken();
        final MicoUserExt micoUserExt = new MicoUserExt();
        micoUserExt.getUserInfo(new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                String nickname = JsonHelper.getNickName(message);
                String mode = JsonHelper.getRealname(message);
                if(!StringUtil.isNull(mode)){
                    myPreference.setMode(mode);
                }else{
                    myPreference.setMode(ActionType.WORK.toString());
                    micoUserExt.setMode(ActionType.WORK.toString(), new MiCOCallBack() {
                        @Override
                        public void onSuccess(String message) {
                            MLog.i("main",message);
                        }

                        @Override
                        public void onFailure(int code, String message) {
                            MLog.i("main",message);
                        }
                    },myPreference.getToken());
                }
                myPreference.setNickname(nickname);
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToast(message);
            }
        },token);
        getDevices();
    }

    private void getDevices(){
        String token = MyPreference.getInstance(this).getToken();
        miCODevice.getDeviceList(new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                MLog.i("MAIN",message);
                String data = JsonHelper.getData(message);
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject temp = (JSONObject) jsonArray.get(i);
                        String name = temp.getString(Constants.PARAM_DEVNAME);
                        String pw = temp.getString(Constants.PARAM_PW);
                        String isSub = temp.getString(Constants.PARAM_SUB);
                        String mac = temp.getString(Constants.PARAM_mac);
                        String role = temp.getString(Constants.PARAM_ROLE);
                        String online = temp.getString(Constants.PARAM_ONLINE);
                        String proId = temp.getString(Constants.PARAM_DEV_ID);
                        deviceModels.add(new DeviceModel(name,mac,pw,isSub,role,online,proId));
                    }
                    listenToDevices();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int code, String message) {
                MLog.i("MAIN",message);
            }
        },token);
    }

    public void listenToDevices(){
        List<ListenDevParFog> listenDevParFogs = new ArrayList<>();
        for(int i = 0;i < deviceModels.size(); i++){
            ListenDevParFog listenDevParFog = new ListenDevParFog();
            listenDevParFog.userName = myPreference.getClientID();
            listenDevParFog.deviceid = deviceModels.get(i).getDevId();
            listenDevParFog.host = Constants.LISTEN_HOST;
            listenDevParFog.port = Constants.CLOUD_PORT;
            listenDevParFog.passWord = myPreference.getPassword();
            listenDevParFog.clientID = listenDevParFog.userName;
            listenDevParFog.isencrypt = false;
            listenDevParFogs.add(listenDevParFog);
        }
        for(int i = 0;i<listenDevParFogs.size();i++){
            miCODevice.startListenDevice(listenDevParFogs.get(i), new ControlDeviceCallBack() {
                @Override
                public void onSuccess(String message) {
                    MLog.i("main",message);
                    ToastUtil.showToastLong(getApplicationContext(),message);
                }

                @Override
                public void onFailure(int code, String message) {
                    MLog.i("main",message);
                    ToastUtil.showToastLong(getApplicationContext(),message);
                }

                @Override
                public void onDeviceStatusReceived(int code, String messages) {
                    MLog.i("main",messages);
                    ToastUtil.showToastLong(getApplicationContext(),messages);
                    Message message = new Message();
                    message.what = Constants.DEVICE_MSG;
                    message.obj = messages;
                    handler.sendMessage(message);
                }
            });
        }
    }

    public void stopListen(){
        miCODevice.stopListenDevice(new ControlDeviceCallBack() {
            @Override
            public void onSuccess(String message) {
                ToastUtil.showToastShort(getApplicationContext(),R.string.peace);
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(getApplicationContext(),message);
            }
        });
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
        preference.setToken("");

        Intent intent1 = new Intent(this, MqttService.class);
        stopService(intent1);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();

        MLog.e("CMD_LOGOUT","SUCCESS");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MqttService.class);
        startService(intent);
        SecurityApp.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SecurityApp.activityPaused();
    }
}
