package com.example.appiii.ui.Travel.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.appiii.C_Dictionary;
import com.example.appiii.ui.Travel.C_PlanDetail;
import com.example.appiii.ui.Travel.C_UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class C_AsyncGetPlanDetail extends AsyncTask<Bundle,Void,String> {
    private Context mContext;
    private static final String TAG = "C_AsyncTravelPlanList";
    private URL urlAPI;
    {
        try {
            urlAPI = new URL(C_Dictionary.MY_SERVER_URL+"getPlanDetail.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private HttpURLConnection urlConnection = null;
    private InputStream getinputStream = null;
    private OutputStream out = null;
    private DataOutputStream writer = null;
    private StringBuilder input_stringbuilder;


//    public C_AsyncGetPlanDetail(Context mContext){
//        this.mContext = mContext;
//    }

    private Interface_AsyncGetPlanDetail PlanDetail_finish;

    public C_AsyncGetPlanDetail(Interface_AsyncGetPlanDetail PlanDetail_finish){
        this.PlanDetail_finish = PlanDetail_finish;
    }

    String pName;
    @Override
    protected String doInBackground(Bundle... bundles) {

//        get pName = bundles[0].getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME);
//        get uAccount = bundles[0].getString(C_Dictionary.USER_ACCOUNT);
        pName = bundles[0].getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME);
        JSONObject JsonObject = new JSONObject();
        try {
            JsonObject.put(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME, URLEncoder.encode(pName,"UTF-8"));
            JsonObject.put(C_Dictionary.USER_ACCOUNT, bundles[0].getString(C_Dictionary.USER_ACCOUNT));

            Log.i(TAG, "doInBackground: pName : "+ bundles[0].getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME));
            Log.i(TAG, "doInBackground: uAccount : "+ bundles[0].getString(C_Dictionary.USER_ACCOUNT));

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
            writer.writeBytes(String.valueOf(JsonObject));
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
            Log.i(TAG, "doInBackground: input_stringbuilder : +++++++++++++++++++++++++++ : "+ input_stringbuilder.toString());
            reader.close();
            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return input_stringbuilder.toString();
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        Log.i(TAG, "onPostExecute: onPostExecute:" + string);
        try {
            JSONObject getJson = new JSONObject(string);
//            getJson.getString("userInfo");
//            getJson.getString("planDatail");
//            URL url_img ;

            JSONObject getJsonUserInfo = new JSONObject(getJson.getString("userInfo"));
            String userUID = getJsonUserInfo.getString("u_id");
            String userAccount = getJsonUserInfo.getString("account");
            String userNickName = getJsonUserInfo.getString("user_name");
            String userHeadImg = getJsonUserInfo.getString("head_img");
            String userSchedule = getJsonUserInfo.getString("schedule");
            Log.i(TAG, "onPostExecute: userHeadImg : "+userHeadImg);
            C_UserInfo getUserInfo = new C_UserInfo(userUID, userAccount, userNickName, pName,userSchedule, userHeadImg);

            JSONArray jsPlanDetail = new JSONArray(getJson.getString("planDatail"));
//            Log.i(TAG, "onPostExecute: json size"+ jsarray.length());
            ArrayList<C_PlanDetail> getplandetail = new ArrayList<>();
            for(int i=0; i<jsPlanDetail.length();i++){
                JSONObject jsobj = new JSONObject();
                jsobj = jsPlanDetail.getJSONObject(i);
                int COLUMN_NAME_DATE = jsobj.getInt(C_Dictionary.TABLE_SCHEMA_DATE);
                int COLUMN_NAME_QUEUE = jsobj.getInt(C_Dictionary.TABLE_SCHEMA_QUEUE);
                String TABLE_SCHEMA_NODE_NAME = jsobj.getString(C_Dictionary.TABLE_SCHEMA_NODE_NAME);
                Double TABLE_SCHEMA_NODE_LATITUDE = jsobj.getDouble(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE);
                Double TABLE_SCHEMA_NODE_LONGITUDE = jsobj.getDouble(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE);
                String TABLE_SCHEMA_NODE_DESCRIBE = jsobj.getString(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE);

                getplandetail.add(new C_PlanDetail(COLUMN_NAME_DATE, COLUMN_NAME_QUEUE, TABLE_SCHEMA_NODE_NAME, TABLE_SCHEMA_NODE_LATITUDE, TABLE_SCHEMA_NODE_LONGITUDE, TABLE_SCHEMA_NODE_DESCRIBE));
//                PlanDetail_finish.GatPlanDetailFinish(COLUMN_NAME_DATE, COLUMN_NAME_QUEUE, TABLE_SCHEMA_NODE_NAME, TABLE_SCHEMA_NODE_LATITUDE ,TABLE_SCHEMA_NODE_LONGITUDE, TABLE_SCHEMA_NODE_DESCRIBE);
//                PlanDetail_finish.GatPlanDetailFinish(al_bundle);
            }
            Log.i(TAG, "onPostExecute: getplandetail size :"+ getplandetail.size());
//            Log.i(TAG, "onPostExecute: getplandetail :"+ getplandetail);

            PlanDetail_finish.GetPlanDetailFinish( getUserInfo , getplandetail );
        } catch ( Exception e) { //JSONException e |
            e.printStackTrace();
        }
    }

}
