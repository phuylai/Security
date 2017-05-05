package com.ecnu.security.Helper;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.R;

/**
 * Created by Phuylai on 2017/5/5.
 */

public class DeviceHolder extends BaseViewHolder {

    private TextView tv_name;
    private TextView tv_decs;
    private ImageView iv_icon;

    private DeviceModel deviceModel;

    @Override
    protected void onDestroy() {
        tv_decs = null;
        tv_decs = null;
        iv_icon = null;
    }

    public DeviceHolder(Context context, DeviceModel deviceModel){
        super(context);
        this.deviceModel = deviceModel;
        setView();
    }

    @Override
    protected void findViews(View view) {
        tv_name = (TextView) view.findViewById(R.id.tv_device_name);
        tv_decs = (TextView) view.findViewById(R.id.tv_desc);
        iv_icon = (ImageView) view.findViewById(R.id.iv_action);
    }

    private void setView(){
        tv_name.setText(deviceModel.getName());
        tv_decs.setText(deviceModel.getMac());
        if (Boolean.parseBoolean(deviceModel.getOnline())) {
            iv_icon.setImageResource(R.drawable.online);
        } else {
            iv_icon.setImageResource(R.drawable.offline);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.device_item;
    }

    @Override
    protected void setEnabled(boolean enabled) {

    }

    @Override
    protected void setListeners() {

    }
}
