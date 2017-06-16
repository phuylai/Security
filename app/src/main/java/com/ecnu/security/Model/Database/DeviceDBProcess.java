package com.ecnu.security.Model.Database;

import android.content.ContentValues;
import android.content.Context;

import com.bst.akario.db.TableOperator;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.Model.DeviceModel;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Phuylai on 2017/5/6.
 */

public class DeviceDBProcess {
    private DeviceDB deviceDB;
    private final static ContentValues contentValues = new ContentValues();
    public DeviceDBProcess(Context context){
        deviceDB = DeviceDB.getInstance(context);
    }
    final static String getDevice =
            "SELECT * FROM "
            + DeviceDB.TABLE_DEVICE
            + " WHERE "
            + DeviceDB.KEY_CLIENTID
            + " =?";

    public Collection<DeviceModel> loadDevices(final String clientid){
        final List<DeviceModel> deviceModels = new ArrayList<>();
        TableOperator tableOperator = new TableOperator() {
            @Override
            public void doWork(SQLiteDatabase db) {
                Cursor cursor = db.rawQuery(getDevice,new String[]{clientid});
                if(cursor.moveToFirst()){
                    do{
                        DeviceModel deviceModel = getDeviceInfo(cursor);
                        if(deviceModel != null){
                            deviceModels.add(deviceModel);
                        }
                    }while(cursor.moveToNext());
                }
                cursor.close();
            }
        };
        deviceDB.readOperator(tableOperator);
        return deviceModels;
    }

    private DeviceModel getDeviceInfo(Cursor cursor){
        if(cursor == null)
            return null;
        String name = cursor.getString(cursor.getColumnIndex(DeviceDB.KEY_NAME));
        String mac =  cursor.getString(cursor.getColumnIndex(DeviceDB.KEY_MAC));
        String pw =  cursor.getString(cursor.getColumnIndex(DeviceDB.KEY_PW));
        String sub =  cursor.getString(cursor.getColumnIndex(DeviceDB.KEY_SUB));
        String role =  cursor.getString(cursor.getColumnIndex(DeviceDB.KEY_ROLE));
        String online =  cursor.getString(cursor.getColumnIndex(DeviceDB.KEY_ONLINE));
        String id =  cursor.getString(cursor.getColumnIndex(DeviceDB.KEY_DEVID));
        return new DeviceModel(name,mac,pw,sub,role,online,id);
    }

    public boolean SaveDeviceDB(final String clientid, final DeviceModel deviceModel){
        synchronized (deviceDB){
            TableOperator insert = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    SaveDevice(db,clientid,deviceModel);
                }
            };
            return deviceDB.writeOperator(insert);
        }
    }

    private boolean SaveDevice(SQLiteDatabase db, final String clientID, final DeviceModel deviceModel){
        contentValues.clear();
        contentValues.put(DeviceDB.KEY_CLIENTID,clientID);
        contentValues.put(DeviceDB.KEY_NAME,deviceModel.getName());
        contentValues.put(DeviceDB.KEY_MAC,deviceModel.getMac());
        contentValues.put(DeviceDB.KEY_PW,deviceModel.getDevPW());
        contentValues.put(DeviceDB.KEY_SUB,deviceModel.getIsSub());
        contentValues.put(DeviceDB.KEY_ROLE,deviceModel.getRole());
        contentValues.put(DeviceDB.KEY_ONLINE,deviceModel.getOnline());
        contentValues.put(DeviceDB.KEY_DEVID,deviceModel.getDevId());
        db.insertWithOnConflict(DeviceDB.TABLE_DEVICE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        MLog.i("device db","save");
        return true;

    }
}
