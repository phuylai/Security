package com.ecnu.security;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.view.activities.LoadingActivity;

/**
 * Created by Phuylai on 2017/5/5.
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.ecnu.security")){
            String msg = intent.getStringExtra(Constants.PARAM_VALUE);
            String visible = intent.getStringExtra(Constants.VISIBLE);
            MLog.i("receiver",msg);
            Intent resultIntent = new Intent(context, LoadingActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.icon)
                            .setContentTitle(
                                   context.getResources().getString(R.string.app_name))
                            .setContentText(msg)
                            .setTicker(msg);
            if(!StringUtil.isNull(visible)) {
                mBuilder.setContentIntent(pendingIntent);
            }
            mBuilder.setAutoCancel(true);
            int mNotificationId = Constants.NOTIFICATION;
            NotificationManager mNotifyMgr =
                    (NotificationManager) context
                            .getSystemService(
                                    Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }

    }
}
