package com.sunxiaoyu.batabaseutilsdemo.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sunxiaoyu.batabaseutilsdemo.application.App
import com.sunxiaoyu.batabaseutilsdemo.manager.RxLifeManager
import com.sxy.kotlinutilsdemo.utils.ActLifeManager
import io.reactivex.disposables.Disposable

/**
 * Created by sunxiaoyu on 2017/7/18.
 */
abstract class BaseActivity : AppCompatActivity(){

    val app : App by lazy{ App.instance }
    var cxt : Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cxt = this

        if (interceptOnCreate(savedInstanceState)){
            finish()
            return
        }

        ActLifeManager.getManager().addActivity(this)

        val view = bindView()
        if (view != null){
            setContentView(view)
        }else{
            setContentView(bindViewId())
        }

        initView(savedInstanceState)
    }

    //-----------------拆解onctrate--------------------------------

    //是否拦截oncreate。返回true则直接finish
    open fun interceptOnCreate(savedInstanceState: Bundle?) : Boolean{
        return false
    }

    //设置视图id
    abstract fun bindViewId() : Int

    //设置视图view
    open fun bindView() : View? = null

    //初始化view
    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun onBack()

    override fun onBackPressed() { onBack() }

    fun managerDisposable(disposable : Disposable){
        RxLifeManager.getManager().addDisposable(cxt, disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActLifeManager.getManager().removeActivity(this)
        RxLifeManager.getManager().endAndRemoveDisposable(cxt)
    }
}