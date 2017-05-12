package com.ecnu.security.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.ecnu.security.Controller.FragmentFactory;
import com.ecnu.security.Helper.BaseViewHolder;
import com.ecnu.security.Helper.ButtonHolder;
import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.DetailItemHolder;
import com.ecnu.security.Helper.DetailItemMaker;
import com.ecnu.security.Helper.UserHeaderHolder;
import com.ecnu.security.Model.ActionType;
import com.ecnu.security.R;
import com.ecnu.security.Util.DialogUtil;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.ResourceUtil;
import com.ecnu.security.view.activities.LoginActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.ecnu.security.Util.ResourceUtil.setMarginTop;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class ProfileFragment extends BaseFragment implements DetailItemHolder.ItemClickListener, ButtonHolder.LogoutListener, DialogUtil.DialogDataListener {

    private ProgressBar progressBar;
    private Collection<BaseViewHolder> baseViewHolders = new ArrayList<>();
    private LinearLayout bodyView;
    private View mView;
    private List<BaseViewHolder> detailItemHolders = new ArrayList<>();
    private MyPreference myPreference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backIndicator = true;
        bottomView = false;
        myPreference = MyPreference.getInstance(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_profile;
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
    public void onDestroy() {
        super.onDestroy();
        progressBar = null;
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

    @Override
    protected void findViews(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        bodyView = (LinearLayout) rootView.findViewById(R.id.ll_details_view);
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

    protected void addView(View view) {
        if (view == null) {
            return;
        }
        if (bodyView == null) {
            return;
        }
        if(view.getParent() != null){
            bodyView.removeAllViews();
        }
        bodyView.addView(view);
    }

    private void setDetail(){
        if(detailItemHolders.size() > 0)
            detailItemHolders.clear();

        DetailItemMaker detailItemMaker = new DetailItemMaker(activity,this);
        detailItemHolders.add(detailItemMaker.addHeader(myPreference.getUsername(),myPreference.getNickName()));
        detailItemHolders.add(detailItemMaker.getDetailItemHolder());
        detailItemHolders.add(detailItemMaker.phoneHolder());
        detailItemHolders.add(detailItemMaker.nameHolder());
        detailItemHolders.add(detailItemMaker.deviceHolder());
        detailItemHolders.add(detailItemMaker.addDevHolder());
        detailItemHolders.add(detailItemMaker.addRedirectHolder());
        detailItemHolders.add(detailItemMaker.addTrustedHolder());
        detailItemHolders.add(detailItemMaker.addNotiHolder());
        detailItemHolders.add(detailItemMaker.addButton(this));
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
        setTitleId(R.string.profile);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void psdClick() {
        activity.goToFragment(new ChangePasswordFragment());
    }

    @Override
    public void nameClick() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_ACTION, ActionType.NAME.toString());
        bundle.putString(Constants.PARAM_VALUE,myPreference.getNickName());
        activity.goToFragment(FragmentFactory.getFragment(Constants.PARAM_NICKNAME,bundle,null));
    }

    @Override
    public void phoneClick() {
        activity.goToFragment(new ChangePhoneNumberFragment());
    }

    @Override
    public void deviceListClick() {
        activity.goToFragment(new DeviceListFragment());
    }

    @Override
    public void addDeviceClick() {
        activity.goToFragment(new BindFragment());
    }

    @Override
    public void logout() {
        activity.logOut();
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.hideKeyBoard();
        setView();
    }

    @Override
    public void notiClick() {
        DialogUtil.showSwitchBarDialog(activity,ActionType.NOTI,myPreference.getNoti(),this);
    }

    @Override
    public void redirectClick() {
        DialogUtil.showSpinnerBarDialog(activity,ActionType.REDIRECT,myPreference.getRedirect(),this);
    }

    @Override
    public void trustedContactClick() {

    }

    @Override
    public void yes(String number, ActionType actionType) {

    }

    @Override
    public void no() {

    }
}
