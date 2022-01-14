package com.recovery.tools.http.loader

import com.recovery.tools.bean.Config
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object ConfigLoader {

    fun getConfig(): Observable<Response<Config>> {
        return RetrofitServiceManager.getInstance().config.getConfig(Constant.CHANNEL_ID)
    }
}