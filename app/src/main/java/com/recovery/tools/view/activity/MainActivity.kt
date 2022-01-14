package com.recovery.tools.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import com.recovery.tools.R
import com.recovery.tools.controller.Constant
import com.recovery.tools.view.base.BaseFragment
import com.recovery.tools.view.base.BaseFragmentActivity
import com.recovery.tools.view.fragment.*
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class MainActivity : BaseFragmentActivity(), View.OnClickListener {

    companion object {
        private const val FRAGMENT_HOME = 0

        private const val DEFAULT_INDEX = FRAGMENT_HOME

        val BOTTOM_ICON_CHECKED = arrayOf(
            R.drawable.ic_global_pic_select,
            R.drawable.vr_icon,
            R.drawable.ic_global_mine_select
        )

        val BOTTOM_ICON_UNCHECKED = arrayOf(
            R.drawable.ic_global_pic_unselect,
            R.drawable.vr_icon,
            R.drawable.ic_global_mine_unselect
        )

        val BOTTOM_TEXT_ARRAY = arrayOf("地图", "", "我的")

        const val BOTTOM_CHECKED_COLOR: Int = 0xff826666.toInt()
        const val BOTTOM_UNCHECKED_COLOR: Int = 0xffc0c0c0.toInt()

        val FRAGMENT_CLASS_ARRAY: Array<Class<out BaseFragment>> = arrayOf(
            FHome::class.java,
            FInland::class.java,
            FMine::class.java
        )

    }

    private var mCheckedFragmentID: Int = DEFAULT_INDEX


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.a_home)
        super.onCreate(savedInstanceState)
        regToWx()
    }

    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_blue)
        }
    }


    override fun onItemClick(item: View?, index: Int) {
        mCheckedFragmentID = index
    }


    override fun putFragments(): Array<Class<out BaseFragment>> {
        return FRAGMENT_CLASS_ARRAY
    }

    private val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
    override fun getBottomItemView(index: Int): View {
        val bottomView = bottomLayoutInflater.inflate(R.layout.l_home_bottom, null)
        val bottomLayout = bottomView.findViewById<LinearLayout>(R.id.home_page_bottom_layout)
        bottomLayout.layoutParams = params

        val bottomImage = bottomView.findViewById<ImageView>(R.id.home_page_bottom_image)
        if (index != 1) {
            bottomImage.setImageResource(BOTTOM_ICON_UNCHECKED[index])
        }
        val buttonName = bottomView.findViewById<TextView>(R.id.home_page_bottom_btn_name)
        buttonName.text = BOTTOM_TEXT_ARRAY[index]
        return bottomView
    }

    override fun getFLid(): Int {
        return R.id.fl_home_body
    }

    override fun getBottomLayout(): LinearLayout? {
        return this@MainActivity.findViewById(R.id.ll_home_bottom)
    }

    override fun checkAllBottomItem(item: View?, position: Int, isChecked: Boolean) {
        if (position != 1) {
            (item?.findViewById<ImageView>(R.id.home_page_bottom_image))?.setImageResource(if (isChecked) BOTTOM_ICON_CHECKED[position] else BOTTOM_ICON_UNCHECKED[position])
            (item?.findViewById<TextView>(R.id.home_page_bottom_btn_name))?.setTextColor(if (isChecked) BOTTOM_CHECKED_COLOR else BOTTOM_UNCHECKED_COLOR)
        }
    }

    override fun setTabSel(item: View?, index: Int) {
        super.setTabSel(item, index)

    }

    private fun changeFragment(index: Int) {
        setTabSel(bottomLayout?.getChildAt(index), index)
    }


    private var mLastClick: Long = 0L
    override fun onBackPressed() {
        if (System.currentTimeMillis() - mLastClick < 2000) {
            val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                addCategory(Intent.CATEGORY_HOME)
            }
            startActivity(homeIntent)
        } else {
            Toast.makeText(this@MainActivity, "再按一下后退键退出程序", Toast.LENGTH_SHORT).show()
            mLastClick = System.currentTimeMillis()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.hook -> {

            }
        }
    }

    private fun regToWx() {
        val api = WXAPIFactory.createWXAPI(this, Constant.TENCENT_APP_ID, true)
        api.registerApp(Constant.TENCENT_APP_ID)

        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                api.registerApp(Constant.TENCENT_APP_ID)
            }
        }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))
    }

}