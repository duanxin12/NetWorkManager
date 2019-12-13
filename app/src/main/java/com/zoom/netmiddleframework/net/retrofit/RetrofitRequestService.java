package com.zoom.netmiddleframework.net.retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RetrofitRequestService {
    @GET()
    Call<ResponseBody> request(@Url String url, @QueryMap Map<String, String> map);
    @POST()
    Call<ResponseBody> request(@Header("data") String header, @Url String url, @Body RequestBody body);

    @Multipart
    @POST()
    Call<ResponseBody> uploadInfo(@Url String url, @Part("description") RequestBody description, @Part MultipartBody.Part file);

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String path);
}
