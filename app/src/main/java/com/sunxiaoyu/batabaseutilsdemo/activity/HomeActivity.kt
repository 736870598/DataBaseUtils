package com.sunxiaoyu.batabaseutilsdemo.activity

import android.Manifest
import android.os.Bundle
import com.sunxiaoyu.batabaseutilsdemo.R
import com.sunxiaoyu.batabaseutilsdemo.dao.VersionDao
import com.sunxiaoyu.batabaseutilsdemo.retrofitcore.RetrofitRequestManager
import com.sxy.kotlinutilsdemo.utils.PermissionsManager
import com.sxy.kotlinutilsdemo.utils.ToastUtils
import com.sxy.retrofitrxjavakotlindemo.resultmodel.VersionModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * Created by sunxiaoyu on 2017/7/18.
 */
class HomeActivity : BaseActivity(){

    override fun interceptOnCreate(savedInstanceState: Bundle?): Boolean {
        PermissionsManager.getManager().requestPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return super.interceptOnCreate(savedInstanceState)
    }

    override fun bindViewId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        request.onClick { getMovie() }
    }

    //进行网络请求
    private fun getMovie() {

        val disposable = RetrofitRequestManager.getManager().impl.getLastVersion("DIDIAO_APP")
                .subscribeOn(Schedulers.io())
                .map {
                    val dao = app.factory.getDataHelper(VersionDao::class.java, VersionModel::class.java)
                    dao.insert(it)
                    it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            resultTV.text = it.toString() },
                        {
                            ToastUtils.showToast(it.message)
                            resultTV.text = it.message } )

        managerDisposable(disposable)
    }

    override fun onBack() {
        finish()
    }

}