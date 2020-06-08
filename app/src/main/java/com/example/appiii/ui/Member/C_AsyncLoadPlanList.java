package com.example.appiii.ui.Member;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.appiii.C_Dictionary;
import com.example.appiii.ui.Travel.Interface_AsyncPlanList;

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
import java.util.Date;

public class C_AsyncLoadPlanList extends AsyncTask<Bundle,Void,String> {
    private Context mContext;
    private static final String TAG = "C_AsyncTravelPlanList";
    private URL urlAPI;
    {
        try {
            urlAPI = new URL("http://hhlc.ddnsking.com/LoadPersonalPlan.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private HttpURLConnection urlConnection = null;
    private InputStream getinputStream = null;
    private OutputStream out = null;
    private DataOutputStream writer = null;
    private StringBuilder input_stringbuilder;


    public C_AsyncLoadPlanList(Context mContext){
        this.mContext = mContext;
    }

    private Interface_AsyncLoadPlanList interface_LoadPlanListlist_finish;

    public C_AsyncLoadPlanList(Interface_AsyncLoadPlanList interface_LoadPlanListlist_finish){
        this.interface_LoadPlanListlist_finish = interface_LoadPlanListlist_finish;
    }


    @Override
    protected String doInBackground(Bundle... bundles) {

        String getUID = bundles[0].getString(C_Dictionary.USER_U_ID);
        Log.i(TAG, "doInBackground: getUID : "+getUID);

        try {
            JSONObject js = new JSONObject() ;
            js.put(C_Dictionary.USER_U_ID,getUID);
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

            out = urlConnection.getOutputStream();  // 輸出的初始化
            writer = new DataOutputStream(out);  // 把要送出的資料寫入輸出串流  //Step 4 : 要傳送的檔案/方式 (JSON=TXT檔)
            writer.writeBytes(String.valueOf(js));
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
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            JSONArray jsa = new JSONArray(string);
            for (int i =0 ; i< jsa.length() ; i++){
                JSONObject js = jsa.getJSONObject(i);
//                js.getString(C_Dictionary.USER_U_ID);
                String pName = js.getString("plan_name");
                String sDate = js.getString("start_date");
                String eDate = js.getString("end_date");
                String Date = sDate+" ~ "+eDate;

                Date startdate  = df.parse(sDate);
                Date enddate =  df.parse(eDate);
                int tatolDay = (int)( Math.abs( enddate.getTime()-startdate.getTime() )/(60*60*1000*24))+1 ;
                interface_LoadPlanListlist_finish.LoadPlanListFinish(pName,Date,tatolDay);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
