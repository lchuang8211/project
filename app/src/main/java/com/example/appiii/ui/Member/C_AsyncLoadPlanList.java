package com.example.appiii.ui.Member;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.appiii.C_Dictionary;
import com.example.appiii.C_MySQLite;
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
import java.util.ArrayList;
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

    public C_AsyncLoadPlanList(Context mContext,Interface_AsyncLoadPlanList interface_LoadPlanListlist_finish){
        this.interface_LoadPlanListlist_finish = interface_LoadPlanListlist_finish;
        this.mContext = mContext;
    }

    String getUID;
    @Override
    protected String doInBackground(Bundle... bundles) {

        getUID = bundles[0].getString(C_Dictionary.USER_U_ID);
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
    C_MySQLite helper;
    SQLiteDatabase sqlite;
    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        helper = new C_MySQLite(mContext);
        sqlite = helper.getReadableDatabase();
        ArrayList<C_LoadPlanList> loadPlanList = new ArrayList<>();
        ArrayList<C_LoadPlanDetail> loadPlanDetail = new ArrayList<>();
        Log.i(TAG, "onPostExecute: onPostExecute:" + string);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            JSONArray jsa = new JSONArray(string);
            for (int i =0 ; i< jsa.length() ; i++){   // for load planlist
                JSONObject jsplan = jsa.getJSONObject(i);
                jsplan.getJSONObject("plan_info");
                JSONObject jsp = jsplan.getJSONObject("plan_info");
                JSONArray jsplandetail = new JSONArray(jsplan.getString("plan_detail"));
//                js.getString(C_Dictionary.USER_U_ID);
                String pName = jsp.getString("plan_name");
                String sDate = jsp.getString("start_date");
                String eDate = jsp.getString("end_date");
                String Date = sDate+" ~ "+eDate;

                Date startdate  = df.parse(sDate);
                Date enddate =  df.parse(eDate);
                int tatolDay = (int)( Math.abs( enddate.getTime()-startdate.getTime() )/(60*60*1000*24))+1 ;
                loadPlanList.add(new C_LoadPlanList(pName, Date, tatolDay));

                Cursor cursor = sqlite.rawQuery("select * from "+C_Dictionary.TRAVEL_LIST_Table_Name+" where "+C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+"=?",new String[]{pName});
                if (cursor.getCount()>0){
                    Log.i("cursor","TRAVEL_LIST_SCHEMA_PLAN_NAME : 表單已存在 " +pName);
                }else{
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(C_Dictionary.USER_U_ID, getUID);
                    contentValues.put(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME,pName);
                    contentValues.put(C_Dictionary.TABLE_SCHEMA_DATE_START,sDate);
                    contentValues.put(C_Dictionary.TABLE_SCHEMA_DATE_END,eDate);
                    contentValues.put(C_Dictionary.TRAVEL_SCHEMA_TABLE_VISIBILITY,1);
                    sqlite.insert(C_Dictionary.TRAVEL_LIST_Table_Name,null,contentValues);


                    String newPlanTable = C_Dictionary.CREATE_TABLE_if_not_exists +"["+C_Dictionary.CREATE_TABLE_HEADER+pName + "] ("
                            + C_Dictionary.TABLE_SCHEMA_DATE+C_Dictionary.VALUE_TYPE_INT+C_Dictionary.VALUE_COMMA_SEP
                            + C_Dictionary.TABLE_SCHEMA_QUEUE+C_Dictionary.VALUE_TYPE_INT+C_Dictionary.VALUE_COMMA_SEP
                            + C_Dictionary.TABLE_SCHEMA_NODE_NAME+C_Dictionary.VALUE_TYPE_STRING+C_Dictionary.VALUE_COMMA_SEP
                            + C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE+C_Dictionary.VALUE_TYPE_DOUBLE+C_Dictionary.VALUE_COMMA_SEP
                            + C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE+C_Dictionary.VALUE_TYPE_DOUBLE +C_Dictionary.VALUE_COMMA_SEP
                            + C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE+C_Dictionary.VALUE_TYPE_STRING +" )";
                    sqlite.execSQL( newPlanTable );

                    for(int j =0; j<jsplandetail.length(); j++){
                        JSONObject jspdetail = jsplandetail.getJSONObject(i);

                        ContentValues cv = new ContentValues();
                        cv.put(C_Dictionary.TABLE_SCHEMA_DATE,jspdetail.getInt("COLUMN_NAME_DATE"));
                        cv.put(C_Dictionary.TABLE_SCHEMA_QUEUE,jspdetail.getInt("COLUMN_NAME_QUEUE"));
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME,jspdetail.getString("TABLE_SCHEMA_NODE_NAME"));
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE,jspdetail.getDouble("TABLE_SCHEMA_NODE_LATITUDE"));
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE,jspdetail.getDouble("TABLE_SCHEMA_NODE_LONGITUDE"));
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE, jspdetail.getString("TABLE_SCHEMA_NODE_DESCRIBE"));
                        sqlite.insert(C_Dictionary.CREATE_TABLE_HEADER+pName,null,cv);

                    }
                    ////////////////////
                    //  for(int i=0 ; i < getdatail.length ; i++){
                    //      for load all plandetail to sqlite
                    //  }
                    ////////////////////
                }
            }

            interface_LoadPlanListlist_finish.LoadPlanListFinish(loadPlanList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
