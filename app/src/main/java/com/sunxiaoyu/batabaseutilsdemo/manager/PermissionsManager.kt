package com.sxy.kotlinutilsdemo.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.content.ContextCompat
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * 权限申请utils，主要使用RxPermissions
 * 详情： https://github.com/tbruyelle/RxPermissions
 * Created by sunxiaoyu on 2017/6/29.
 */
class PermissionsManager private constructor(){

    private object PermissionsUtilsHolder{
        val instance = PermissionsManager()
    }

    companion object{
        fun getManager() = PermissionsUtilsHolder.instance
    }

    /**
     * 请求权限
     */
    fun requestPermissions(activity: Activity, vararg permissions: String){
        if (permissions.any { lacksPermission(activity, it) }){
            //如果要申请的权限中有任何一项没有申请则去申请
            RxPermissions(activity).requestEach(*permissions).subscribe { dealPermission(activity, it) }
        }
    }

    /**
     *  是否缺少该权限
     */
    fun lacksPermission(activity: Activity, permission: String) =
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED

    /**
     * 处理权限受理结果
     */
    private fun dealPermission(activity: Activity, permission: Permission) {
        with(permission){
            if (!granted){  //不同意授权
                if (shouldShowRequestPermissionRationale){
                    //可以询问
                    requestPermissions(activity, name)
                }else{
                    //不让询问
                    showDialog2Toast(activity)
                }
            }
        }
    }

    /**
     * dialog提示去打开权限
     */
    private fun showDialog2Toast(activity: Activity){
        AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage("缺少使用该功能的必要权限，请前往开启")
                .setPositiveButton("前往开启"){
                    dialog, _ ->
                    startAppSettings(activity)
                    dialog.dismiss()
                }.create().show()
    }


    /**
     * 跳转到该app设置界面
     */
    private fun  startAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(intent)
    }
}
