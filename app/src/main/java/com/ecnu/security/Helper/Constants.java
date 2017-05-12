package com.ecnu.security.Helper;

/**
 * Created by Phuylai on 2017/4/25.
 */

public class Constants {
    //first run flag
    public static final String FIRST_RUN_TAG = "first_run_tag";

    public static String _APPID = "7e05d7d6-f987-11e6-9d95-00163e103941";

    public static String _APPID(){
        return _APPID;
    }

    public static String SERVICE_NAME = "_easylink._tcp.local.";

    public static String CLOUD_PORT = "1883";

    public static String LISTEN_HOST = "v2.fogcloud.io";

    //url
    public static String _APIHOST = "https://v2.fogcloud.io";

    private static String _URLHEAD() {
        return _APIHOST + "/enduser/";
    }

    public static String UPDATENAME() {
        return _URLHEAD() + "updateUserInfo/";
    }

    public static String GETUSERINFO(){
        return _URLHEAD() + "getUserInfo/";
    }

    //debug true or false
    public static final boolean DEBUG_ENABLE = true;


    public final static String POP_TO_HOME = "HOMELIST_FRAGMENT";

    // setAlpha
    public static final int DEF_OPAQUE = 255;
    public static final int ENABLE_OPAQUE = (int) (DEF_OPAQUE * 0.6);
    public static final int DISABLE_OPAQUE = (int) (DEF_OPAQUE * 0.3);

    //version,logout
    public static final String PARAM_VERSION = "version";
    public static final String VISIBLE = "visible";
    public static final String LOGOUT_REASON = "LOGOUT_REASON";
    public final static String POP_TO_HOMELIST_BACKNAME = "HOMELIST_FRAGMENT";

    //message what value
    public static final int ERROR_INDEX = -1;
    public static final int SECOND_TICK = -5;
    public static final int TIMER_END = -6;
    public static final int DEVICE_MSG = -7;
    public static final int NOTIFICATION = -8;
    public static final int ONE_SECOND = 1000;
    public static final int MINUTE_SECOND = 60;
    public static final int THREE_MIN = 180;

    //user
    public final static String PARAM_USERNAME = "username";
    public final static String PARAM_PASSWORD = "password";
    public final static String PARAM_CLIENTID = "clientid";
    public final static String PARAM_TOKEN = "token";
    public final static String PARAM_FOG_HOST = "foghost";
    public final static String PARAM_APP_ID = "fogappid";
    public final static String PARAM_NICKNAME = "nickname";
    public final static String PARAM_REALNAME = "realname";
    public final static String PARAM_DATA = "data";
    public final static String PARAM_PAYLOAD = "payload";
    public final static String PARAM_DEVICEINFO = "device_info";
    public final static String PARAM_IR = "infrared_reflective";

    //Language
    public static final String PARAM_LANGUAGE = "language";

    //password
    public static final String PASSWORD_RULES_REGEX = "^[\\Sa-zA-Z0-9_.]{6,15}$";
    public static final int PASSWORD_MINNUM = 6;
    public static final int PASSWORD_MAXNUM = 15;
    public static final int FULLNAME_LENGTH = 64;

    //mode
    public static final String PARAM_MODE = "mode";
    public static final String MODE_PEACE = "peace";
    public static final String MODE_PANIC = "panic";
    public static final String MODE_AWAY = "away";
    public static final String MODE_WORK = "work";

    //Register
    public static final String PARAM_ACCOUNT = "account";

    //Fragment
    public static final String FRAG_SETTING = "Setting";
    public static final String FRAG_DETAIL = "Detail";
    public static final String FRAG_MODE = "mode";

    //Bunlde
    public static final String PARAM_VALUE = "value";
    public static final String PARAM_ACTION = "action";

    //device
    public static final int PARAM_FIND = 100;
    public static final String PARAM_UNCHECK = "UNCHECK";
    public static final String PARAM_DEV_NAME = "Name";
    public static final String PARAM_IP = "IP";
    public static final String PARAM_MAC = "MAC";
    public static final String PARAM_PORT = "Port";
    public static final String PARAM_SUPER_USER = "IsHaveSuperUser";
    public static final String PARAM_PRODUCT_ID = "FogProductId";
    public static final String PARAM_REMAIN = "RemainingUserNumber";
    public static final String PARAM_MODEL = "Model";
    public static final String PARAM_PROTOCOL = "Protocol";
    public static final String PARAM_PW = "device_pw";
    public static final String PARAM_DEVNAME = "device_name";
    public static final String PARAM_SUB = "is_sub";
    public static final String PARAM_mac = "mac";
    public static final String PARAM_ONLINE = "online";
    public static final String PARAM_DEV_ID = "device_id";
    public static final String PARAM_ROLE = "role";
    public static final String PARAM_DEVICE_ID = "deviceid";
    public static final String PARAM_ALARM = "alarm";
    public static final String PARAM_LED = "led";
    public static final String PARAM_NOTI = "noti";
    public static final String PARAM_REDIRECT = "redirect";

    //login
    public static final String PARAM_LOGIN = "PARAM_LOGIN";






}
