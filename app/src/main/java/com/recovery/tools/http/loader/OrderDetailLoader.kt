package com.recovery.tools.http.loader

import com.recovery.tools.bean.OrderDetail
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object OrderDetailLoader {

    fun getOrderStatus(orderSn: String): Observable<Response<OrderDetail>> {
        return RetrofitServiceManager.getInstance().orderDetail.getOrderDetail(orderSn, Constant.CLIENT_TOKEN)
    }
}