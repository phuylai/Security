package com.ecnu.security.view.fragments;

import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecnu.security.Controller.FragmentFactory;
import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.Helper.RunnableManager;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.R;
import com.ecnu.security.Util.DialogUtil;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.view.Adapter.DeviceAdapter;
import com.ecnu.security.view.activities.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCODevice;
import io.fog.fog2sdk.MiCOUser;

/**
 * Created by Phuylai on 2017/5/4.
 */

public class DeviceListFragment extends BaseFragment implements DeviceAdapter.DeviceClickListener,
        Runnable, DialogUtil.DialogListener {

    private String TAG = DeviceListFragment.class.getSimpleName();

    private List<DeviceModel> deviceModelList = new ArrayList<>();
    private DeviceAdapter deviceAdapter;

    private DeviceModel deviceModel;
    private MiCODevice miCODevice;

    // refresh
    protected Handler mUiHandler = new Handler();
    protected long DELAYMILLIS = 200;
    protected List<Object> models = new ArrayList<>();

    protected RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backIndicator = true;
        bottomView = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_recyler;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void findViews(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        deviceAdapter = new DeviceAdapter(activity,deviceModelList,this);
        recyclerView.setAdapter(deviceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        miCODevice = new MiCODevice(activity);
        getDeviceList();
    }

    private void getDeviceList(){
        String token = MyPreference.getInstance(activity).getToken();
        deviceModelList.clear();
        miCODevice.getDeviceList(new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                MLog.i(TAG,message);
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
                        deviceModelList.add(new DeviceModel(name,mac,pw,isSub,role,online,proId));
                        refreshList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int code, String message) {
                MLog.i(TAG,message);
            }
        },token);
    }

    protected void refreshList(){
        if(mUiHandler == null)
            return;
        RunnableManager.getInstance().postDelayed(this,DELAYMILLIS);
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.device_list);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void deviceClick(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
        DialogUtil.showDeviceDetailDialog(activity,deviceModel,this);
    }

    @Override
    public void run() {
        if(deviceModelList != null){
            updateData();
            notifyData();
        }
    }

    protected void updateData(){
        models.clear();
        models.addAll(deviceModelList);
    }

    protected void notifyData(){
        if (deviceAdapter != null)
            deviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void yes() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAM_VALUE,deviceModel);
        activity.goToFragment(FragmentFactory.getFragment(Constants.FRAG_DETAIL,bundle,null));
    }

    @Override
    public void no() {

    }


}
