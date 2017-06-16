package com.ecnu.security.Controller;

import android.content.Context;
import android.widget.Toast;

import com.ecnu.security.Model.DeviceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Phuylai on 2017/4/26.
 */

public class CurrentSession {
    private static Toast toast = null;
    private static CurrentSession instance = null;
    public static Toast getToast() {
        return toast;
    }

    private static CurrentSession getInstance() {
        if (instance == null) {
            instance = new CurrentSession();
        }
        return instance;
    }

    public static void saveDevices(Context context, DeviceModel deviceModels,String clientID){
        if(deviceModels == null)
            return;
        DeviceDBController.saveDeviceToDB(context,deviceModels,clientID);
    }

    public static List<DeviceModel> getDevices(Context context,String clientID){
        Collection<DeviceModel> deviceModels = DeviceDBController.getDeviceFromDB(context,clientID);
        if(deviceModels != null && deviceModels.size() > 0){
            return new ArrayList<>(deviceModels);
        }
        return null;
    }
}
