package com.example.appiii.ui.Member;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.appiii.C_Dictionary;
import com.example.appiii.C_MySQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class C_AsyncPublicPlan extends AsyncTask<Bundle,Void,Void> {
    private Context mContext;
    private static final String TAG = "C_AsyncPublicPlan";
    private URL urlAPI;
    {
        try {
            urlAPI = new URL("http://hhlc.ddnsking.com/pushtoclound.php");  //pushtoclound  getplanlist
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private HttpURLConnection urlConnection = null;
    private InputStream getinputStream = null;
    private OutputStream out = null;
    private DataOutputStream writer = null;
    private StringBuilder input_stringbuilder;


    public C_AsyncPublicPlan(Context mContext){
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Bundle... bundles) {

        Boolean Signal = bundles[0].getBoolean(C_Dictionary.PUBLIC_TO_CLOUND_SIGNAL);
        String planName = bundles[0].getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME);
        Log.i(TAG,"get Signal : "+Signal );
        Log.i(TAG,"get planName : "+planName );
        JSONArray setJsonArray = new JSONArray();
        JSONObject setJsonObject = new JSONObject();
        C_MySQLite myhelper = new C_MySQLite(mContext);
        SQLiteDatabase SQLiteDB = myhelper.getReadableDatabase();
        Cursor cursor;
        String startDate="";
        String endDate="";

            cursor = SQLiteDB.rawQuery("select * from " + C_Dictionary.TRAVEL_LIST_Table_Name + " where " + C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME + "='" + planName + "'", null);
            if (cursor.getCount() > 0) {
                Log.i(TAG, "get getCount : " + cursor.getCount());
                while (cursor.moveToNext()) {
                    Log.i(TAG, "doInBackground: while : " + cursor.getString(0) + cursor.getString(1) + cursor.getString(2));
                    startDate = cursor.getString(1); // 行程開始的日期
                    endDate = cursor.getString(2);  // 行程結束的日期
                }
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(C_Dictionary.ACCOUNT_SETTING, 0); //儲存的檔案名稱
                String userID = sharedPreferences.getString(C_Dictionary.USER_U_ID, "").trim();  //使用者ID 唯一
                String userAccount = sharedPreferences.getString(C_Dictionary.TABLE_SCHEMA_ACCOUNT, "").trim(); // 使用者帳號

                String new_planName = userID + "|" + userAccount + "|" + planName + "|" + startDate + "|" + endDate;

                try {
                    setJsonObject.put(C_Dictionary.PUBLIC_TO_CLOUND_SIGNAL,Signal);   // 放刪除/新增 訊號
//                    setJsonObject.put(C_Dictionary.CLOUND_TABLE_NAME, URLEncoder.encode(new_planName.trim(), "UTF-8"));  // 要建的 Table name
                    setJsonObject.put(C_Dictionary.USER_U_ID,userID);  // u_id   USER_U_ID  USER_ACCOUNT TRAVEL_LIST_SCHEMA_PLAN_NAME TABLE_SCHEMA_DATE_START TABLE_SCHEMA_DATE_END
                    setJsonObject.put(C_Dictionary.USER_ACCOUNT,userAccount);  // 使用者帳號
                    setJsonObject.put(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME , URLEncoder.encode(planName.trim(),"UTF-8"));  // 行程名稱
                    setJsonObject.put(C_Dictionary.TABLE_SCHEMA_DATE_START , startDate);  // 開始日期
                    setJsonObject.put(C_Dictionary.TABLE_SCHEMA_DATE_END , endDate);  // 結束日期
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
                Log.i(TAG, "get all setJsonObject1 : " + setJsonObject);
        if(Signal) {  // 要新增才 撈資料
                cursor = SQLiteDB.rawQuery("select * from " + C_Dictionary.CREATE_TABLE_HEADER + planName, null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        try {
                            JSONObject setJsonObject2 = new JSONObject();
                            setJsonObject2.put(C_Dictionary.TABLE_SCHEMA_DATE, cursor.getInt(0));
                            setJsonObject2.put(C_Dictionary.TABLE_SCHEMA_QUEUE, cursor.getInt(1));
                            setJsonObject2.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME, URLEncoder.encode(cursor.getString(2), "UTF-8"));
                            setJsonObject2.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE, cursor.getDouble(3));
                            setJsonObject2.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE, cursor.getDouble(4));
                            setJsonObject2.put(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE, URLEncoder.encode(cursor.getString(5), "UTF-8"));
                            setJsonArray.put(setJsonObject2);
                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i(TAG, "get setJsonArray : " + setJsonArray);
                }
                try {
                    setJsonObject.put("planQueue", setJsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }

//            String json1 = setJsonObject.toString();
//            String json2 = setJsonArray.toString();
            try {
                urlConnection = (HttpURLConnection) urlAPI.openConnection();  // STEP 1
                Log.i("JSON", "urlConnection :" + urlConnection);
                /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");   // STEP 2 : 連線設定 /  設定檔案型別:
                urlConnection.setRequestProperty("Charset", "UTF-8");
                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");
                /* for Get request */
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setDoInput(true);    //允許輸入流，即允許下載
                urlConnection.setDoOutput(true);   //允許輸出流，即允許上傳
                urlConnection.setUseCaches(true); //設置是否使用緩存
                urlConnection.connect();    // // STEP 3 : 連線開啟  ( 安卓 4拿掉 ? )

                out = urlConnection.getOutputStream();  // 輸出的初始化
                writer = new DataOutputStream(out);  // 把要送出的資料寫入輸出串流  //Step 4 : 要傳送的檔案/方式 (JSON=TXT檔)
                writer.writeBytes(String.valueOf(setJsonObject));
//                writer.writeBytes(json2);
                writer.flush();
                out.close();
                writer.close();
                /////////////////////////////////////////////////////////////////
                getinputStream = urlConnection.getInputStream();   //接收初始化 response //Step 6 : 一定要接收 Response 才算成功
                BufferedReader reader = new BufferedReader(new InputStreamReader(getinputStream));
                String line;
                input_stringbuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    input_stringbuilder.append(line);
                }
                Log.i(TAG, "doInBackground: input_stringbuilder : "+ input_stringbuilder.toString());
                reader.close();
                urlConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }




        return null;
    }

}
