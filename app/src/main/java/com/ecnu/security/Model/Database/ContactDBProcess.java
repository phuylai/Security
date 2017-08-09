package com.ecnu.security.Model.Database;

import android.content.ContentValues;
import android.content.Context;

import com.bst.akario.db.TableOperator;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.Model.TrustedContact;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Phuylai on 2017/6/17.
 */

public class ContactDBProcess {
    private ContactDB contactDB;
    private final static ContentValues contentValues = new ContentValues();
    public ContactDBProcess(Context context){
        contactDB = ContactDB.getInstance(context);
    }
    final static String getContact =
            "SELECT * FROM "
                    + ContactDB.TABLE_CONTACT
                    + " WHERE "
                    + DeviceDB.KEY_CLIENTID
                    + " =?";

    public boolean updateContact(final String oldName, final String oldPhone,
                                 final String newName, final String newPhone){
        synchronized (contactDB){
            TableOperator tableOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    contentValues.clear();
                    contentValues.put(ContactDB.KEY_PHONE,newPhone);
                    contentValues.put(ContactDB.KEY_NAME,newName);
                    db.update(ContactDB.TABLE_CONTACT,contentValues,ContactDB.KEY_NAME +
                            " =? AND " + ContactDB.KEY_PHONE + " =?",new String[]{oldName,oldPhone});
                }
            };
            MLog.i("contact","update");
            return contactDB.writeOperator(tableOperator);
        }
    }

    public boolean removeContact(final String name,final String phone){
        synchronized (contactDB){
            TableOperator delete = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    db.delete(ContactDB.TABLE_CONTACT,ContactDB.KEY_NAME +
                            " =? AND " + ContactDB.KEY_PHONE + " =?",new String[]{name,phone});
                }
            };
            MLog.i("contact","delete");
            return contactDB.writeOperator(delete);
        }
    }

    public Collection<TrustedContact> loadContact(final String clientid){
        final List<TrustedContact> trustedContacts = new ArrayList<>();
        TableOperator tableOperator = new TableOperator() {
            @Override
            public void doWork(SQLiteDatabase db) {
                Cursor cursor = db.rawQuery(getContact,new String[]{clientid});
                if(cursor.moveToFirst()){
                    do{
                        TrustedContact contact = getContacts(cursor);
                        if(contact != null){
                            trustedContacts.add(contact);
                        }
                    }while(cursor.moveToNext());
                }
                cursor.close();
            }
        };
        contactDB.readOperator(tableOperator);
        return trustedContacts;
    }

    private TrustedContact getContacts(Cursor cursor){
        if(cursor == null)
            return null;
        String name = cursor.getString(cursor.getColumnIndex(ContactDB.KEY_NAME));
        String phone =  cursor.getString(cursor.getColumnIndex(ContactDB.KEY_PHONE));
        return new TrustedContact(phone,name);
    }

    public boolean SaveContactDB(final String clientid, final TrustedContact contact){
        synchronized (contactDB){
            TableOperator insert = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    saveContacts(db,clientid,contact);
                }
            };
            return contactDB.writeOperator(insert);
        }
    }

    private boolean saveContacts(SQLiteDatabase db, final String clientID, final TrustedContact contact){
        contentValues.clear();
        contentValues.put(ContactDB.KEY_CLIENTID,clientID);
        contentValues.put(ContactDB.KEY_NAME,contact.getName());
        contentValues.put(ContactDB.KEY_PHONE,contact.getPhonenumber());
        db.insertWithOnConflict(ContactDB.TABLE_CONTACT,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        MLog.i("contact db","save");
        return true;
    }


}
