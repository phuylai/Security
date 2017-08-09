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

    public static boolean updateContact(Context context,TrustedContact newContact,
                                        TrustedContact oldContact){
        ContactDBProcess contactDBProcess = DBController.getContactDB(context);
        if(contactDBProcess == null){
            return false;
        }
        return contactDBProcess.updateContact(oldContact.getName(),
                oldContact.getPhonenumber(),newContact.getName(),newContact.getPhonenumber());
    }

    public static boolean removeContact(Context context,TrustedContact contact){
        ContactDBProcess contactDBProcess = DBController.getContactDB(context);
        if(contactDBProcess == null){
            return false;
        }
        return contactDBProcess.removeContact(contact.getName(),contact.getPhonenumber());
    }
}
