package com.recovery.tools.http.loader

import com.recovery.tools.bean.CheckPayParam
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object CheckPayLoader {

    fun checkPay(): Observable<Response<CheckPayParam>> {
        return RetrofitServiceManager.getInstance().checkPayService().checkPay(Constant.PRODUCT_ID, Constant.CLIENT_TOKEN)
    }
}