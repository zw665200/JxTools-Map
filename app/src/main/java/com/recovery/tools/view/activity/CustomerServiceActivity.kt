package com.recovery.tools.view.activity

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recovery.tools.R
import com.recovery.tools.adapter.DataAdapter
import com.recovery.tools.bean.Resource
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.IMManager
import com.recovery.tools.controller.PayManager
import com.recovery.tools.http.loader.CheckPayLoader
import com.recovery.tools.http.response.ResponseTransformer
import com.recovery.tools.http.schedulers.SchedulerProvider
import com.recovery.tools.utils.ToastUtil
import com.recovery.tools.view.base.BaseActivity
import kotlinx.android.synthetic.main.item_customer.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerServiceActivity : BaseActivity() {
    private lateinit var customer: RecyclerView
    private lateinit var back: ImageView
    private lateinit var charge: Button
    private var payed = false
    private var descrption = ""

    override fun setLayout(): Int {
        return R.layout.a_customer_service
    }

    override fun initView() {
        back = findViewById(R.id.iv_back)
        customer = findViewById(R.id.customer_service)
        charge = findViewById(R.id.charge)

        back.setOnClickListener { finish() }
        charge.setOnClickListener { toPayPage() }
    }

    override fun initData() {
        loadCustomerService()
        checkPay()
    }

    private fun loadCustomerService() {
        val list = arrayListOf<Resource>()
        list.add(Resource("wechat", R.drawable.customer_tel, "在线客服"))
        list.add(Resource("doc", R.drawable.customer_gd, "提交反馈"))
        list.add(Resource("doc", R.drawable.common_qs, "常见问题"))

        val mAdapter = DataAdapter.Builder<Resource>()
            .setData(list)
            .setLayoutId(R.layout.item_customer)
            .addBindView { itemView, itemData, position ->
                Glide.with(this).load(itemData.icon).into(itemView.service_icon)
                itemView.service_name.text = itemData.name
                when (position) {
                    0 -> {
                        val text = "在线客服(10:00-22:00)"
                        itemView.tv_service_title.text = text
                        itemView.tv_service_descrition.text = getString(R.string.vip_service_des)
                    }

                    1 -> {
                        itemView.tv_service_title.text = "投诉与退款"
                        itemView.tv_service_descrition.text = getString(R.string.visitor_service_des)
                    }

                    2 -> {
                        itemView.tv_service_title.text = "常见问题回复"
                        itemView.tv_service_descrition.text = getString(R.string.common_questions_des)
                    }
                }

                itemView.setOnClickListener {
                    when (position) {
                        0 -> {
                            if (payed) {
                                checkUserStatus()
                            } else {
                                ToastUtil.showShort(this, "成为会员即可发起会话")
                            }
                        }


                        1 -> {
                            if (payed) {
                                val intent = Intent()
                                intent.setClass(this, FeedbackActivity::class.java)
                                startActivity(intent)
                            } else {
                                ToastUtil.showShort(this, "成为会员即可投诉与退款")
                            }
                        }

                        2 -> {
                            val intent = Intent()
                            intent.setClass(this, QuestionActivity::class.java)
                            startActivity(intent)
                        }
                    }

                }
            }
            .create()

        customer.layoutManager = LinearLayoutManager(this)
        customer.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }

    /**
     * 检查顾客是否开通了Vip
     */
    private fun checkPay() {
        launch(Dispatchers.IO) {
            CheckPayLoader.checkPay()
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe({
                    if (it.msg == "true") {
                        payed = true
                        descrption = "需要核实具体支付套餐"
                    }
                }, {})
        }
    }


    private fun toPayPage() {
        if (!payed) {
            val intent = Intent()
            intent.setClass(this, PayActivity::class.java)
            intent.putExtra("serviceId", 1)
            startActivity(intent)
        } else {
            ToastUtil.showShort(this, "您已经是尊贵的VIP用户")
        }
    }

    private fun checkUserStatus() {
        if (Constant.USER_NAME.isEmpty()) return
        IMManager.register(Constant.USER_NAME, { startConversation() }, {})
    }

    private fun startConversation() {
        //防止进入会话发送消息还在回调
        IMManager.removeMessageListener()
        IMManager.startConversation(this, Constant.USER_NAME, descrption)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}