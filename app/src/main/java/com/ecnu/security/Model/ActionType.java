package com.ecnu.security.Model;

/**
 * Created by Phuylai on 2017/5/3.
 */

public enum ActionType {

    PASSWORD("password"),PHONE("phone"),NAME("name"),DEV_LIST("dev_list"),ADD_DEV("add_dev"),
    ONLINE("online");

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
        return ADD_DEV;
    }

}
