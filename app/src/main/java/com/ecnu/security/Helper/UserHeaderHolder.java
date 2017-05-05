package com.ecnu.security.Helper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ecnu.security.R;
import com.ecnu.security.Util.StringUtil;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class UserHeaderHolder extends BaseViewHolder {

    private Context context;
    private TextView tv_phone;
    private TextView tv_name;
    private String phone;
    private String nickname;

    public UserHeaderHolder(Context context, String phone,String nickname) {
        super(context);
        this.context = context;
        this.phone = phone;
        this.nickname = nickname;
        setView();
    }

    private void setView(){
        tv_phone.setText(phone);
        if(!StringUtil.isNull(nickname)){
            tv_name.setVisibility(View.VISIBLE);
            tv_name.setText(nickname);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_header;
    }

    @Override
    protected void findViews(View view) {
        tv_name = (TextView) view .findViewById(R.id.tv_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
    }

    @Override
    protected void setEnabled(boolean enabled) {

    }

    @Override
    protected void onDestroy() {
        tv_name = null;
    }
}
