package com.zoom.netmiddleframework.net;

public class RequestData<T> {
    private String requestType;
    private String requestId;
    private String url;
    private String deviceId;
    private long resendNum;
    private T data;

    public RequestData(String requestType, String requestId, String url, String deviceId, T data) {
        this.requestType = requestType;
        this.requestId = requestId;
        this.url = url;
        this.deviceId = deviceId;
        this.data = data;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setResendNum(long resendNum) {
        this.resendNum = resendNum;
    }

    public long getResendNum() {
        return resendNum;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
