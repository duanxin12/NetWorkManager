package com.zoom.netmiddleframework.net;

/**
 * 网络请求回调接口
 */
public interface IHttpCallBack {
    void success(String callBack);
    void fail(Throwable throwable);
}
