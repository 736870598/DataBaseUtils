package com.sxy.retrofitrxjavakotlindemo.resultmodel

import com.sxy.databasecore.annotation.SxyDBField
import com.sxy.databasecore.annotation.SxyDBTable
import java.io.Serializable

/**
 * Created by sunxiaoyu on 2017/7/18.
 */
@SxyDBTable("versionInfo")
data class VersionModel(
        @SxyDBField("versionName") var versionName: String? = null,
        @SxyDBField("versionCode") var versionCode : String? = null,
        @SxyDBField("appUrl") var appUrl : String? =null,
        @SxyDBField("versionInfo") var versionInfo : String? = null,
        @SxyDBField("temp") var temp : Long? = null
) : Serializable