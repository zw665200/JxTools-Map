package com.recovery.tools.http.loader

import com.recovery.tools.bean.Order
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object OrderLoader {

    fun getOrders(): Observable<Response<List<Order>>> {
        return RetrofitServiceManager.getInstance().orders.getOrders(Constant.CLIENT_TOKEN)
    }
}