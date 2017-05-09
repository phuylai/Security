package com.ecnu.security.Model;


import com.ecnu.security.Helper.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import io.fog.callbacks.MiCOCallBack;
import io.fog.helper.CommonFunc;
import io.fog.helper.Configuration;
import io.fog.httputils.HttpSendParam;

/**
 * Created by Phuylai on 2017/5/3.
 */

public class MicoUserExt{

    private HttpSendParam hsp = new HttpSendParam();
    private CommonFunc comfunc = new CommonFunc();

    /**
     * update user info
     *
     * @param nickname
     * @param micocb
     */
    public void setNickname(String nickname, MiCOCallBack micocb,String token) {
        if (comfunc.checkPara(nickname,token)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("nickname", nickname);
                hsp.doHttpPut(Constants.UPDATENAME(), postParam, micocb,token);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(micocb);
        }
    }

    /**
     * update user info
     *
     * @param mode
     * @param micocb
     */
    public void setMode(String mode, MiCOCallBack micocb,String token) {
        if (comfunc.checkPara(mode,token)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("realname", mode);
                hsp.doHttpPut(Constants.UPDATENAME(), postParam, micocb,token);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            comfunc.illegalCallBack(micocb);
        }
    }

    /**
     * get user info
     *
     * @param micocb
     */
    public void getUserInfo(MiCOCallBack micocb,String token) {
        if (comfunc.checkPara(token)) {
            hsp.doHttpGet(Constants.GETUSERINFO(),"", micocb,token);
        } else {
            comfunc.illegalCallBack(micocb);
        }
    }


}
