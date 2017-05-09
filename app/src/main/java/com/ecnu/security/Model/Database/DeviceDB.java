package com.ecnu.security.Model.Database;

import android.content.Context;

import com.bst.akario.db.models.DBModel;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Phuylai on 2017/5/6.
 */

public class DeviceDB extends DBModel
{

    private static DeviceDB instance;
    private static final int VERSION = 1;
    private static final String DBNAME = "device.db";

    public DeviceDB(Context context){
        super(context,DBNAME,null,VERSION);
    }

    public static DeviceDB getInstance(Context context){
        if(instance == null)
            instance = new DeviceDB(context);
        return instance;
    }

    public static final String TABLE_DEVICE = "table_device";
    public static final String KEY_ID = "_id";
    public static final String KEY_CLIENTID = "_clientID";
    public static final String KEY_NAME = "_name";
    public static final String KEY_MAC = "_mac";
    public static final String KEY_PW = "_pw";
    public static final String KEY_SUB ="_sub";
    public static final String KEY_ROLE = "_role";
    public static final String KEY_ONLINE = "_online";
    public static final String KEY_DEVID ="_deviceID";

    @Override
    protected String getReadLogKey() {
        return "Device DB Read Operator:";
    }

    @Override
    protected String getWriteLogKey() {
        return "Device DB Write Operator:";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DEVICE
                + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_CLIENTID + " varchar(40) unique, "
                + KEY_NAME + " varchar(25), "
                + KEY_MAC + " varchar(25), "
                + KEY_PW + " varchar(10), "
                + KEY_SUB + " varchar(10), "
                + KEY_ROLE + " varchar(5), "
                + KEY_ONLINE + " varchar(10), "
                + KEY_DEVID + " varchar(50))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE);
        onCreate(db);
    }


}
