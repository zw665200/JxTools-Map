package com.recovery.tools.http.loader

import com.recovery.tools.bean.OssParam
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object OssLoader : ObjectLoader() {

    fun getOssToken(token: String): Observable<Response<OssParam>> {
        return RetrofitServiceManager.getInstance().ossToken.getOssToken(token)
    }
}