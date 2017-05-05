package com.ecnu.security.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.common.design.MaterialDialog;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.R;

import static com.ecnu.security.Util.ResourceUtil.getString;

/**
 * Created by Phuylai on 2017/5/5.
 */

public class DialogUtil {
    public static void showDeviceDetailDialog(Context context, DeviceModel deviceModel,
                                              DialogListener dialogListener){
        final DialogListener listener = dialogListener;
        View view = View.inflate(context, R.layout.device_detail,null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvId = (TextView) view.findViewById(R.id.tv_id);
        TextView tvSub = (TextView) view.findViewById(R.id.tv_sub);
        TextView tvMac = (TextView) view.findViewById(R.id.tv_mac);
        TextView tvRole = (TextView) view.findViewById(R.id.tv_role);
        tvName.setText(deviceModel.getName());
        tvId.setText(deviceModel.getDevId());
        tvSub.setText(deviceModel.getIsSub());
        tvMac.setText(deviceModel.getMac());
        String role = "";
        switch (Integer.parseInt(deviceModel.getRole())){
            case 1:
                role = getString(R.string.role_1);
                break;
            case 2:
                role = getString(R.string.role_2);
                break;
            case 3:
                role = getString(R.string.role_3);
                break;
        }
        tvRole.setText(role);
        new MaterialDialog.Builder(context)
                .setContentView(view)
                .setPositiveButton(getString(R.string.device_edit), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null)
                            listener.yes();
                        return false;
                    }
                })
                .setNegativeButton(getString(R.string.back), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null)
                            listener.no();
                        return false;
                    }
                }).show();

    }

    public static void showDialog(Context context, int title, int message,DialogListener dialogListener){
        final DialogListener listener = dialogListener;
        new MaterialDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null)
                            listener.yes();
                        return false;
                    }
                })
                .setNegativeButton(R.string.no, new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null)
                            listener.no();
                        return false;
                    }
                }).show();
    }


    public interface DialogListener{
        void yes();
        void no();
    }
}
