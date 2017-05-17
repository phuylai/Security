package com.ecnu.security.Controller;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Model.AccountModel;
import com.ecnu.security.Model.SMSModel;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Phuylai on 2017/5/13.
 */

public interface ApiCallInterface {
    @Headers(Constants._HEADER)
    @POST(Constants._SMSURL)
    Call<ResponseBody> getSentSMS(@Body RequestBody string);

}
