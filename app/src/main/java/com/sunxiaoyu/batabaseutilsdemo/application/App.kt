package com.sunxiaoyu.batabaseutilsdemo.application

import android.app.Application
import android.os.Environment
import com.sxy.databasecore.core.DaoManagerFactory
import com.sxy.kotlinutilsdemo.utils.extensions.DelegatesExt

/**
 * Created by sunxiaoyu on 2017/7/18.
 */
class App : Application(){

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()

        val DB_PATH = Environment.getExternalStorageDirectory().absolutePath + "/SxyDB"
        val DB_NAME = "sxy.db"
    }

    val factory : DaoManagerFactory  by lazy {
        DaoManagerFactory.getFactory(DB_PATH, DB_NAME) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}