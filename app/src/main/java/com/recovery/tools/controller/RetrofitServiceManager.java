package com.recovery.tools.controller;

import com.recovery.tools.http.ApiConfig;
import com.recovery.tools.http.request.AliPayService;
import com.recovery.tools.http.request.CheckPayService;
import com.recovery.tools.http.request.ComplaintService;
import com.recovery.tools.http.request.ConfigService;
import com.recovery.tools.http.request.FastPayService;
import com.recovery.tools.http.request.OrderCancelService;
import com.recovery.tools.http.request.OrderDetailService;
import com.recovery.tools.http.request.OrderService;
import com.recovery.tools.http.request.OssService;
import com.recovery.tools.http.request.PayStatusService;
import com.recovery.tools.http.request.ServiceListService;
import com.recovery.tools.http.request.SinglePayStatusService;
import com.recovery.tools.http.request.TokenService;
import com.recovery.tools.http.request.UserInfoService;
import com.recovery.tools.http.request.WechatPayService;
import com.recovery.tools.utils.JLog;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {

    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit mRetrofit;
    private static RetrofitServiceManager mInstance;
    private static volatile UserInfoService userInfo = null;
    private static volatile TokenService token = null;
    private static volatile OrderService orderService = null;
    private static volatile ConfigService configService = null;
    private static volatile AliPayService aliPayService = null;
    private static volatile FastPayService fastPayService = null;
    private static volatile ServiceListService priceService = null;
    private static volatile OrderDetailService orderDetailService = null;
    private static volatile OrderCancelService orderCancelService = null;
    private static volatile PayStatusService payStatusService = null;
    private static volatile SinglePayStatusService singlePayStatusService = null;
    private static volatile OssService ossService = null;
    private static volatile ComplaintService complaintService = null;
    private static volatile WechatPayService wechatPayService = null;
    private static volatile CheckPayService checkPayService = null;

    public static RetrofitServiceManager getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitServiceManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitServiceManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化retrofit
     */
    public void initRetrofitService() {
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间
//        builder.addInterceptor(new BaseUrlInterceptor());

        //打印网络请求日志
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);

        // 添加公共参数拦截器
//        HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
//                .addHeaderParams("paltform", "android")
//                .addHeaderParams("userToken", "1234343434dfdfd3434")
//                .addHeaderParams("userId", "123445")
//                .build();
//        builder.addInterceptor(commonInterceptor);

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiConfig.BASE_URL_1)
                .build();
    }

    public UserInfoService getUserInfo() {
        if (userInfo == null) {
            synchronized (UserInfoService.class) {
                userInfo = mRetrofit.create(UserInfoService.class);
            }
        }
        return userInfo;
    }

    public TokenService getToken() {
        if (token == null) {
            synchronized (TokenService.class) {
                token = mRetrofit.create(TokenService.class);
            }
        }
        return token;
    }

    public ConfigService getConfig() {
        if (configService == null) {
            synchronized (ConfigService.class) {
                configService = mRetrofit.create(ConfigService.class);
            }
        }
        return configService;
    }

    public OrderService getOrders() {
        if (orderService == null) {
            synchronized (OrderService.class) {
                orderService = mRetrofit.create(OrderService.class);
            }
        }
        return orderService;
    }

    public AliPayService getAliPayParam() {
        if (aliPayService == null) {
            synchronized (AliPayService.class) {
                aliPayService = mRetrofit.create(AliPayService.class);
            }
        }
        return aliPayService;
    }

    public FastPayService getFastPayParam() {
        if (fastPayService == null) {
            synchronized (FastPayService.class) {
                fastPayService = mRetrofit.create(FastPayService.class);
            }
        }
        return fastPayService;
    }

    public ServiceListService getPrice() {
        if (priceService == null) {
            synchronized (ServiceListService.class) {
                priceService = mRetrofit.create(ServiceListService.class);
            }
        }
        return priceService;
    }

    public OrderDetailService getOrderDetail() {
        if (orderDetailService == null) {
            synchronized (OrderDetailService.class) {
                orderDetailService = mRetrofit.create(OrderDetailService.class);
            }
        }
        return orderDetailService;
    }

    public OrderCancelService orderCancel() {
        if (orderDetailService == null) {
            synchronized (OrderCancelService.class) {
                orderCancelService = mRetrofit.create(OrderCancelService.class);
            }
        }
        return orderCancelService;
    }

    public PayStatusService getPayStatus() {
        if (payStatusService == null) {
            synchronized (PayStatusService.class) {
                payStatusService = mRetrofit.create(PayStatusService.class);
            }
        }
        return payStatusService;
    }

    public WechatPayService getWechatPayStatus() {
        if (wechatPayService == null) {
            synchronized (PayStatusService.class) {
                wechatPayService = mRetrofit.create(WechatPayService.class);
            }
        }
        return wechatPayService;
    }

    public SinglePayStatusService getSinglePayStatus() {
        if (singlePayStatusService == null) {
            synchronized (SinglePayStatusService.class) {
                singlePayStatusService = mRetrofit.create(SinglePayStatusService.class);
            }
        }
        return singlePayStatusService;
    }

    public CheckPayService checkPayService() {
        if (checkPayService == null) {
            synchronized (CheckPayService.class) {
                checkPayService = mRetrofit.create(CheckPayService.class);
            }
        }
        return checkPayService;
    }

    public OssService getOssToken() {
        if (ossService == null) {
            synchronized (OssService.class) {
                ossService = mRetrofit.create(OssService.class);
            }
        }
        return ossService;
    }

    public ComplaintService reportComplaint() {
        if (complaintService == null) {
            synchronized (ComplaintService.class) {
                complaintService = mRetrofit.create(ComplaintService.class);
            }
        }
        return complaintService;
    }


    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

}
