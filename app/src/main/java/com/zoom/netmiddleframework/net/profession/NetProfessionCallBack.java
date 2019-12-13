package com.zoom.netmiddleframework.net.profession;

import com.zoom.netmiddleframework.net.HttpCallBack;
import com.zoom.netmiddleframework.net.RequestData;

public class NetProfessionCallBack extends HttpCallBack {

    public NetProfessionCallBack(RequestData mResendData) {
        super(mResendData);
    }

    @Override
    public void successProfessionExcute(String callBack) {
    }

    @Override
    public void failProfessionExcute(Throwable throwable) {

    }
}
