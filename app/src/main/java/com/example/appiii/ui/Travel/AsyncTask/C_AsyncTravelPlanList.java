package com.example.appiii.ui.Travel.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

public class C_AsyncTravelPlanList extends AsyncTask<Bundle,Void,String> {
    private Context mContext;
    private static final String TAG = "C_AsyncTravelPlanList";
    private URL urlAPI;
    {
        try {
            urlAPI = new URL("http://hhlc.ddnsking.com/getplanlist.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private HttpURLConnection urlConnection = null;
    private InputStream getinputStream = null;
    private OutputStream out = null;
    private DataOutputStream writer = null;
    private StringBuilder input_stringbuilder;


    public C_AsyncTravelPlanList(Context mContext){
        this.mContext = mContext;
    }

    private Interface_AsyncPlanList interface_plist_finish;

    public C_AsyncTravelPlanList(Interface_AsyncPlanList interface_plist_finish){
        this.interface_plist_finish = interface_plist_finish;
    }


    @Override
    protected String doInBackground(Bundle... bundles) {

        String region = bundles[0].getString("Search_requirement");


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
            urlConnection.setDoInput(true);   //允許輸入流，即允許下載
            urlConnection.setDoOutput(true);  //允許輸出流，即允許上傳
            urlConnection.setUseCaches(true); //設置是否使用緩存
            urlConnection.connect();    // // STEP 3 : 連線開啟  ( 安卓 4拿掉 ? )

//            out = urlConnection.getOutputStream();  // 輸出的初始化
//            writer = new DataOutputStream(out);  // 把要送出的資料寫入輸出串流  //Step 4 : 要傳送的檔案/方式 (JSON=TXT檔)
//            writer.writeBytes(String.valueOf(setJsonObject));
////                writer.writeBytes(json2);
//            writer.flush();
//            out.close();
//            writer.close();
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return input_stringbuilder.toString();
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        Log.i(TAG, "onPostExecute: onPostExecute:" + string);
        try {
            URL url_img ;
            JSONArray jsarray = new JSONArray(string);
            Log.i(TAG, "onPostExecute: json size"+ jsarray.length());
            for(int i=0; i<jsarray.length();i++){
                JSONObject jsobj = new JSONObject();
                jsobj = jsarray.getJSONObject(i);
                String getUesrAccount = jsobj.getString("account");
                ///////////-------- nick name
                String getUserName = jsobj.getString("user_name");
                String getPlanName = jsobj.getString("plan_Name");
                String getStarDate = jsobj.getString("start_date");
                String getEndDate = jsobj.getString("end_date");
                String getHead_img = jsobj.getString("head_img");
//                String[] tokens = getlist.split("\\|");
                interface_plist_finish.PlanListFinish( getUesrAccount, getUserName, getPlanName, getStarDate, getEndDate, getHead_img);  //nick_name
                Log.i(TAG, "onPostExecute: tokens :"+getUesrAccount+"::"+getUserName+"::"+getPlanName+"::"+getStarDate+"::"+getEndDate+"::"+getHead_img);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
