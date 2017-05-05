package com.ecnu.security.Helper;

import android.content.Context;

import com.ecnu.security.Model.ActionType;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.R;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class DetailItemMaker {
    private DetailItemHolder detailItemHolder;
    private Context context;
    private DetailItemHolder.ItemClickListener itemClickListener;

    public DetailItemMaker(Context context,
                           DetailItemHolder.ItemClickListener itemClickListener) {
        detailItemHolder = new DetailItemHolder(context,R.drawable.ic_password,R.string.change_password,
                ActionType.PASSWORD,true,itemClickListener);
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public BaseViewHolder getDetailItemHolder(){
        return detailItemHolder;
    }

    public BaseViewHolder passwordHolder(){
        return new DetailItemHolder(context, R.drawable.ic_password,R.string.change_password,
                ActionType.PASSWORD,true,itemClickListener);
    }

    public BaseViewHolder phoneHolder(){
        return new DetailItemHolder(context, R.drawable.ic_action_phone,R.string.change_phonne,
                ActionType.PHONE,true,itemClickListener);
    }
    public BaseViewHolder nameHolder(){
        return new DetailItemHolder(context, R.drawable.ic_user,R.string.change_name,
                ActionType.NAME,true,itemClickListener);
    }
    public BaseViewHolder deviceHolder(){
        return new DetailItemHolder(context, R.drawable.ic_action_list,R.string.device_list,
                ActionType.DEV_LIST,true,itemClickListener);
    }
    public BaseViewHolder addDevHolder(){
        return new DetailItemHolder(context, R.drawable.ic_action_add,R.string.add_device,
                ActionType.ADD_DEV,true,itemClickListener);
    }

    public BaseViewHolder addNameHolder(){
        return new DetailItemHolder(context,R.drawable.ic_electrical_sensor,R.string.device_name,
                ActionType.NAME,true,itemClickListener);
    }

    public BaseViewHolder addIdHolder(){
        return new DetailItemHolder(context,R.drawable.ic_electrical_sensor,R.string.device_id,
                ActionType.PHONE,true,itemClickListener);
    }

    public BaseViewHolder addUnbindHolder(){
        return new DetailItemHolder(context,R.drawable.ic_electrical_sensor,R.string.unbind,
                ActionType.ADD_DEV,true,itemClickListener);
    }

    public BaseViewHolder addHeader(String phone,String nickname){
        return new UserHeaderHolder(context,phone,nickname);
    }

    public BaseViewHolder addButton(ButtonHolder.LogoutListener logoutListener){
        return new ButtonHolder(context,logoutListener);
    }

    public BaseViewHolder addDeviceHeader(DeviceModel deviceModel){
        return new DeviceHolder(context,deviceModel);
    }

}
