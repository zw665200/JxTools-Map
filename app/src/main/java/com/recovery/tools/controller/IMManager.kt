package com.recovery.tools.controller

import android.app.Activity
import android.os.Build
import com.hyphenate.chat.ChatClient
import com.hyphenate.chat.ChatClient.ConnectionListener
import com.hyphenate.chat.ChatManager.MessageListener
import com.hyphenate.chat.Message
import com.hyphenate.helpdesk.Error
import com.hyphenate.helpdesk.callback.Callback
import com.hyphenate.helpdesk.easeui.util.IntentBuilder
import com.hyphenate.helpdesk.model.VisitorInfo
import com.recovery.tools.bean.UserInfo
import com.recovery.tools.utils.AppUtil
import com.recovery.tools.utils.JLog
import com.recovery.tools.utils.ToastUtil
import com.tencent.mmkv.MMKV


/**
 * @author Herr_Z
 * @description:
 * @date : 2021/3/8 16:06
 */
object IMManager {
    private var messageListener: MessageListener? = null

    fun register(name: String, success: () -> Unit, failed: (str: String?) -> Unit) {
        ChatClient.getInstance().register(name, Constant.SDK_DEFAULT_PASSWORD, object : Callback {
            override fun onSuccess() {
                success()
                JLog.i("register success")
            }

            override fun onError(code: Int, error: String?) {
                when (code) {
                    Error.USER_ALREADY_EXIST -> success()
                    Error.USER_ALREADY_LOGIN -> success()
                    else -> failed(error)
                }
            }

            override fun onProgress(progress: Int, status: String?) {
            }
        })
    }

    private fun checkLogin(): Boolean {
        return ChatClient.getInstance().isLoggedInBefore
    }

    /**
     * IM login
     * @param activity
     * @param name
     */
    fun login(activity: Activity, name: String, success: () -> Unit) {
        ChatClient.getInstance().login(name, Constant.SDK_DEFAULT_PASSWORD, object : Callback {
            override fun onSuccess() {
                JLog.i("login success")
                success()
            }

            override fun onError(code: Int, error: String?) {
                ToastUtil.show(activity, "???????????????????????????")
            }

            override fun onProgress(progress: Int, status: String?) {
            }
        })
    }


    /**
     * start conversation
     */
    fun startConversation(activity: Activity, name: String, menuDes: String) {
        if (checkLogin()) {
            val value = MMKV.defaultMMKV()?.decodeString("report") ?: ""
            val refund = if (value == "success") {
                "?????????????????????"
            } else {
                "????????????????????????"
            }

            val userInfo = MMKV.defaultMMKV()?.decodeParcelable("userInfo", UserInfo::class.java)
            if (userInfo != null) {
                val date = userInfo.addtime * 1000

                val visitorInfo = VisitorInfo()
                visitorInfo.phone("${Build.BRAND}|${Build.MODEL}")
                visitorInfo.email("app??????" + activity.packageManager.getPackageInfo(activity.packageName, 0).versionName)
                visitorInfo.companyName("??????" + Build.VERSION.RELEASE)
                visitorInfo.nickName(userInfo.nickname)
                visitorInfo.qq("??????D")
                visitorInfo.description("$menuDes||$refund||${AppUtil.timeStamp2Date(date.toString(), null)}")

                val intent = IntentBuilder(activity)
                    .setServiceIMNumber(Constant.SDK_SERVICE_ID)
                    .setTitleName("????????????")
                    .setVisitorInfo(visitorInfo)
                    .build()
                activity.startActivity(intent)
            } else {
                JLog.i("userInfo is empty")
            }

        } else {
            login(activity, name) { startConversation(activity, name, menuDes) }
        }
    }

    fun setConnectionListener() {
        ChatClient.getInstance().addConnectionListener(object : ConnectionListener {
            override fun onConnected() {
                //????????????????????????
            }

            override fun onDisconnected(errorcode: Int) {
                //errorcode??????
                //Error.USER_REMOVED ????????????
                //Error.USER_LOGIN_ANOTHER_DEVICE ???????????????????????????
                //Error.USER_AUTHENTICATION_FAILED ??????????????????
                //Error.USER_NOT_FOUND  ???????????????
            }
        })
    }

    fun setMessageListener(method: (() -> Unit)) {
        if (checkLogin()) {
            if (messageListener == null) {
                messageListener = object : MessageListener {
                    override fun onMessage(list: List<Message?>?) {
                        //??????????????????
                        method()
                    }

                    override fun onCmdMessage(list: List<Message?>?) {
                        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        //???????????????????????????????????????????????????
                    }

                    override fun onMessageStatusUpdate() {
                        //??????????????????????????????????????????????????????????????????????????????
                    }

                    override fun onMessageSent() {
                        //??????????????????????????????????????????????????????????????????????????????
                    }
                }
            }

            ChatClient.getInstance().chatManager().addMessageListener(messageListener)
        }
    }

    fun removeMessageListener() {
        if (checkLogin() && messageListener != null) {
            ChatClient.getInstance().chatManager().removeMessageListener(messageListener)
        }
    }


    /**
     * IM logout
     */
    fun logout() {
        if (checkLogin()) {
            ChatClient.getInstance().logout(true, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(code: Int, error: String?) {
                }

                override fun onProgress(progress: Int, status: String?) {
                }
            })
        }
    }
}