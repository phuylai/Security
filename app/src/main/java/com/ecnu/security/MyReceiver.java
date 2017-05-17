package com.ecnu.security;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ecnu.security.Controller.ObservableObject;
import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.Model.AlertDevice;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.view.activities.LoadingActivity;

/**
 * Created by Phuylai on 2017/5/5.
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.ecnu.security")){
           // ((MainActivity) context).removeTimer();
            //ObservableObject.getInstance().updateValue(null);
            int alertMsg = 0;
            AlertDevice msg = (AlertDevice) intent.getSerializableExtra(Constants.PARAM_VALUE);
            String visible = intent.getStringExtra(Constants.VISIBLE);
            MLog.i("receiver",msg.getModule());
            Intent resultIntent = new Intent(context, LoadingActivity.class);
            resultIntent.putExtra(Constants.PARAM_VALUE,msg);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.icon)
                            .setAutoCancel(true)
                            .setContentTitle(
                                   context.getResources().getString(R.string.app_name))
                            .setContentText(msg.getModule())
                            .setTicker(msg.getModule());
            if(visible.equals("false")) {
                mBuilder.setContentIntent(pendingIntent);
            }
            int mNotificationId = Constants.NOTIFICATION;
            NotificationManager mNotifyMgr =
                    (NotificationManager) context
                            .getSystemService(
                                    Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }

    }
}
