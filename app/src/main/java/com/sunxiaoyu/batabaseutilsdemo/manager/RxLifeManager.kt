package com.sunxiaoyu.batabaseutilsdemo.manager

import android.content.Context
import com.sxy.kotlinutilsdemo.utils.ActLifeManager

import java.util.ArrayList
import java.util.Collections
import java.util.HashMap

import io.reactivex.disposables.Disposable

/**

 * Created by sunxiaoyu on 2017/7/18.
 */
class RxLifeManager private constructor() {

    private val disHashMap: MutableMap<Context, MutableList<Disposable>>
            by lazy { mutableMapOf<Context, MutableList<Disposable>>() }

    private object RxLifeManagerHolder {
        val instance = RxLifeManager()
    }

    companion object{
        fun getManager() = RxLifeManagerHolder.instance
    }

    /**
     * 添加disposable用于统一管理
     */
    fun addDisposable(context: Context?, disposable: Disposable?) {

        if (context == null || disposable == null) {
            return
        }

        var list: MutableList<Disposable>? = disHashMap[context]
        if (list == null) {
            list = Collections.synchronizedList(mutableListOf<Disposable>())
        }
        list!!.add(disposable)

        disHashMap.put(context, list)
    }

    /**
     * 移除并结束disposable
     */
    fun endAndRemoveDisposable(context: Context?) {

        if (context == null) {
            return
        }

        disHashMap[context]?.forEach {
            if( !it.isDisposed )
                it.dispose()
        }

        disHashMap.remove(context)

    }

}
