package com.zoom.netmiddleframework.net.retrofit;

import com.zoom.netmiddleframework.net.IHttpCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallBack implements Callback {

    IHttpCallBack iRequestCallBack;

    public RetrofitCallBack(IHttpCallBack iRequestCallBack) {
        this.iRequestCallBack = iRequestCallBack;
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
                if (iRequestCallBack != null) {
                    iRequestCallBack.success(response.body().toString());
                }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        if (iRequestCallBack != null) {
            iRequestCallBack.fail(t);
        }
    }

}
