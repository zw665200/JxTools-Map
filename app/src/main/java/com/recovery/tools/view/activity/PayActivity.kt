package com.recovery.tools.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baidu.mobads.action.ActionParam
import com.baidu.mobads.action.ActionType
import com.baidu.mobads.action.BaiduAction
import com.bumptech.glide.Glide
import com.tencent.mmkv.MMKV
import com.recovery.tools.R
import com.recovery.tools.adapter.DataAdapter
import com.recovery.tools.bean.FileBean
import com.recovery.tools.bean.Resource
import com.recovery.tools.callback.DialogCallback
import com.recovery.tools.callback.PayCallback
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.PayManager
import com.recovery.tools.controller.WxManager
import com.recovery.tools.http.loader.OrderDetailLoader
import com.recovery.tools.http.response.ResponseTransformer
import com.recovery.tools.http.schedulers.SchedulerProvider
import com.recovery.tools.utils.AppUtil
import com.recovery.tools.utils.JLog
import com.recovery.tools.utils.RomUtil
import com.recovery.tools.utils.ToastUtil
import com.recovery.tools.view.base.BaseActivity
import com.recovery.tools.view.views.AutoTextView
import com.recovery.tools.view.views.PaySuccessDialog
import com.recovery.tools.view.views.QuitDialog
import kotlinx.android.synthetic.main.heart_small.view.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*


class PayActivity : BaseActivity() {
    private lateinit var back: ImageView
    private lateinit var pay: Button
    private lateinit var userAgreement: AppCompatCheckBox
    private lateinit var wechatPay: AppCompatCheckBox
    private lateinit var aliPay: AppCompatCheckBox
    private lateinit var titleName: TextView

    private lateinit var firstLayout: FrameLayout
    private lateinit var secondLayout: FrameLayout
    private lateinit var thirdLayout: FrameLayout

    private lateinit var firstPriceView: TextView
    private lateinit var firstOriginPriceView: TextView
    private lateinit var secondPriceView: TextView
    private lateinit var secondOriginPriceView: TextView
    private lateinit var thirdPriceView: TextView
    private lateinit var thirdOriginPriceView: TextView

    private lateinit var discount: TextView
    private lateinit var discountWx: TextView
    private lateinit var introduce2: TextView
    private lateinit var introduce3: TextView
    private lateinit var menuSign: ImageView
    private lateinit var menuBox: RecyclerView

    private lateinit var forever: ImageView
    private lateinit var year: ImageView
    private lateinit var month: ImageView

    private var serviceId: Int = 0
    private var currentServiceId = 0
    private var firstServiceId = 0
    private var secondServiceId = 0
    private var thirdServiceId = 0

    private var title: String? = null
    private var lastClickTime: Long = 0L

    private var mPrice = 0f
    private var firstPrice = 0f
    private var firstOriginalPrice = 0f
    private var secondPrice = 0f
    private var secondOriginPrice = 0f
    private var thirdPrice = 0f
    private var thirdOriginPrice = 0f

    private lateinit var firstDailyPrice: TextView
    private lateinit var secondDailyPrice: TextView
    private lateinit var thirdDailyPrice: TextView

    private lateinit var counter: TextView
    private lateinit var counterTimer: CountDownTimer
    private lateinit var timer: CountDownTimer
    private lateinit var customerAgreement: TextView
    private lateinit var notice: AutoTextView

    private var remindTime = 15 * 60 * 1000L
    private var kv: MMKV? = MMKV.defaultMMKV()
    private var orderSn = ""
    private var startPay = false

