package com.sxy.kotlinutilsdemo.utils

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.support.annotation.UiThread
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.sunxiaoyu.batabaseutilsdemo.R
import com.sunxiaoyu.batabaseutilsdemo.application.App
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.padding
import org.jetbrains.anko.textColor

/**
 * ToastUtils
 * Created by sunxiaoyu on 2017/6/30.
 */
object ToastUtils{

    private var toast : Toast? = null
    private var view : TextView? = null

    @UiThread
    fun showToast(content : String?, duration : Int = Toast.LENGTH_SHORT){

        if (content == null){
            return
        }

        if (view == null){
            view = TextView(App.instance).apply {
                textColor = Color.WHITE
                gravity = Gravity.CENTER
                backgroundResource = R.drawable.black_circular_view
                padding = textSize.toInt()
            }
        }


        if (toast == null){
            toast = Toast(App.instance)
        }

        view?.text = content
        toast?.view = view
        toast?.duration = duration
        toast?.show()

    }

}