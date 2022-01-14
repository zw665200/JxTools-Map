package com.recovery.tools.http.loader

import com.recovery.tools.bean.UserInfo
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object UserInfoLoader : ObjectLoader() {

    fun getUser(token: String): Observable<Response<List<UserInfo>>> {
        return RetrofitServiceManager.getInstance().userInfo.getUser(token)
    }
}