    override fun setLayout(): Int {
        return R.layout.a_recovery_pay
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        back = findViewById(R.id.iv_back)
        pay = findViewById(R.id.do_pay)
        wechatPay = findViewById(R.id.do_wechat_pay)
        aliPay = findViewById(R.id.do_alipay_pay)
        titleName = findViewById(R.id.pay_content)
        firstPriceView = findViewById(R.id.price)
        firstOriginPriceView = findViewById(R.id.original_price)
        secondPriceView = findViewById(R.id.price2)
        secondOriginPriceView = findViewById(R.id.original_price2)
        thirdPriceView = findViewById(R.id.price3)
        thirdOriginPriceView = findViewById(R.id.original_price3)
        introduce2 = findViewById(R.id.introduce2)
        introduce3 = findViewById(R.id.introduce3)
        counter = findViewById(R.id.counter)
        notice = findViewById(R.id.tv_notice)
        customerAgreement = findViewById(R.id.customer_agreement)
        userAgreement = findViewById(R.id.user_agreement)
        discount = findViewById(R.id.discount)
        discountWx = findViewById(R.id.discount_wx)
        menuSign = findViewById(R.id.menu_sign)
        menuBox = findViewById(R.id.menu_box)
        firstLayout = findViewById(R.id.ll_1)
        secondLayout = findViewById(R.id.ll_2)
        thirdLayout = findViewById(R.id.ll_3)

        //原价删除线
        firstOriginPriceView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        secondOriginPriceView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        thirdOriginPriceView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

        back.setOnClickListener { onBackPressed() }
        pay.setOnClickListener { checkPay(this) }
        firstLayout.setOnClickListener { chooseMenu(1) }
        secondLayout.setOnClickListener { chooseMenu(2) }
        thirdLayout.setOnClickListener { chooseMenu(3) }
        customerAgreement.setOnClickListener { toAgreementPage() }
        menuBox.setOnTouchListener { _, _ ->
            firstLayout.performClick()
            false
        }

        //核算每日价格
        firstDailyPrice = findViewById(R.id.price_per_day)
        secondDailyPrice = findViewById(R.id.price_per_day2)
        thirdDailyPrice = findViewById(R.id.price_per_day3)

        forever = findViewById(R.id.forever)
        year = findViewById(R.id.year)
        month = findViewById(R.id.month)

        //选择微信支付
        wechatPay.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                aliPay.isChecked = false
            }
        }

        //选择支付宝支付
        aliPay.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                wechatPay.isChecked = false
            }
        }


        chooseMenu(1)
        kv = MMKV.defaultMMKV()

        initNotice()
        initCounter()

    }

    override fun onResume() {
        super.onResume()
        if (startPay) {
            checkPayResult()
        }
    }

    override fun initData() {
        serviceId = intent.getIntExtra("serviceId", 0)
        title = intent.getStringExtra("title")
        if (title != null) {
            titleName.text = title
        }

        getServicePrice()
        loadMenuBox()
    }

    private fun initNotice() {
        timer = object : CountDownTimer(4000 * 1000L, 4000) {
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                val str = WxManager.getInstance(this@PayActivity).getRecoveryUser()
                notice.setText(str, Color.GRAY)
            }
        }

        timer.start()
    }

    private fun initCounter() {
        val result = kv?.decodeLong("pay_counter")
        remindTime = if (result == 0L) 15 * 60 * 1000L else result!!

        counterTimer = object : CountDownTimer(remindTime, 100 / 6L) {
            override fun onFinish() {
                val text = AppUtil.timeStamp2Date("0", "mm:ss:SS")
                counter.text = text
                kv?.encode("pay_counter", 15 * 60 * 1000L)
            }

            override fun onTick(millisUntilFinished: Long) {
                val text = AppUtil.timeStamp2Date(millisUntilFinished.toString(), "mm:ss:SS")
                counter.text = text
                remindTime = millisUntilFinished
            }
        }
    }


    private fun loadMenuBox() {
        val list = arrayListOf<Resource>()

        when (serviceId) {
            7 -> {
                list.add(Resource("1", R.drawable.vip_pay_laojiuzhaopianfanxin, "照片翻新"))
                list.add(Resource("2", R.drawable.vip_pay_mohuzhaopianxiufu, "照片修复"))
                list.add(Resource("3", R.drawable.vip_pay_heibaizhaopianshangse, "照片上色"))
                list.add(Resource("4", R.drawable.vip_pay_diqingtuxiangzengqiang, "照片高清"))
                list.add(Resource("5", R.drawable.vip_pay_wangtuwusunfangda, "无损放大"))
                list.add(Resource("6", R.drawable.vip_pay_huiyuanzhuanshukefu, "专属客服"))

                year.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_pic_year, null))
                month.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_pic_month, null))
            }

            else -> {
                list.add(Resource("chat", R.drawable.ico_faction05, "聊天恢复"))
                list.add(Resource("friend", R.drawable.ico_faction06, "好友恢复"))
                list.add(Resource("pic", R.drawable.ico_faction01, "图片恢复"))
                list.add(Resource("video", R.drawable.ico_faction02, "视频恢复"))
                list.add(Resource("audio", R.drawable.ico_faction03, "语音恢复"))
                list.add(Resource("doc", R.drawable.ico_faction04, "文档恢复"))

                year.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_pay_02, null))
                month.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_pay_02, null))

                secondDailyPrice.visibility = View.GONE
                thirdDailyPrice.visibility = View.GONE
            }
        }


        val mAdapter = DataAdapter.Builder<Resource>()
            .setData(list)
            .setLayoutId(R.layout.heart_small)
            .addBindView { itemView, itemData ->
                Glide.with(this).load(itemData.icon).into(itemView.iv_icon)
                itemView.tv_name.text = itemData.name
            }
            .create()

        menuBox.layoutManager = GridLayoutManager(this, 4)
        menuBox.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }


    private fun chooseMenu(index: Int) {
        when (index) {
            1 -> {
                firstLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.background_gradient_stroke, null)
                secondLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.pay_background_nomal, null)
                thirdLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.pay_background_nomal, null)
                currentServiceId = firstServiceId
            }

            2 -> {
                firstLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.pay_background_nomal, null)
                secondLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.background_gradient_stroke, null)
                thirdLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.pay_background_nomal, null)
                currentServiceId = secondServiceId
            }

            3 -> {
                firstLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.pay_background_nomal, null)
                secondLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.pay_background_nomal, null)
                thirdLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.background_gradient_stroke, null)
                currentServiceId = thirdServiceId
            }
        }

    }


    private fun toAgreementPage() {
        val intent = Intent()
        intent.setClass(this, AgreementActivity::class.java)
        startActivity(intent)
    }

    private fun getServicePrice() {
        var type = Constant.COM
        when (serviceId) {
            7 -> type = Constant.PHOTO_FIX
        }

        PayManager.getInstance().getPayStatus(this, type) {
            discount.text = "支付立减${it.discountFee}"
            discountWx.text = "支付立减${it.discountFee}"

            val packDetails = it.packDetail

            //单项补价套餐
            if (packDetails.size == 1) {
                firstServiceId = packDetails[0].id
                firstPrice = packDetails[0].sale_price.toFloat()
                firstOriginalPrice = packDetails[0].server_price.toFloat()
                secondLayout.visibility = View.GONE
                thirdLayout.visibility = View.GONE
                currentServiceId = firstServiceId
            }

            //多项套餐或者补价套餐
            if (packDetails.size == 2) {
                for (child in packDetails) {
                    if (child.server_code == Constant.REC) {
                        firstServiceId = child.id
                        firstPrice = child.sale_price.toFloat()
                        firstOriginalPrice = child.server_price.toFloat()
                        currentServiceId = firstServiceId
                    }

                    if (child.server_code == Constant.COM) {
                        secondServiceId = child.id
                        secondPrice = child.sale_price.toFloat()
                        secondOriginPrice = child.server_price.toFloat()
                        introduce2.text = child.desc
                    }

                    thirdLayout.visibility = View.GONE
                }
            }

            //多项套餐或者补价套餐
            if (packDetails.size == 3) {
                for (child in packDetails) {
                    if (child.expire_type == "2") {
                        firstServiceId = child.id
                        firstPrice = child.sale_price.toFloat()
                        firstOriginalPrice = child.server_price.toFloat()
                        currentServiceId = firstServiceId
                    }

                    if (child.expire_type == "1") {
                        secondServiceId = child.id
                        secondPrice = child.sale_price.toFloat()
                        secondOriginPrice = child.server_price.toFloat()
                        introduce2.text = child.desc
                    }

                    if (child.expire_type == "3") {
                        thirdServiceId = child.id
                        thirdPrice = child.sale_price.toFloat()
                        thirdOriginPrice = child.server_price.toFloat()
                        introduce3.text = child.desc
                    }
                }
            }

            //刷新价格
            changeDescription(packDetails.size)
        }
    }


    private fun changeDescription(index: Int) {
        pay.visibility = View.VISIBLE
        when (index) {

            1 -> {
                firstPriceView.text = String.format("%.0f", firstPrice)
                firstOriginPriceView.text = String.format("%.0f", firstOriginalPrice)
                counterTimer.start()

                firstDailyPrice.text = String.format("%.2f", firstPrice / 730) + "/天"
            }

            2 -> {
                firstPriceView.text = String.format("%.0f", firstPrice)
                firstOriginPriceView.text = String.format("%.0f", firstOriginalPrice)
                secondPriceView.text = String.format("%.0f", secondPrice)
                secondOriginPriceView.text = String.format("%.0f", secondOriginPrice)

                firstDailyPrice.text = String.format("%.2f", firstPrice / 730) + "/天"
                secondDailyPrice.text = String.format("%.2f", secondPrice / 365) + "/天"

                counterTimer.start()
            }

            3 -> {
                firstPriceView.text = String.format("%.0f", firstPrice)
                firstOriginPriceView.text = String.format("%.0f", firstOriginalPrice)
                secondPriceView.text = String.format("%.0f", secondPrice)
                secondOriginPriceView.text = String.format("%.0f", secondOriginPrice)
                thirdPriceView.text = String.format("%.0f", thirdPrice)
                thirdOriginPriceView.text = String.format("%.0f", thirdOriginPrice)

                firstDailyPrice.text = String.format("%.2f", firstPrice / 730) + "/天"
                secondDailyPrice.text = String.format("%.2f", secondPrice / 365) + "/天"
                thirdDailyPrice.text = String.format("%.2f", thirdPrice / 30) + "/天"

                counterTimer.start()
            }

            else -> {
                firstLayout.visibility = View.GONE
                secondLayout.visibility = View.GONE
                thirdLayout.visibility = View.GONE
            }
        }
    }


    private fun checkPay(c: Activity) {
        if (!userAgreement.isChecked) {
            ToastUtil.show(this, "请阅读并勾选《会员须知》")
            return
        }

        if (!wechatPay.isChecked && !aliPay.isChecked) {
            ToastUtil.show(this, "请选择付款方式")
            return
        }

        if (lastClickTime == 0L) {
            lastClickTime = System.currentTimeMillis()
        } else if (System.currentTimeMillis() - lastClickTime < 2 * 1000) {
            ToastUtil.showShort(c, "请不要频繁发起支付")
            return
        }

        lastClickTime = System.currentTimeMillis()


        if (wechatPay.isChecked) {
            startPay = true
            doPay(c, 2)
        } else {
            startPay = false
            doPay(c, 1)
        }
    }

    /**
     *  index = 0快速支付 1支付宝支付 2微信支付
     */
    private fun doPay(c: Activity, index: Int) {
        when (index) {
            0 -> PayManager.getInstance().doFastPay(c, currentServiceId, object : PayCallback {
                override fun success() {
                }

                override fun progress(orderId: String) {
                    orderSn = orderId
                }

                override fun failed(msg: String) {
                    launch(Dispatchers.Main) {
                        ToastUtil.showShort(c, msg)
                    }
                }
            })

            1 -> PayManager.getInstance().doAliPay(c, currentServiceId, object : PayCallback {
                override fun success() {
                    launch(Dispatchers.Main) {

                        //pay upload
                        if (!RomUtil.isOppo()) {
                            val actionParam = JSONObject()
                            actionParam.put(ActionParam.Key.PURCHASE_MONEY, mPrice * 100)
                            BaiduAction.logAction(ActionType.PURCHASE, actionParam)
                        }

                        //返回支付结果
                        ToastUtil.showShort(c, "支付成功")

                        if (currentServiceId == secondServiceId) {
                            toPaySuccessPage()
                        } else {
                            openPaySuccessDialog()
                        }
                    }
                }

                override fun progress(orderId: String) {
                    orderSn = orderId
                }

                override fun failed(msg: String) {
                    launch(Dispatchers.Main) {
                        ToastUtil.showShort(c, msg)
                    }
                }
            })

            2 -> PayManager.getInstance().doWechatPay(c, currentServiceId, object : PayCallback {
                override fun success() {
                }

                override fun progress(orderId: String) {
                    JLog.i("orderId = $orderId")
                    orderSn = orderId
                }

                override fun failed(msg: String) {
                }
            })
        }

    }

    private fun checkPayResult() {
        JLog.i("orderSn = $orderSn")
        if (orderSn == "") return
        launch(Dispatchers.IO) {
            OrderDetailLoader.getOrderStatus(orderSn)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe({
                    JLog.i("order_sn = ${it.order_sn}")
                    if (it.order_sn != orderSn) {
                        return@subscribe
                    }

                    when (it.status) {
                        "1" -> {

                            //pay upload
                            if (!RomUtil.isOppo()) {
                                val actionParam = JSONObject()
                                actionParam.put(ActionParam.Key.PURCHASE_MONEY, mPrice * 100)
                                BaiduAction.logAction(ActionType.PURCHASE, actionParam)
                            }

                            if (currentServiceId == secondServiceId) {
                                toPaySuccessPage()
                            } else {
                                openPaySuccessDialog()
                            }

                            //返回支付结果
                            ToastUtil.showShort(this@PayActivity, "支付成功")
                        }

                        else -> {
                            ToastUtil.show(this@PayActivity, "未支付")
                        }
                    }

                }, {
                    ToastUtil.show(this@PayActivity, "查询支付结果失败")
                })
        }

    }

    private fun toPaySuccessPage() {
        val intent = Intent(this, PaySuccessActivity::class.java)
        intent.putExtra("serviceId", serviceId)
        startActivity(intent)
        finish()
    }

    private fun openPaySuccessDialog() {
        PaySuccessDialog(this@PayActivity, object : DialogCallback {
            override fun onSuccess(file: FileBean) {
                setResult(0x100)
                finish()
            }

            override fun onCancel() {
            }
        }).show()
    }


    override fun onBackPressed() {
        QuitDialog(this, getString(R.string.quite_title), object : DialogCallback {
            override fun onSuccess(file: FileBean) {
                finish()
            }

            override fun onCancel() {
            }
        }).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        timer.cancel()
        counterTimer.cancel()

        if (kv != null && remindTime != 0L) {
            kv?.encode("pay_counter", remindTime)
        }
    }


}