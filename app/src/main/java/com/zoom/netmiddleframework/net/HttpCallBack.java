package com.zoom.netmiddleframework.net;

public abstract class HttpCallBack implements IHttpCallBack {

    public RequestData mResendData;

    public HttpCallBack(RequestData mResendData) {
        this.mResendData = mResendData;
    }

    @Override
    public void success(String callBack) {
        successProfessionExcute(callBack);
    }

    @Override
    public void fail(Throwable throwable) {
        failProfessionExcute(throwable);
        addResendData(this);
    }

    public abstract void successProfessionExcute(String callBack);
    public abstract void failProfessionExcute(Throwable throwable);

    private  void addResendData(HttpCallBack mResendData) {
        ResendManager.getInstance().addResendData(mResendData);
    }
}
