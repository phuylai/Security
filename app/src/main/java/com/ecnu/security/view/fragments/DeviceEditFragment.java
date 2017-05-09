package com.ecnu.security.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ecnu.security.Controller.FragmentFactory;
import com.ecnu.security.Helper.BaseViewHolder;
import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.DetailItemHolder;
import com.ecnu.security.Helper.DetailItemMaker;
import com.ecnu.security.Model.ActionType;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.R;
import com.ecnu.security.Util.DialogUtil;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.Util.ToastUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.fog.callbacks.ManageDeviceCallBack;
import io.fog.fog2sdk.MiCODevice;

/**
 * Created by Phuylai on 2017/5/5.
 */

public class DeviceEditFragment extends BaseFragment implements DetailItemHolder.ItemClickListener, DialogUtil.DialogListener, BaseFragment.SelectedItemCallBackListener {

    private Collection<BaseViewHolder> baseViewHolders = new ArrayList<>();
    private LinearLayout bodyView;
    private List<BaseViewHolder> detailItemHolders = new ArrayList<>();
    private MyPreference myPreference;
    private DeviceModel deviceModel;

    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backIndicator = true;
        bottomView = false;
        myPreference = MyPreference.getInstance(activity);
        parseArgument();
    }

    private void parseArgument(){
        Bundle bundle = getArguments();
        if(bundle == null)
            return;
        deviceModel = (DeviceModel) bundle.getSerializable(Constants.PARAM_VALUE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(baseViewHolders.size() > 0){
            baseViewHolders.clear();
            baseViewHolders = null;
        }
        bodyView = null;
    }

    private void removeView(){
        if(bodyView != null){
            bodyView.removeAllViews();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_profile;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void findViews(View rootView) {
        bodyView = (LinearLayout) rootView.findViewById(R.id.ll_details_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        setView();
    }

    private void setView(){
        removeView();
        setDetail();
    }

    protected void addViewHolder(BaseViewHolder baseViewHolder) {
        if (baseViewHolders == null) {
            return;
        }
        baseViewHolders.add(baseViewHolder);
    }

    private void setDetail(){
        if(detailItemHolders.size() > 0)
            detailItemHolders.clear();

        DetailItemMaker detailItemMaker = new DetailItemMaker(activity,this);
        detailItemHolders.add(detailItemMaker.addDeviceHeader(deviceModel));
        ResourceUtil.addGreyLine(activity,bodyView,ResourceUtil.
                getDimenPx(activity,R.dimen.ten_dp));
        detailItemHolders.add(detailItemMaker.addNameHolder());
        detailItemHolders.add(detailItemMaker.addIdHolder());
        detailItemHolders.add(detailItemMaker.addUnbindHolder());
        addToBody(detailItemHolders);
    }

    private void addToBody(List<BaseViewHolder> list){
        for(BaseViewHolder detailActionItemHolder:list){
            addViewHolder(detailActionItemHolder);
            View rootview = detailActionItemHolder.getRootView();
            ResourceUtil.addView(bodyView,rootview);
            ResourceUtil.setMarginTop(ResourceUtil.getDimenPx(activity,R.dimen.zero),rootview);
        }
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.device_edit);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void psdClick() {

    }

    @Override
    public void nameClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAM_VALUE,deviceModel);
        activity.goToFragment(FragmentFactory.getFragment(Constants.PARAM_DEV_NAME,bundle,this));
    }

    @Override
    public void phoneClick() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_VALUE,deviceModel.getDevId());
        activity.goToFragment(FragmentFactory.getFragment(Constants.PARAM_DEV_ID,bundle,null));
    }

    @Override
    public void deviceListClick() {

    }

    @Override
    public void addDeviceClick() {
        DialogUtil.showDialog(activity,R.string.reminder,R.string.confirm_unbind,this);
    }

    @Override
    public void yes() {
        MiCODevice miCODevice = new MiCODevice(activity);
        progressBar.setVisibility(View.VISIBLE);
        miCODevice.unBindDevice(deviceModel.getDevId(), new ManageDeviceCallBack() {
            @Override
            public void onSuccess(String message) {
                progressBar.setVisibility(View.VISIBLE);
                ToastUtil.showToastShort(activity,R.string.changed);
                getFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(int code, String message) {
                progressBar.setVisibility(View.VISIBLE);
                ToastUtil.showToastShort(activity,message);
            }
        },myPreference.getToken());
    }

    @Override
    public void no() {

    }

    @Override
    public void onItemSelected(Object selectedItem) {
        String newName = (String) selectedItem;
        deviceModel.setName(newName);
        setView();
    }
}
