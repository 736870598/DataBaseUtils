package com.sxy.kotlinutilsdemo.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * 网络相关信息
 * Created by sunxiaoyu on 2017/6/28.
 */
object NetWorkUtils {

    /**
     * 此类用于得到当前网络连接状态
     * @return   0 无网络连接   1 流量连接  2 wifi连接  3 未知(有网能用，不是流量)
     */
    fun getNetType(context: Context) : Int{
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = connectManager.activeNetworkInfo

        if (mNetworkInfo == null || !mNetworkInfo.isAvailable){
            return 0
        }

        when(mNetworkInfo.type){
            ConnectivityManager.TYPE_MOBILE -> return 1
            ConnectivityManager.TYPE_WIFI -> return 2
            else -> return 3
        }
    }

    /**
     * 判断当前网络是否连接正常
     */
    fun isnetWorkAvailable(context: Context) : Boolean{
        return getNetType(context) != 0
    }

    /**
     *  判断当前网络是否连接wifi
     */
    fun isConnWifi(context: Context) : Boolean{
        return getNetType(context) == 2
    }


}