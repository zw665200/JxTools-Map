package com.recovery.tools.controller;


import android.os.Handler;

public class Constant {

    public static final String PRODUCT_ID = "7";
    public static boolean isDebug = true;
    public static Handler mHandler;
    public static String ROM = "";
    public static final String ROM_MIUI = "MIUI";
    public static final String ROM_EMUI = "EMUI";
    public static final String ROM_FLYME = "FLYME";
    public static final String ROM_OPPO = "OPPO";
    public static final String ROM_VIVO = "VIVO";
    public static final String ROM_OTHER = "OTHER";
    public static String CLIENT_TOKEN = "";
    public static String USER_NAME = "";
    public static Boolean ScanStop = false;

    //baidu "5c6109d067bcd295"     0
    //official "e0987005e07e80ef"   1
    //应用宝 "bef03da7cb82efd7" 2
    //百度应用商店 "42ad6f989640c170" 3
    //VIVO应用商店 "46abc5e760a15230" 4
    //百度推广A001 "e5662617319e78c1" 5
    //百度应用商店A01 "74c94129e1d82b94" 6
    //百度推广A002 "ac2999198df31940" 7
    //百度应用商店A002 "fb031e0e61441dee" 8

    //百度推广A006 "5c87b8ed1ca69e96"  06
    //百度应用商店A006 "94dddccbe2ac9263" 06s
    //百度推广A007 "d38c3b1164c26b76"  07
    //百度应用商店A007 "b3289bb361849381" 07s
    public static String CHANNEL_ID = "5c87b8ed1ca69e96";
    public static String WEBSITE = "";

    //Baidu A000
//    public static long USER_ACTION_SET_ID = 11497;
//    public static String APP_SECRET_KEY = "02c05b3d0f857eadd125b2f0c74989bc";

    //Baidu A001
//    public static long USER_ACTION_SET_ID = 11613;
//    public static String APP_SECRET_KEY = "713b68de78726e79011b12a909ae70c2";

    //Baidu A002
//    public static long USER_ACTION_SET_ID = 11823;
//    public static String APP_SECRET_KEY = "b2a7330b44238c87d322de2fd0e0b706";

    //Baidu A006
    public static long USER_ACTION_SET_ID = 12341;
    public static String APP_SECRET_KEY = "704027a480dac865ebd76c87f84e587e";


    //service_code
    public static String REC = "rec";
    public static String COM = "com";
    public static String REPL = "repl";
    public static String BILL = "billrec";
    public static String DELETE = "delete";
    public static String PHOTO_FIX = "photofix";

    //service_expire
    public static String EXPIRE_TYPE_FOREVER = "2";
    public static String EXPIRE_TYPE_YEAR = "1";
    public static String EXPIRE_TYPE_MONTH = "3";

    public static String EXPORT_PATH = "/export/";
    public static String WX_HIGN_VERSION_PATH = "/Android/data/com.tencent.mm/";
    public static String MM_RESOURCE_PATH = "/Android/data/com.immomo.momo/";
    public static String SOUL_RESOURCE_PATH = "/Android/data/cn.soulapp.android/";
    public static String WX_PICTURE_PATH = "/Pictures/WeiXin/";
    public static String PICTURE_PATH = "/Pictures/";
    public static String DOWNLOAD_PATH = "/Download/";
    public static String DCIM_PATH = "/DCIM/";
    public static String QQ_PICTURE_PATH = "/Pictures/";
    public static String WX_DB_PATH = "/App/com.tencent.mm/MicroMsg/";
    public static String WX_ZIP_PATH = "APP/com.tencent.mm.zip";
    public static String WX_RESOURCE_PATH = "/tencent/";
    public static String WX_DOWNLOAD_PATH = "/Download/Weixin/";
    public static String QQ_RESOURCE_PATH = "/tencent/MobileQQ/";
    public static String QQ_HIGN_VERSION_PATH = "/Android/data/com.tencent.mobileqq/";
    public static String WX_BACKUP_NAME = "tencent";
    public static String FLYME_BACKUP_PATH = "/backup/";
    public static String HW_BACKUP_PATH = "/HuaweiBackup/backupFiles/";
    public static String HW_BACKUP_PATH2 = "/huawei/Backup/backupFiles/";

