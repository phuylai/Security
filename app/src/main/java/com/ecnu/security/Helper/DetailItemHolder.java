package com.ecnu.security.Helper;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecnu.security.Model.ActionType;
import com.ecnu.security.R;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class DetailItemHolder extends BaseViewHolder implements Cloneable{

    private int iconImageId;
    private int actionStringId;
    private ActionType actionType;
    private boolean clickAble;
    private ItemClickListener itemClickListener;

    private TextView tv_name;
    private ImageView iv_icon;
    private ImageView iv_navigate;

    public interface ItemClickListener{
        void psdClick();
        void nameClick();
        void phoneClick();
        void deviceListClick();
        void addDeviceClick();
        void notiClick();
        void redirectClick();
        void trustedContactClick();
        void changeAP();
    }

    public DetailItemHolder(Context context, int iconImageId, int actionStringId,
                            ActionType actionType, boolean clickAble, ItemClickListener itemClickListener) {
        super(context);
        this.iconImageId = iconImageId;
        this.actionStringId = actionStringId;
        this.actionType = actionType;
        this.clickAble = clickAble;
        this.itemClickListener = itemClickListener;
        setView();
    }

    private void setView(){
        if(iconImageId > 0){
            iv_icon.setImageResource(iconImageId);
        }
        if(actionType != null){
            switch (actionType){
                case PASSWORD:
                case PHONE:
                case NAME:
                case DEV_LIST:
                case ADD_DEV:
                case REDIRECT:
                case TRUSTED:
                case NOTI:
                case CHANGE:
                    tv_name.setTextColor(context.getResources().getColor(R.color.text_grey));
                    break;
            }
        }
        tv_name.setText(actionStringId);
        if(clickAble){
            iv_navigate.setVisibility(View.VISIBLE);
        }
    }

    public void NameClick(){
        if(itemClickListener != null)
            itemClickListener.nameClick();
    }

    public void passwordClick(){
        if(itemClickListener != null)
            itemClickListener.psdClick();
    }

    public void phoneClick(){
        if(itemClickListener != null)
            itemClickListener.phoneClick();
    }

    public void deviceListClick(){
        if(itemClickListener != null)
            itemClickListener.deviceListClick();
    }

    public void addNewDevice(){
        if(itemClickListener != null)
            itemClickListener.addDeviceClick();
    }

    public void redirect(){
        if(itemClickListener != null)
            itemClickListener.redirectClick();
    }

    public void notificationClick(){
        if(itemClickListener != null)
            itemClickListener.notiClick();
    }

    public void trustedClick(){
        if(itemClickListener != null)
            itemClickListener.trustedContactClick();
    }

    public void changeAP(){
        if(itemClickListener != null){
            itemClickListener.changeAP();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.detail_action_item;
    }

    @Override
    protected void findViews(View view) {
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        tv_name = (TextView) view.findViewById(R.id.tv_action_name);
        iv_navigate = (ImageView) view .findViewById(R.id.iv_action);
    }

    @Override
    public void onClick(View view) {
        if(actionType == null)
            return;
        switch (actionType){
            case PASSWORD:
                passwordClick();
                break;
            case PHONE:
                phoneClick();
                break;
            case NAME:
                NameClick();
                break;
            case DEV_LIST:
                deviceListClick();
                break;
            case ADD_DEV:
                addNewDevice();
                break;
            case REDIRECT:
                redirect();
                break;
            case TRUSTED:
                trustedClick();
                break;
            case NOTI:
                notificationClick();
                break;
            case CHANGE:
                changeAP();
                break;
        }
    }

    @Override
    protected void setEnabled(boolean enabled) {

    }

    @Override
    protected void onDestroy() {
        tv_name = null;
        iv_icon = null;
        iv_navigate = null;
    }
}
