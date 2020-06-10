package com.example.appiii;

public class C_Dictionary {

    public final static String CITY_NAME_REQUEST = "CITY_NAME_REQUEST";  // json KEY
    public final static String CITY_NAME_KEELUNG = "CITY_NAME_KEELUNG";  //基隆
    public final static String CITY_NAME_TAIPEI = "CITY_NAME_TAIPEI";  // 臺北
    public final static String CITY_NAME_NEW_TAIPEI = "CITY_NAME_NEW_TAIPEI";  //新北市
    public final static String CITY_NAME_YILAN = "CITY_NAME_YILAN";  // 宜蘭
    public final static String CITY_NAME_TAOYUAN = "CITY_NAME_TAOYUAN";  // 桃園
    public final static String CITY_NAME_HSINCHU = "CITY_NAME_HSINCHU";  //新竹
    public final static String CITY_NAME_MIAOLI = "CITY_NAME_MIAOLI";  //苗栗
    public final static String CITY_NAME_TAICHUNG = "CITY_NAME_TAICHUNG";  //台中
    public final static String CITY_NAME_CHANGHUA = "CITY_NAME_CHANGHUA";  //彰化
    public final static String CITY_NAME_NANTOU = "CITY_NAME_NANTOU";  //南投
    public final static String CITY_NAME_YUNLIN = "CITY_NAME_YUNLIN";  //雲林
    public final static String CITY_NAME_CHIAYI = "CITY_NAME_CHIAYI";  //嘉義
    public final static String CITY_NAME_TAINAN = "CITY_NAME_TAINAN";  //台南
    public final static String CITY_NAME_KAOHSIUNG = "CITY_NAME_KAOHSIUNG";  //高雄
    public final static String CITY_NAME_PINGTUNG = "CITY_NAME_PINGTUNG";  //屏東
    public final static String CITY_NAME_TAITUNG = "CITY_NAME_TAITUNG";  //台東
    public final static String CITY_NAME_HUALIEN = "CITY_NAME_HUALIEN";  //花蓮
    public final static String CITY_NAME_PENGHU = "CITY_NAME_PENGHU";  //澎湖
    public final static String CITY_NAME_KINMEN = "CITY_NAME_KINMEN";  //金門
    public final static String CITY_NAME_LIENCHIANG = "CITY_NAME_LIENCHIANG";  //連江

    public final static String SPOT_NAME = "SPOT_NAME";
    public static final String SPOT_TYPE = "SPOT_TYPE";
    public final static String SPOT_TYPE_REQUEST = "SPOT_TYPE_REQUEST";
    public final static String SPOT_TYPE_HOTEL = "SPOT_TYPE_HOTEL";  //住宿
    public final static String SPOT_TYPE_VIEW = "SPOT_TYPE_VIEW";  //風景
    public final static String SPOT_TYPE_CULTURE = "SPOT_TYPE_CULTURE"; //文化

    public final static String USER_ACCOUNT = "USER_ACCOUNT";
    public final static String USER_PASSWORD = "USER_PASSWORD";

    public final static String USER_SIGNIN_OR_SIGNUP = "USER_SIGNIN_OR_SIGNUP";
    public final static String USER_STATUS = "USER_STATUS";
    public final static String USER_STATUS_MEMBER = "USER_STATUS_MEMBER";
    public final static String USER_STATUS_VISITORS = "USER_STATUS_VISITORS";
    public final static String USER_SIGNUP_ACCOUNT = "USER_SIGNUP_ACCOUNT";
    public final static String USER_SIGNUP_PASSWORD = "USER_SIGNUP_PASSWORD";

    public final static String LOCATION_LONGITUDE = "LOCATION_LONGITUDE";
    public final static String LOCATION_LATITUDE = "LOCATION_LATITUDE";
    public final static String SEARCH_SPOT_INFO_COPY = "SEARCH_SPOT_INFO_COPY";


    //建立欄位的資料型太
    //NULL、INTEGER、REAL（浮點數）、TEXT(字串)和 BLOB(布林)
    public static final String VALUE_TYPE_DOUBLE = " REAL NOT NULL";
    public static final String VALUE_TYPE_INT = " INT NOT NULL";
    public static final String VALUE_TYPE_STRING = " TEXT NOT NULL";
    public static final String VALUE_TYPE_BLOB = " BLOB NOT NULL";
    public static final String VALUE_COMMA_SEP = ", ";