    public static String BACKUP_PATH = "/aA123456在此/";
    public static String XM_BACKUP_PATH = "/MIUI/backup/AllBackup/";
    public static String OPPO_BACKUP_PATH = "/backup/App/";
    public static String HW_BACKUP_NAME_DB = "com.tencent.mm.db";
    public static String HW_BACKUP_NAME_TAR = "com.tencent.mm.tar";
    public static String HW_BACKUP_APP_DATA_TAR = "com.tencent.mm_appDataTar";
    public static String MZ_BACKUP_NAME_TAR = "com.tencent.mm.zip";
    public static String XM_BACKUP_NAME_BAK = "微信(com.tencent.mm).bak";
    public static String OPPO_BACKUP_NAME_TAR = "com.tencent.mm.tar";
    public static String VIVO_BACKUP_NAME_TAR = "5a656b0891e6321126f9b7da9137994c72220ce7";
    public static String HW_BACKUP_NAME_XML = "info.xml";
    public static String JX_BACKUP_PATH = "/backup/";

    public static String DB_NAME = "EnMicroMsg.db";
    public static String DB_FTS_NAME = "FTS5IndexMicroMsg_encrypt.db";

    public static String UIN_CONFIG_NAME = "system_config_prefs.xml";
    public static String SYSTEM_INFO_NAME = "systemInfo.cfg";
    public static String COMPATIBLE_INFO_NAME = "CompatibleInfo.cfg";
    public static String HISTORY_INFO_NAME = "app_brand_global_sp.xml";
    public static String DENGTA_META_NAME = "DENGTA_META.xml";
    public static String AUTH_INFO_KEY_NAME = "auth_info_key_prefs.xml";
    public static String ZIP_FILE_SUFFIX = ".zip";
    public static String SPECIAL_FILE = "MicroMsg";
    public static Long CURRENT_BACKUP_TIME = 0L;
    public static String CURRENT_BACKUP_PATH = "";

    public static int PERMISSION_CAMERA_REQUEST_CODE = 0x00000011;
    public static int CAMERA_REQUEST_CODE = 0x00000012;

    //File
    public static int FILE_NOT_FOUND = 0;
    public static int FILE_UNZIP_FAILED = 1;
    public static int FILE_DAMAGE = 2;
    public static int FILE_CREATE_FAILED = 3;
    public static int FOLDER_CREATE_FAILED = 4;

    //db
    public static String NOT_FOUND_ACCOUNT = "没有找到账户";
    public static String NOT_FOUND_CONTACT = "没有找到联系人";
    public static String NOT_FOUND_MESSAGE = "没有找到聊天记录";

    //Realm
    public static String ROOM_DB_NAME = "EnMicroMsg";

    //IM
    public static String SDK_APP_ID = "132041";
    public static String SDK_APP_KEY = "1482210305025478#kefuchannelapp90871";
    public static String SDK_SERVICE_ID = "kefuchannelimid_451993";
    public static String SDK_DEFAULT_PASSWORD = "123456";

    //Notification
    public static String Notification_title = "消息提醒";
    public static String Notification_content = "您有新的客服消息";

    //Bugly
    public static String BUGLY_APPID = "e3efc56c14";

    //oss
    public static String END_POINT = "http://oss-cn-shenzhen.aliyuncs.com";
    public static String END_POINT_WITHOUT_HTTP = "oss-cn-shenzhen.aliyuncs.com";
    public static String BUCKET_NAME = "qlrecovery";

    //tencent Pay
    public static String TENCENT_APP_ID = "wxfcb054ac8618018e";
    public static String TENCENT_MINI_PROGRAM_APP_ID = "gh_72629534a52d";
    public static String TENCENT_PARTNER_ID = "1605572449";
}
