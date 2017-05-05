package com.ecnu.security.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Phuylai on 2017/5/3.
 */

public abstract  class BaseViewHolder implements View.OnClickListener {
    protected LayoutInflater layoutInflater;
    protected View rootView;
    protected Context context;

    public BaseViewHolder(Context context) {
        this.context = context;
        createRootViews(context);
        findViews(rootView);
        setListeners();
    }

    public abstract int getLayoutId();
    protected abstract void findViews(View view);
    protected abstract void setEnabled(boolean enabled);
    protected abstract void onDestroy();

    private void createRootViews(Context context){
        layoutInflater = LayoutInflater.from(context);
        int layoutId = getLayoutId();
        rootView = layoutInflater.inflate(layoutId,null);
    }

    protected void setListeners(){
        if(rootView == null)
            return;
        rootView.setOnClickListener(this);
    }

    public View getRootView() {
        return rootView;
    }

    @Override
    public void onClick(View view) {

    }
}
