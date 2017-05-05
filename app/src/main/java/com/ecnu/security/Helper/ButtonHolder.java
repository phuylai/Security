package com.ecnu.security.Helper;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.ecnu.security.MainActivity;
import com.ecnu.security.R;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.view.activities.LoginActivity;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class ButtonHolder extends BaseViewHolder {

    private Context context;
    private Button btnOut;
    private LogoutListener listener;


    public ButtonHolder(Context context, LogoutListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public interface LogoutListener{
        void logout();
    }

    public void logout(){
        if(listener != null)
            listener.logout();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_button;
    }

    @Override
    protected void findViews(View view) {
        btnOut = (Button) view.findViewById(R.id.btn_out);
        btnOut.getBackground().mutate().setAlpha(Constants.DEF_OPAQUE);
    }

    @Override
    protected void setListeners() {
        btnOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_out:
                logout();
                break;
        }
    }

    @Override
    protected void setEnabled(boolean enabled) {

    }

    @Override
    protected void onDestroy() {

    }
}
