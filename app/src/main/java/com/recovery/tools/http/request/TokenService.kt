package com.recovery.tools.http.request

import com.recovery.tools.bean.GetToken
import com.recovery.tools.bean.Token
import com.recovery.tools.http.response.Response
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {

    @POST("getQuestToken")
    fun getToken(@Body getToken: GetToken): Observable<Response<Token>>
}