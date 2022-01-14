package com.recovery.tools.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.mmkv.MMKV
import com.recovery.tools.bean.*
import com.recovery.tools.callback.PayCallback
import com.recovery.tools.http.loader.*
import com.recovery.tools.http.response.ResponseTransformer
import com.recovery.tools.http.schedulers.SchedulerProvider
import com.recovery.tools.utils.JLog
import com.recovery.tools.utils.ToastUtil
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class PayManager private constructor() : CoroutineScope by MainScope() {

    companion object {

        @Volatile
        private var instance: PayManager? = null

        fun getInstance(): PayManager {
            if (instance == null) {
                synchronized(PayManager::class) {
                    if (instance == null) {
                        instance = PayManager()
                    }
                }
            }

            return instance!!
        }
    }

    fun checkBillPay(context: Context, result: (Boolean) -> Unit) {
        val service = MMKV.defaultMMKV()?.decodeParcelable(Constant.BILL + Constant.EXPIRE_TYPE_FOREVER, Price::class.java)
        if (service != null) {
            getSinglePayStatus(context, service.id) {
                when (it.serverExpire) {
                    0 -> {
                        result(true)
                    }

                    else -> {
                        result(false)
                    }
                }
            }
        }
    }

    fun checkDeletePay(context: Context, result: (Boolean) -> Unit) {
        val service = MMKV.defaultMMKV()?.decodeParcelable(Constant.DELETE + Constant.EXPIRE_TYPE_FOREVER, Price::class.java)
        if (service != null) {
            JLog.i("id = ${service.id}")
            getSinglePayStatus(context, service.id) {
                when (it.serverExpire) {
                    0 -> {
                        result(true)
                    }

                    else -> {
                        result(false)
                    }
                }
            }
        }
    }

    /**
     * 检查恢复套餐
     * @param context
     * @param result
     */
    fun checkFixPay(context: Context, result: (Boolean) -> Unit) {
        val mmkv = MMKV.defaultMMKV()
        when (mmkv?.decodeInt("fix")) {
            110 -> result(true)

            else -> {
                getPayStatus(context, Constant.PHOTO_FIX) {
                    when (it.serverExpire) {
                        0 -> {
                            val pack = it.packDetail
                            if (pack.isEmpty()) {
                                result(true)
                                mmkv?.encode("fix", 110)
                                return@getPayStatus
                            }

                            if (pack.size == 3) {
                                mmkv?.encode("fix", 0)
                                result(false)
                            }
                        }

                        else -> {
                            result(false)
                            mmkv?.encode("fix", 0)
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查恢复套餐
     * @param context
     * @param serviceId
     * @param result
     */
    fun checkRecoveryPay(context: Context, serviceId: Int, result: (Boolean) -> Unit) {
        val mmkv = MMKV.defaultMMKV()
        when (mmkv?.decodeInt("recovery")) {
            110 -> result(true)

            111 -> {
                if (serviceId == 1 || serviceId == 2) {
                    result(false)
                } else {
                    result(true)
                }
            }

            else -> {
                getPayStatus(context, Constant.COM) {
                    when (it.serverExpire) {
                        0 -> {
                            val pack = it.packDetail
                            if (pack.isEmpty()) {
                                result(true)
                                mmkv?.encode("recovery", 110)
                                return@getPayStatus
                            }

                            if (pack.size == 1) {
                                mmkv?.encode("recovery", 111)
                                if (serviceId == 1 || serviceId == 2) {
                                    result(false)
                                } else {
                                    result(true)
                                }
                                return@getPayStatus
                            }

                            if (pack.size == 2) {
                                if (pack[0].server_code == Constant.REPL) {
                                    mmkv?.encode("recovery", 111)
                                    if (serviceId == 1 || serviceId == 2) {
                                        result(false)
                                    } else {
                                        result(true)
                                    }
                                    return@getPayStatus
                                } else {
                                    mmkv?.encode("recovery", 0)
                                    result(false)
                                }
                            }
                        }

                        else -> {
                            result(false)
                            mmkv?.encode("recovery", 0)
                        }
                    }
                }
            }
        }
    }

    fun getPayStatus(context: Context, serviceCode: String, success: (PayStatus) -> Unit) {
        val service = MMKV.defaultMMKV()?.decodeParcelable(serviceCode + Constant.EXPIRE_TYPE_FOREVER, Price::class.java)
        if (service != null) {
            thread {
                PayStatusLoader.getPayStatus(service.id, Constant.CLIENT_TOKEN)
                    .compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe({
                        if (it.isEmpty()) return@subscribe
                        success(it[0])
                    }, {
                        ToastUtil.showShort(context, "获取支付状态失败")
                    })
            }
        } else {
            ToastUtil.show(context, "获取套餐列表失败")
        }

    }

    fun getSinglePayStatus(context: Context, serviceId: Int, payStatus: (PayStatus) -> Unit) {
        thread {
            SinglePayStatusLoader.getPayStatus(serviceId, Constant.CLIENT_TOKEN)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe({
                    if (it.isEmpty()) return@subscribe
                    payStatus(it[0])
                }, {
                    ToastUtil.showShort(context, "获取支付状态失败")
                })
        }
    }


    /**
     * 支付宝支付
     */
    fun doAliPay(activity: Activity, serviceId: Int, callback: PayCallback) {
        thread {
            AliPayLoader.getOrderParam(serviceId)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe({
                    checkOrderStatus(activity, it, callback)
                }, {
                    ToastUtil.show(activity, "发起支付请求失败")
                })
        }
    }

    /**
     * 快付
     */
    fun doFastPay(activity: Activity, serviceId: Int, callback: PayCallback) {
        thread {
            FastPayParamLoader.getOrderParam(serviceId)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe({
                    checkFastPay(activity, it, callback)
                }, {
                    ToastUtil.show(activity, "发起支付请求失败")
                })
        }
    }

    /**
     * 微信支付
     */
    fun doWechatPay(activity: Activity, serviceId: Int, callback: PayCallback) {
        thread {
            WechatPayLoader.getOrderParam(serviceId)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe({
                    checkWechatPay(activity, it, callback)
                }, {
                    ToastUtil.show(activity, "发起支付请求失败")
                })
        }
    }

    private fun checkOrderStatus(activity: Activity, order: AlipayParam, callback: PayCallback) {
        launch(Dispatchers.IO) {
            JLog.i("param = ${order.body}")
            JLog.i("orderSn = ${order.orderSn}")

            val task = PayTask(activity)
            val result = task.payV2(order.body, true)
            val res = PayResult(result)
            val resultStatus = res.resultStatus

            if (resultStatus == "9000") {
                JLog.i("alipay success")

                callback.progress(order.orderSn)
                callback.success()

                OrderDetailLoader.getOrderStatus(order.orderSn)
                    .compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe({
                        if (it.order_sn != order.orderSn) {
                            return@subscribe
                        }

                        when (it.status) {
//                            "1" -> callback.success()
//                            "0" -> callback.failed("未支付")
//                            "2" -> callback.failed("退款中")
//                            "3" -> callback.failed("已退款")
//                            "4" -> callback.failed("已取消")
                        }
                    }, {
                        JLog.i("${it.message}")
                    })

            } else {
                //支付失败，也需要发起服务端校验
                JLog.i("alipay failed")

                callback.failed("已取消")

//                launch(Dispatchers.IO) {
//                    OrderCancelLoader.orderCancel(order.orderSn)
//                        .compose(ResponseTransformer.handleResult())
//                        .compose(SchedulerProvider.getInstance().applySchedulers())
//                        .subscribe({}, {
//                            JLog.i("${it.message}")
//                        })
//                }
            }
        }

    }

    private fun checkFastPay(activity: Activity, order: FastPayParam, callback: PayCallback) {
        callback.progress(order.orderSn)

        val page = order.body
//        JLog.i("cd = ${page.orderCd}")
//        JLog.i("sign = ${page.sign}")


        //alipay
//        val intent = Intent()
//        intent.setClass(activity, FastPayActivity::class.java)
//        intent.putExtra("title","支付")
//        intent.putExtra("page", page)
//        activity.startActivity(intent)

        val url = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=$page"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(intent)

        //wechat pay
//        val sb = StringBuffer()
//        sb.append("orderCd=")
//        sb.append(page.orderCd)
//        sb.append("&")
//        sb.append("sign=")
//        sb.append(page.sign)
//        sb.append("&")
//        val api = WXAPIFactory.createWXAPI(activity, Constant.TENCENT_APP_ID)
//        val req = WXLaunchMiniProgram.Req()
//        req.userName = Constant.TENCENT_MINI_PROGRAM_APP_ID
//        req.path = "pages/index/index?$sb"
//        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE
//        api.sendReq(req)

    }

    private fun checkWechatPay(activity: Activity, order: WechatPayParam, callback: PayCallback) {
        callback.progress(order.orderSn)

        JLog.i("发起支付")
        //wechat pay
        val api = WXAPIFactory.createWXAPI(activity, Constant.TENCENT_APP_ID, false)
        api.registerApp(Constant.TENCENT_APP_ID)

        val request = PayReq()
        request.appId = Constant.TENCENT_APP_ID
        request.partnerId = Constant.TENCENT_PARTNER_ID
        request.prepayId = order.body
        request.packageValue = "Sign=WXPay"
        request.nonceStr = order.noncestr
        request.timeStamp = order.timestamp.toString()
        request.sign = order.sign
        api.sendReq(request)
    }

}