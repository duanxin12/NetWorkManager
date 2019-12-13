package com.zoom.netmiddleframework.net.retrofit;

import android.text.TextUtils;
import android.util.Log;
import com.zoom.netmiddleframework.net.HttpCallBack;
import com.zoom.netmiddleframework.net.IHttpRequest;
import com.zoom.netmiddleframework.net.RequestData;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RetrofitRequestManager extends BaseRetrofitRequestManager<RetrofitRequestService> implements IHttpRequest {

    public RetrofitRequestManager(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public void get(HttpCallBack requestCallBack) {
        RequestData mRequestData = (RequestData)requestCallBack.mResendData;
        mService.request(mRequestData.getUrl(), (Map)mRequestData.getData());
    }

    @Override
    public void post(HttpCallBack requestCallBack) {
        RequestData mRequestData = (RequestData)requestCallBack.mResendData;

        if (TextUtils.isEmpty(mRequestData.getDeviceId()) || TextUtils.isEmpty(mRequestData.getUrl())) {
            Log.w("RetrofitRequestManager", "data or url is null!");
            return;
        }
        //按需要处理data
        RequestBody body = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), (String)mRequestData.getData());
        mService.request(mRequestData.getDeviceId(), mRequestData.getUrl(), body).enqueue(new RetrofitCallBack(requestCallBack));
    }

    @Override
    public void uploadFile(HttpCallBack requestCallBack) {
        RequestData mRequestData = (RequestData)requestCallBack.mResendData;

        RequestBody description = RequestBody.create(MediaType.parse(
                "text/plain; charset=utf-8"), (String) mRequestData.getData());
        String filePath = "";
        try {
            JSONObject jsonObject = new JSONObject((String) mRequestData.getData());
            filePath = jsonObject.getString("filePath");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        mService.uploadInfo(requestCallBack.mResendData.getUrl(), description, fileBody);
    }

    @Override
    public void downloadloadFile(HttpCallBack requestCallBack) {
        mService.downloadFile(requestCallBack.mResendData.getUrl());
    }
}
