package com.zoom.netmiddleframework.net.retrofit;

import android.util.Log;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofitRequestManager<Service>{
    public static  final String TAG = "BaseRetrofitRequestManager";
    protected static Retrofit retrofit;
    protected Service mService;
    protected String baseUrl;

    public BaseRetrofitRequestManager(String baseUrl) {
        this.baseUrl = baseUrl;
        initRetrofit();
        mService = retrofit.create(getServiceClass());
    }

    private Class<Service> getServiceClass() {
        return (Class<Service>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private void initRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.w("LoggingInterceptor", message);

            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);//只抓取请求/响应行，防止内存溢出

        //拦截请求，为每个请求添加token
 /*       Interceptor mTokenIntercepter = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originaRequest = chain.request();
                Log.d(TAG,"originaRequest:" + originaRequest.toString());
                if (PreferencesManager.getSingleInstance().getData("token") == null/*|| alreadyHasAuthorizationHeader(originaRequest)*///) {
/*                    return chain.proceed(originaRequest);
                }

                String token = PreferencesManager.getSingleInstance().getData("token");
                Request request = originaRequest.newBuilder()
                        .header("token",token)
                        .build();
                return chain.proceed(request);
            }
        };*/

        //自动刷新token,错误代码为401时调用
       /* Authenticator mAuthenticator = ne Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                TokenService tokenService = retrofit.create(TokenService.class);
                String accessToken = "";
                if (mACacheUtil.getToken() != null) {
                    Call<Token> call = tokenService.refreshToken("refresh_token",mACacheUtil.getToken().getRefresh_token());
                    retrofit2.Response tokenReponse = call.execute();
                    Token token = (Token) tokenReponse.body();
                    if (token != null) {
                        mACacheUtil.saveToken(token);
                        accessToken = token.getAccess_token();
                    }
                }
                return response.request().newBuilder().addHeader("Authorization",accessToken).build();
            }
        };*/

        /*TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
                    throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
                    throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        } };*/

        SSLContext sc =null;

        try {
            sc = SSLContext.getInstance("TLS");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //配置client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                //.addNetworkInterceptor(mTokenIntercepter)
                //.authenticator(mAuthenticator)
                /*          .hostnameVerifier( new HostnameVerifier() {
                              @Override
                              public boolean verify(String s, SSLSession sslSession) {
                                  return true;
                              }
                          })*/
//                .sslSocketFactory( sc.getSocketFactory() )
                .protocols( Collections.singletonList( Protocol.HTTP_1_1) )
                .build();


        //配置retrofi

        if (baseUrl != null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    //.addConverterFactory(IGsonFactory.create())
                    .client(client)
                    .build();
        } else {

        }

    }
}
