package com.ecnu.security.Model;

import com.ecnu.security.Helper.Constants;

/**
 * Created by Phuylai on 2017/5/3.
 */

public enum ActionType {

    PASSWORD("password"),PHONE("phone"),NAME("name"),DEV_LIST("dev_list"),ADD_DEV("add_dev"),
    ONLINE("online"),PEACE(Constants.MODE_PEACE),PANIC(Constants.MODE_PANIC),AWAY(Constants.MODE_AWAY),
    WORK(Constants.MODE_WORK),ALARM("alarm"),LED("led"),REDIRECT("redirect"),NOTI("noti"),TRUSTED("trusted"),
    ADD("add"),EDIT("edit"),CHANGE("change");

    private final String text;

    ActionType(String text){
        this.text = text;
    }

    public boolean equal(String value){
        if(text == null)
            return false;
        return text.equals(value);
    }

    @Override
    public String toString() {
        return text;
    }

    public static ActionType getType(String value){
        if(PASSWORD.equal(value)){
            return PASSWORD;
        }
        if(PHONE.equal(value)){
            return PHONE;
        }
        if(NAME.equal(value)){
            return NAME;
        }
        if(DEV_LIST.equal(value)){
            return DEV_LIST;
        }
        if(ONLINE.equal(value)){
            return ONLINE;
        }
        if(AWAY.equal(value)){
            return AWAY;
        }
        if(PEACE.equal(value)){
            return PEACE;
        }
        if(PANIC.equal(value)){
            return PANIC;
        }
        if(WORK.equal(value)){
            return WORK;
        }
        if(ALARM.equal(value)){
            return ALARM;
        }
        if(LED.equal(value)){
            return LED;
        }
        if(TRUSTED.equal(value)){
            return TRUSTED;
        }
        if(NOTI.equal(value)){
            return NOTI;
        }
        if(REDIRECT.equal(value)){
            return REDIRECT;
        }
        if(ADD.equal(value)){
            return ADD;
        }
        if(EDIT.equal(value)){
            return EDIT;
        }
        if(CHANGE.equal(value)){
            return CHANGE;
        }
        return ADD_DEV;
    }

}
