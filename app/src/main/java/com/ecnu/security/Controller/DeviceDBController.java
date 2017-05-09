package com.ecnu.security.Controller;

import android.content.Context;

import com.ecnu.security.Model.Database.DeviceDB;
import com.ecnu.security.Model.Database.DeviceDBProcess;
import com.ecnu.security.Model.DeviceModel;

import java.util.Collection;

/**
 * Created by Phuylai on 2017/5/6.
 */

public class DeviceDBController {
    public static void saveDeviceToDB(Context context, DeviceModel deviceModel, String clientID){
        DeviceDBProcess deviceDBProcess = DBController.getDeviceDB(context);
        if(deviceDBProcess == null){
            return;
        }
        deviceDBProcess.SaveDeviceDB(clientID,deviceModel);
    }

    public static Collection<DeviceModel> getDeviceFromDB(Context context, String clientid){
        DeviceDBProcess deviceDBProcess = DBController.getDeviceDB(context);
        if(deviceDBProcess == null){
            return null;
        }
        return deviceDBProcess.loadDevices(clientid);
    }
}
