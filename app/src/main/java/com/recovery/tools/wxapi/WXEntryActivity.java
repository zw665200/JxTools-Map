package com.recovery.tools.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.recovery.tools.R;
import com.recovery.tools.controller.Constant;
import com.recovery.tools.utils.JLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constant.TENCENT_APP_ID, false);

        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        int result;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    paySuccess();
                    result = R.string.errcode_success;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = R.string.errcode_cancel;
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = R.string.errcode_deny;
                    break;
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                    result = R.string.errcode_unsupported;
                    break;
                default:
                    result = R.string.errcode_unknown;
                    break;
            }

            JLog.i(getString(result) + ", type=" + resp.getType());
        }

        finish();
    }

    private void paySuccess() {
        JLog.i("weChat pay success");
    }

}