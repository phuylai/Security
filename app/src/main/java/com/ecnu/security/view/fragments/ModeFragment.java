package com.ecnu.security.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ecnu.security.Helper.BaseViewHolder;
import com.ecnu.security.Helper.DetailItemMaker;
import com.ecnu.security.Helper.ModeHolder;
import com.ecnu.security.Model.ActionType;
import com.ecnu.security.Model.MicoUserExt;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.Util.ToastUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.fog.callbacks.ControlDeviceCallBack;
import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCODevice;

/**
 * Created by Phuylai on 2017/5/8.
 */

public class ModeFragment extends BaseFragment implements ModeHolder.ModeClickListener {

    private MyPreference myPreference;
    private Collection<BaseViewHolder> baseViewHolders = new ArrayList<>();
    private LinearLayout bodyView;
    private List<BaseViewHolder> detailItemHolders = new ArrayList<>();
    private ProgressBar progressBar;

    private String mode;
    private ModeHolder h1;
    private ModeHolder h2;
    private ModeHolder h3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backIndicator = false;
        bottomView = true;
        myPreference = MyPreference.getInstance(activity);
    }


    private void removeView(){
        if(bodyView != null){
            bodyView.removeAllViews();
        }
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
        mode = myPreference.getMode();
        setView();
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.title_dashboard);
    }

    @Override
    public void setListener() {

    }

    private void setView(){
        removeView();
        setDetail();
    }

    private void addToBody(List<BaseViewHolder> list){
        for(BaseViewHolder detailActionItemHolder:list){
            addViewHolder(detailActionItemHolder);
            View rootview = detailActionItemHolder.getRootView();
            ResourceUtil.addView(bodyView,rootview);
            ResourceUtil.setMarginTop(ResourceUtil.getDimenPx(activity,R.dimen.zero),rootview);
        }
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

        DetailItemMaker detailItemMaker = new DetailItemMaker(activity,null);
        h1 = (ModeHolder) detailItemMaker.addModeHolder(ActionType.WORK,this,compareMode(ActionType.WORK,mode));
        detailItemHolders.add(h1);
        h2 = (ModeHolder) detailItemMaker.addModeHolder(ActionType.PEACE,this,compareMode(ActionType.PEACE,mode));
        detailItemHolders.add(h2);
        h3 = (ModeHolder) detailItemMaker.addModeHolder(ActionType.AWAY,this,compareMode(ActionType.AWAY,mode));
        detailItemHolders.add(h3);
        addToBody(detailItemHolders);
    }

    private boolean compareMode(ActionType actionType,String mode){
        return actionType == ActionType.getType(mode);
    }

    @Override
    public void peace() {
        h1.cancelClick();
        h3.cancelClick();
        if(compareMode(ActionType.PEACE,getMode())){
            return;
        }else{
            progressBar.setVisibility(View.VISIBLE);
            String value = ActionType.PEACE.toString();
            updateUserInfo(value,false);
        }
    }


    @Override
    public void work() {
        h2.cancelClick();
        h3.cancelClick();
        if(compareMode(ActionType.WORK,getMode())){
            return;
        }else{
            progressBar.setVisibility(View.VISIBLE);
            updateUserInfo(ActionType.WORK.toString(),true);
        }
    }

    @Override
    public void away() {
        h1.cancelClick();
        h2.cancelClick();
        if(compareMode(ActionType.AWAY,getMode())){
            return;
        }else {
            progressBar.setVisibility(View.VISIBLE);
            String value = ActionType.AWAY.toString();
            updateUserInfo(value,false);
        }
    }

    private void updateUserInfo(final String value,final boolean noti){
        MicoUserExt micoUserExt = new MicoUserExt();
        micoUserExt.setMode(value, new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                progressBar.setVisibility(View.GONE);
                myPreference.setMode(value);
                myPreference.setNoti(noti);
                ToastUtil.showToastLong(activity,message);
            }

            @Override
            public void onFailure(int code, String message) {
                progressBar.setVisibility(View.GONE);
                ToastUtil.showToastLong(activity,message);
            }
        },myPreference.getToken());
    }

    private String getMode(){
        mode = myPreference.getMode();
        return mode;
    }
}
