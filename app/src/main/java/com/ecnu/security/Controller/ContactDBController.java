package com.ecnu.security.Controller;

import android.content.Context;

import com.ecnu.security.Model.Database.ContactDBProcess;
import com.ecnu.security.Model.TrustedContact;

import java.util.Collection;

/**
 * Created by Phuylai on 2017/6/17.
 */

public class ContactDBController {

    public static void saveContactDB(Context context, TrustedContact contact, String clientID){
        ContactDBProcess contactDBProcess = DBController.getContactDB(context);
        if(contactDBProcess == null){
            return;
        }
        contactDBProcess.SaveContactDB(clientID,contact);
    }

    public static Collection<TrustedContact> getContactDB(Context context, String clientid){
        ContactDBProcess contactDBProcess = DBController.getContactDB(context);
        if(contactDBProcess == null){
            return null;
        }
        return contactDBProcess.loadContact(clientid);
    }
}
