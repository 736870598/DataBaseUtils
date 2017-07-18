package com.sunxiaoyu.batabaseutilsdemo.activity

import android.os.Bundle
import com.sunxiaoyu.batabaseutilsdemo.R
import com.sunxiaoyu.batabaseutilsdemo.application.App
import com.sunxiaoyu.batabaseutilsdemo.dao.VersionDao
import com.sunxiaoyu.batabaseutilsdemo.retrofitcore.RetrofitRequest
import com.sxy.databasecore.core.DaoManagerFactory
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
    override fun bindViewId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        request.onClick { getMovie() }
    }

    //进行网络请求
    private fun getMovie() {

        val disposable = RetrofitRequest.getRequest().requestImpl().getLastVersion("DIDIAO_APP")
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
                            resultTV.text = it.message } )

        managerDisposable(disposable)
    }

    override fun onBack() {
        finish()
    }

}