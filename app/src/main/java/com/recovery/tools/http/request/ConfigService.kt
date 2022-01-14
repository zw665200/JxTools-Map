package com.recovery.tools.http.request

import com.recovery.tools.bean.Config
import com.recovery.tools.controller.Constant
import com.recovery.tools.http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface ConfigService {

    @GET("siteInfo")
    fun getConfig(@Query("serverCode") serviceCode: String): Observable<Response<Config>>
}