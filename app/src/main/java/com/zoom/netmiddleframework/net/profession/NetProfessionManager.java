package com.zoom.netmiddleframework.net.profession;

import android.content.Context;

import com.zoom.netmiddleframework.net.HttpRequestManager;
import com.zoom.netmiddleframework.net.RequestData;
import com.zoom.netmiddleframework.net.ResendManager;
import com.zoom.netmiddleframework.net.retrofit.RetrofitRequestManager;
import java.util.List;
import java.util.UUID;

/**
 * 网络业务管理类：用于整理网络请求的数据
 */
public class NetProfessionManager {

    static HttpRequestManager mHttpRequestManager;
    static String baseUrl;
    static String deviceId;

    public static HttpRequestManager getRequestManager(Context context) {
        if (mHttpRequestManager == null) {
            baseUrl = "http://" ;
            deviceId = "";
            mHttpRequestManager = HttpRequestManager.getInstance();
            mHttpRequestManager.init(context, RetrofitRequestManager.class.getName(), baseUrl, true, 30);
        }

        return mHttpRequestManager;
    }

    private static void sendRequest(Context context, String requestType, String url, String deviceId, String data) {
        RequestData mRequestData = new RequestData(requestType, UUID.randomUUID().toString(), url, deviceId, data);
        getRequestManager(context).sendRequest(new NetProfessionCallBack(mRequestData));
    }
}
