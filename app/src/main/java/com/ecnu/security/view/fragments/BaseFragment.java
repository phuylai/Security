package com.ecnu.security.view.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.MainActivity;
import com.ecnu.security.R;
import com.ecnu.security.Util.ResourceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phuylai on 2017/4/26.
 */

public abstract  class BaseFragment extends Fragment {
    protected MainActivity activity;
    protected View rootView;
    protected int layoutId = -1;
    private int titleId = -1;
    protected Context context;
    protected Resources resources;
    protected Toolbar toolbar;
    protected BottomNavigationView bottomNavigationView;
    protected boolean backIndicator = false;
    protected boolean bottomView = true;

    protected Map<String,SelectedItemCallBackListener> listenerMap = new HashMap<>();
    public interface SelectedItemCallBackListener{
        void onItemSelected(Object selectedItem);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        resources = context.getResources();
        activity = (MainActivity) getActivity();
        toolbar = activity.toolbar;
        bottomNavigationView = activity.bottomNavigationView;
    }

    public void setBackIndicator(){
        activity.setBackIndicator(backIndicator);
        toolbar.setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary));
    }

    public void setBottomView(){
        activity.setBottomNavigationView(bottomView);
        bottomNavigationView.setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(layoutId != -1){
            rootView = inflater.inflate(layoutId,null);
        }
        findViews(rootView);
        return rootView;
    }

    protected abstract void findViews(View rootView);

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if(activity == null)
            return;
        if(activity instanceof MainActivity){
            this.activity = (MainActivity) activity;
            setBackIndicator();
            setBottomView();
            changeTitle();
            this.activity.onFragmentResume(this);
        }
    }

    public abstract void changeTitle();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        if(toolbar != null){
            toolbar.setTitle(getResources().getString(titleId));
        }
    }

    public abstract void setListener();

    public void initMenu(Context context,Menu menu){
        clearMenu(menu);
    }

    protected void clearMenu(Menu menu){
        if(menu != null){
            menu.clear();
        }
    }

    public void registerSelectedCallBackListener(SelectedItemCallBackListener callBackListener){
        listenerMap.put(callBackListener.toString(),callBackListener);
    }

    protected void itemSelected(Object selectedItem){
        for(SelectedItemCallBackListener itemCallBackListener:listenerMap.values()){
            itemCallBackListener.onItemSelected(selectedItem);
        }
    }

    protected void callItemSelected(Object selecctedItem){
        activity.actionBarClick();
        itemSelected(selecctedItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity = null;
        rootView = null;
        if(listenerMap != null) {
            listenerMap.clear();
            listenerMap = null;
        }
    }

    public boolean onBackPressed(){
        /*if(backIndicator){
            activity.goToMainPageFragment();
            return true;
        }*/
        return false;
    }
}
