package com.sunxiaoyu.batabaseutilsdemo.request;

import com.sxy.retrofitrxjavakotlindemo.resultmodel.VersionModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Created by sunxiaoyu on 2017/7/18.
 */
public interface RequestImpl {
    @GET("latest")
    Observable<VersionModel> getLastVersion(@Query("appCode") String CMF_TY_APP);
}
