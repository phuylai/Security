package com.ecnu.security.Controller;

import android.content.Context;

import com.ecnu.security.Model.Database.ContactDBProcess;
import com.ecnu.security.Model.Database.DeviceDBProcess;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phuylai on 2017/5/6.
 */

public class DBController {
    private static Map<Context, DeviceDBProcess> deviceDBProcessHashMap = new HashMap<>();
    private static Map<Context, ContactDBProcess> contactDBProcessHashMap = new HashMap<>();

    public static DeviceDBProcess getDeviceDB(Context context){
        if(context == null){
            return null;
        }
        Context applicationContext = context.getApplicationContext();
        DeviceDBProcess deviceDBProcess = deviceDBProcessHashMap.get(applicationContext);
        if(deviceDBProcess == null){
            deviceDBProcess = new DeviceDBProcess(applicationContext);
            deviceDBProcessHashMap.put(applicationContext,deviceDBProcess);
        }
        return deviceDBProcess;
    }

    public static ContactDBProcess getContactDB(Context context){
        if(context == null){
            return null;
        }
        Context applicationContext = context.getApplicationContext();
        ContactDBProcess contactDBProcess = contactDBProcessHashMap.get(applicationContext);
        if(contactDBProcess== null){
            contactDBProcess = new ContactDBProcess(applicationContext);
            contactDBProcessHashMap.put(applicationContext,contactDBProcess);
        }
        return contactDBProcess;
    }
}