    public static final String TRAVEL_LIST_Table_Name = "TRAVEL_LIST_Table_Name";
    public static final String TRAVEL_LIST_SCHEMA_PLAN_NAME = "TRAVEL_LIST_SCHEMA_PLAN_NAME";
    public static final String CREATE_TABLE_if_not_exists = "CREATE TABLE IF NOT EXISTS ";
    public static final String INTEGER_PRIMARY_KEY_AUTOINCREMENT = " INTEGER_PRIMARY_KEY_AUTOINCREMENT ";
    public static final String TRAVEL_Table_Name = "TRAVEL_Table_Name";
    public static final String TABLE_SCHEMA_DATE = "COLUMN_NAME_DATE";
    public static final String TABLE_SCHEMA_DATE_END = "TABLE_SCHEMA_DATE_END";
    public static final String TABLE_SCHEMA_DATE_START = "TABLE_SCHEMA_DATE_START";
    public static final String TABLE_SCHEMA_QUEUE = "COLUMN_NAME_QUEUE";
    public static final String TABLE_SCHEMA_NODE_NAME = "TABLE_SCHEMA_NODE_NAME";
    public static final String TABLE_SCHEMA_NODE_LONGITUDE = "TABLE_SCHEMA_NODE_LONGITUDE";
    public static final String TABLE_SCHEMA_NODE_LATITUDE = "TABLE_SCHEMA_NODE_LATITUDE";

    public static final String CREATE_TABLE_HEADER = "plan_";  // 建 Plan 時附加的 Table 名稱
    public static final String TRAVEL_SCHEMA_TABLE_VISIBILITY = "TRAVEL_SCHEMA_TABLE_VISIBILITY";  // 紀錄是否有公開行程表

    public static final String TABLE_SCHEMA_id = "_id";
    public static final int TRAVEL_ADD_PLAN_REQUEST_CODE = 301;  // travel activity for add plan
    public static final int TRAVEL_DEL_PLAN_REQUEST_CODE = 302;  // travel activity for delete plan

    public static final String TRAVEL_MAX_PLAN_DAY = "TRAVEL_MAX_PLAN_DAY";  // travel activity for show single plan
    public static final String TRAVEL_PLAN_IS_EMPTY = "TRAVEL_PLAN_IS_EMPTY";
    public static final String TABLE_SCHEMA_NODE_DESCRIBE = "TABLE_SCHEMA_NODE_DESCRIBE";

    public static final String TABLE_SCHEMA_PASSWORD = "TABLE_SCHEMA_PASSWORD";
    public static final String TABLE_SCHEMA_ACCOUNT = "TABLE_SCHEMA_ACCOUNT";
    public static final String TABLE_NAME_ACCOUNT_INFORMATION = "TABLE_NAME_ACCOUNT_INFORMATION";


    public static final String MY_COLLECTION_TABLE = "MY_COLLECTION_TABLE";  //我的最愛的資料表名稱

    public static final String ACCOUNT_SETTING = "ACCOUNT_SETTING";   // 帳號設定的SharedPreference資料名稱
    public static final String REMEMBER_ACCOUNT_SETTING = "RemamberAccountSetting";  // 自動記憶帳號
    public static final String AUTO_SIGN_IN_SETTING = "AutoSignInSetting";  //自動登入+記憶帳密
    public static final String USER_NICK_NAME = "USER_NICK_NAME"; // 使用者暱稱
    public static final String USER_U_ID = "USER_U_ID"; // 使用者ID  唯一

    public static final String PUBLIC_TRAVEL_PLAN_TO_CLOUD = "PUBLIC_TRAVEL_PLAN_TO_CLOUD"; // 確認使否要上傳雲端
    public static final String PUBLIC_TO_CLOUND_SIGNAL = "PUBLIC_TO_CLOUND_SIGNAL";   //行程表 上傳雲端許可 FOR AsyncTask FOR PHP
    public static final String CLOUND_TABLE_NAME = "CLOUND_TABLE_NAME";  //雲端上的新 table 名
    public static final String PLAN_DAYS_RECORD = "PLAN_DAYS_RECORD";
    public static final String USER_HEAD_IMG = "USER_HEAD_IMG";
    public static final String USER_LOCATION_LATITUDE = "USER_LOCATION_LATITUDE";
    public static final String USER_LOCATION_LONGITUDE = "USER_LOCATION_LONGITUDE";

    //////////// URL
    public static final String MY_SERVER_URL = "http://hhlc.ddnsking.com/";

}
