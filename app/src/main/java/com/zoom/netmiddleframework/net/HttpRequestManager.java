package com.zoom.netmiddleframework.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import java.lang.reflect.Constructor;

/**
 * 请求工具管理：
 */
public class HttpRequestManager {

    //网络是否通畅
    private static boolean isConnect = false;
    private static IHttpRequest mIHttpRequest;

    private static HttpRequestManager mHttpRequestManager;
    private ResendManager mResendManager;
    private HttpRequestManager() {
        mResendManager = ResendManager.getInstance();
    }

    public static HttpRequestManager getInstance() {
        synchronized (HttpRequestManager.class) {
            if (mHttpRequestManager == null) {
                mHttpRequestManager = new HttpRequestManager();
            }
        }
        return mHttpRequestManager;
    }

    /**
     * 传入的类必须在构造函数中对baseUrl进行初始化：第三方使用时，必须符合该要求
     * @param classPath
     * @param baseUrl
     */
    public void init(Context mContext, String classPath, String baseUrl, boolean isAllowResend, long mRsendNum) {
        try {
            Class<?> clazz = Class.forName(classPath);
            if (clazz != null) {
                Constructor mConstructor = clazz.getConstructor(String.class);
                if (mConstructor != null) {
                    mIHttpRequest = (IHttpRequest)mConstructor.newInstance(clazz, baseUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //注册广播回调
        NetworkStatesChangeManager mNetworkStatesChangeManager = new NetworkStatesChangeManager();
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        NetworkRequest request = builder.build();
        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager != null) {
            mConnectivityManager.registerNetworkCallback(request, mNetworkStatesChangeManager);
        }

        mResendManager.init(isAllowResend, mRsendNum);
    }

    public void mNetConnectStatusChanged(boolean status) {
        isConnect = status;
    }

    public boolean getNetConnectStatus() {
        return isConnect;
    }

    public void sendRequest(HttpCallBack mHttpCallBack) {
        if ("get".equals(mHttpCallBack.mResendData.getRequestType())) {
            get(mHttpCallBack);
        } else if ("post".equals(mHttpCallBack.mResendData.getRequestType())) {
            post(mHttpCallBack);
        } else if ("uploadFile".equals(mHttpCallBack.mResendData.getRequestType())) {
            uploadFile(mHttpCallBack);
        } else if ("downloadFile".equals(mHttpCallBack.mResendData.getRequestType())) {
            downloadFile(mHttpCallBack);
        }
    }

    //get
    public void get(HttpCallBack mHttpCallBack) {
        if (!isConnect) {
            addResendData(mHttpCallBack);
            return;
        }
        mIHttpRequest.get(mHttpCallBack);
    }

    //post
    public void post(HttpCallBack mHttpCallBack) {
        if (!isConnect) {
            addResendData(mHttpCallBack);
            return;
        }

        mIHttpRequest.post(mHttpCallBack);
    }

    //upload
    public void uploadFile(HttpCallBack mHttpCallBack) {
        if (!isConnect) {
            addResendData(mHttpCallBack);
            return;
        }

        mIHttpRequest.uploadFile(mHttpCallBack);
    }

    //download
    private void downloadFile(HttpCallBack mHttpCallBack) {
        if (!isConnect) {
            addResendData(mHttpCallBack);
            return;
        }

        mIHttpRequest.downloadFile(mHttpCallBack);
    }

    private void addResendData(HttpCallBack mHttpCallBack) {
        if (mResendManager.getAllowed()) {
            mResendManager.addResendData(mHttpCallBack);
        }
    }

}
