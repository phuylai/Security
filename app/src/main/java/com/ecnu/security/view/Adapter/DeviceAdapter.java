package com.ecnu.security.view.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecnu.security.Model.ActionType;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.R;
import com.ecnu.security.Util.StringUtil;

import java.util.List;

/**
 * Created by Phuylai on 2017/5/4.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private Context context;
    private List<DeviceModel> deviceModels;
    private DeviceClickListener deviceClickListener;

    public interface DeviceClickListener{
        void deviceClick(DeviceModel deviceModel);
    }

    private void deviceClick(DeviceModel deviceModel){
        if(deviceClickListener != null)
            deviceClickListener.deviceClick(deviceModel);
    }

    public DeviceAdapter(Context context,List<DeviceModel> deviceModels,DeviceClickListener deviceClickListener){
        this.context = context;
        this.deviceModels = deviceModels;
        this.deviceClickListener = deviceClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        DeviceModel deviceModel = deviceModels.get(i);
        viewHolder.tv_name.setText(deviceModel.getName());
        if(!StringUtil.isNull(deviceModel.getIP())) {
            viewHolder.tv_decs.setText(deviceModel.getIP());
        }else{
            viewHolder.tv_decs.setText(deviceModel.getMac());
        }
        if(!StringUtil.isNull(deviceModel.getOnline())) {
            if (Boolean.parseBoolean(deviceModel.getOnline())) {
                viewHolder.iv_icon.setImageResource(R.drawable.online);
            } else {
                viewHolder.iv_icon.setImageResource(R.drawable.offline);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(deviceModels == null)
            return 0;
        return deviceModels.size();
    }

    @Override
    public long getItemId(int position) {
        if(deviceModels == null)
            return 0;
        return deviceModels.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_item,viewGroup,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        TextView tv_decs;
        ImageView iv_icon;
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_device_name);
            tv_decs = (TextView) view.findViewById(R.id.tv_desc);
            iv_icon = (ImageView) view.findViewById(R.id.iv_action);
            cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            DeviceModel deviceModel = deviceModels.get(getAdapterPosition());
            deviceClick(deviceModel);
        }
    }
}
