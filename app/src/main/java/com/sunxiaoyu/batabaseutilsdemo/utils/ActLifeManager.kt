package com.sxy.kotlinutilsdemo.utils

import android.app.Activity

/**
 * activity 管理器
 * Created by sunxiaoyu on 2017/6/27.
 */
class ActLifeManager private constructor(){

    private object ActivityManagerHolder{
        val instance = ActLifeManager()
    }

    companion object{
        fun getManager() = ActivityManagerHolder.instance
    }

    private val activityList : MutableList<Activity> by lazy { mutableListOf<Activity>() }

    fun addActivity(activity : Activity){
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity){
        if (activityList.contains(activity))
            activityList.remove(activity)
    }


    fun clearActivity(){
        activityList.forEach {
            if (!it.isFinishing)
                it.finish()
        }
        activityList.clear()
    }


}

