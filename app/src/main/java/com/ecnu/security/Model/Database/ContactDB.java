package com.ecnu.security.Model.Database;

import android.content.Context;

import com.bst.akario.db.models.DBModel;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Phuylai on 2017/6/17.
 */

public class ContactDB extends DBModel {

    private static ContactDB instance;
    private static final int VERSION = 1;
    private static final String DBNAME = "contact.db";
    public static ContactDB getInstance(Context context){
        if(instance == null)
            return new ContactDB(context);
        return instance;
    }
    public ContactDB(Context context){
        super(context,DBNAME,null,VERSION);
    }

    public static final String TABLE_CONTACT = "table_contact";
    public static final String KEY_ID = "_id";
    public static final String KEY_CLIENTID = "_clientID";
    public static final String KEY_NAME = "_name";
    public static final String KEY_PHONE = "_phone";
    @Override
    protected String getReadLogKey() {
        return "contact DB Read Operator:";
    }

    @Override
    protected String getWriteLogKey() {
        return "contact DB Write Operator:";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CONTACT
                + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_CLIENTID + " varchar(40) unique, "
                + KEY_NAME + " varchar(25), "
                + KEY_PHONE + " varchar(25))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);
    }

}
