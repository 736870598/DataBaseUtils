package com.sunxiaoyu.batabaseutilsdemo.retrofitcore;


import com.sunxiaoyu.batabaseutilsdemo.request.RequestImpl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 *
 * Created by sunxiaoyu on 2017/7/18.
 */

public class RetrofitRequestManager {

    public static final String BASE_URL = "https://vers.cmfun.cn/OriAppVersion/version/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private RequestImpl requestImpl;


    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final RetrofitRequestManager INSTANCE = new RetrofitRequestManager();
    }

    //获取单例
    public static RetrofitRequestManager getManager(){
        return SingletonHolder.INSTANCE;
    }

    private RetrofitRequestManager(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        requestImpl = create(RequestImpl.class);
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    public RequestImpl getImpl() {
        return requestImpl;
    }
}
