package com.zoom.netmiddleframework.net;

/**
 * 网络请求管理接口
 */
public interface IHttpRequest {
    void get(HttpCallBack requestCallBack);
    void post(HttpCallBack requestCallBack);
    void uploadFile(HttpCallBack requestCallBack);
    void downloadFile(HttpCallBack requestCallBack);
}
