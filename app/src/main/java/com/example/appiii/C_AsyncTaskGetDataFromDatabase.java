package com.example.appiii;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class C_AsyncTaskGetDataFromDatabase extends AsyncTask<Bundle , Integer, String> {

    private static final String TAG = "C_AsyncTaskGetDataFromDatabase";
    public Interface_AsyncGetDBTask getDBTaskCompleted = null;

    public C_AsyncTaskGetDataFromDatabase(Interface_AsyncGetDBTask task_Completed) {
        this.getDBTaskCompleted = task_Completed;   //Assigning call back interfacethrough constructor
        Log.d("JSON","進來 C_dbconectTask(Interface_AsyncDBTask asyncResponse) taskCompleted  : "+ getDBTaskCompleted);
    }

    private JSONObject jsonfromPhone, jsonfromPHP,emptyfromPHP;
    private JSONArray jsonArrayfromPHP;
    private String Stringinput, StringOutput;
    private String postTophp_CityName;
    private String postTophp_SpotType;
    private Double postTophp_UserLatitude;
    private Double postTophp_UserLongitude;
    private String postSearch_Input;
    private Double[] LocationOutput = new Double[2];
    private String phoneDataJson;
    private StringBuilder builder;

    private HttpURLConnection urlConnection = null;
    private InputStream getinputStream = null;
    private OutputStream out = null;
    private DataOutputStream writer = null;
    public C_AsyncTaskGetDataFromDatabase(){};
    private URL urlAPI;
    {
        try {
            urlAPI = new URL(C_Dictionary.MY_SERVER_URL+"getAndroid.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(Bundle... getStrings) {
        try {
            jsonfromPhone = new JSONObject();
            Log.i("JSON","String 進來 : "+ getStrings);
            Bundle bundle = getStrings[0];
            postTophp_CityName = bundle.getString(C_Dictionary.CITY_NAME_REQUEST);
            postTophp_SpotType = bundle.getString(C_Dictionary.SPOT_TYPE_REQUEST);
//            Log.i(TAG, "doInBackground: LATITUDE : "+bundle.getString(C_Dictionary.USER_LOCATION_LATITUDE));
            if(String.valueOf(bundle.getDouble(C_Dictionary.USER_LOCATION_LATITUDE)) != null && String.valueOf(bundle.getDouble(C_Dictionary.USER_LOCATION_LONGITUDE)) != null ){
                postTophp_UserLatitude = bundle.getDouble(C_Dictionary.USER_LOCATION_LATITUDE);
                postTophp_UserLongitude = bundle.getDouble(C_Dictionary.USER_LOCATION_LONGITUDE);
                Log.i(TAG, "doInBackground: LATITUDE/LONGITUDE :" + postTophp_UserLatitude+"-"+postTophp_UserLongitude);
                jsonfromPhone.put(C_Dictionary.USER_LOCATION_LATITUDE, postTophp_UserLatitude);
                jsonfromPhone.put(C_Dictionary.USER_LOCATION_LONGITUDE, postTophp_UserLongitude);
            }
//            postTophp_SpotType = bundle.getString(C_Dictionary.SPOT_TYPE_REQUEST);
            if(bundle.getString("EDITTXT_SEARCH_INPUT")!=null ){
                postSearch_Input = bundle.getString("EDITTXT_SEARCH_INPUT");
                Log.i(TAG, "doInBackground: postSearch_Input :"+postSearch_Input);
                if(!postSearch_Input.matches(""))
                    jsonfromPhone.put("EDITTXT_SEARCH_INPUT", URLEncoder.encode(postSearch_Input,"UTF-8"));
            }
            Log.i("JSON","CityName 接 bundle : "+ postTophp_CityName);
            Log.i("JSON","SpotType 接 bundle : "+ postTophp_SpotType);
            //手機輸入字串(input)轉成 JSON格式

            postTophp_CityName =  URLEncoder.encode(postTophp_CityName,"UTF-8");  //避免中文亂碼而暫時避掉中文
            postTophp_SpotType = URLEncoder.encode(postTophp_SpotType,"UTF-8");
            Log.i("JSON","String 進來 URLEncoder : "+ postTophp_CityName);
            Log.i("JSON","String 進來 URLEncoder : "+ postTophp_SpotType);
            jsonfromPhone.put(C_Dictionary.CITY_NAME_REQUEST, URLEncoder.encode(postTophp_CityName,"UTF-8"));
            jsonfromPhone.put(C_Dictionary.SPOT_TYPE_REQUEST, URLEncoder.encode(postTophp_SpotType,"UTF-8"));
            Log.i("JSON","JSON包裝成功 : "+ jsonfromPhone);


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
            phoneDataJson = jsonfromPhone.toString();   //為了傳輸字串
            Log.i("JSON", "Json tostring :" + phoneDataJson);
            out = urlConnection.getOutputStream();  // 輸出的初始化
            Log.i("JSON", "os urlConnection.getOutputStream() :" + out);
//                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
//                bw.write(phoneDataJson);
            writer = new DataOutputStream(out);  // 把要送出的資料寫入輸出串流  //Step 4 : 要傳送的檔案/方式 (JSON=TXT檔)
            writer.writeBytes(phoneDataJson);
            writer.flush();
//                bw.flush();
//                out.close();
            out.close();
//                bw.close();
            writer.close();    // Step 5 : close
            /** Get Response 200 represents HTTP OK **/
//                if (urlConnection.getResponseCode() == 200) { /* do something */}
            getinputStream = urlConnection.getInputStream();   //接收初始化 response //Step 6 : 一定要接收 Response 才算成功
//                urlConnection.getInputStream();
//            Log.i("JSON", "getinputStream  :" + getinputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(getinputStream));
            String line;
            builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Log.i("JSON", "respone builder +++++++++++++++ :" + builder.toString());
            jsonArrayfromPHP = new JSONArray(builder.toString());
//            Log.i("JSON", "jsonArrayfromPHP . toS : " + jsonArrayfromPHP.toString());
            reader.close();
            urlConnection.disconnect();
//            Log.i("JSON", "disconnect urlConnection");
//

        }catch(JSONException | UnsupportedEncodingException je) {
            System.err.println(je);
        }catch (Exception e){
            System.err.println(e);
        }
        return jsonArrayfromPHP.toString();
//        return null;
    }
    private String ctiyName, SpotAddress, SpotToldescribe, SpotLocation_latitude, SpotLocation_longitude, NodeImg;
    private String DesKeyName = "Toldescribe";
    double re_lat, re_long;
    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
//        Log.i("JSON","進入 onPostExecute : "+result);
//        if(postTophp_SpotType==C_Dictionary.SPOT_TYPE_HOTEL)
//            DesKeyName = "Description";
        try {
            for (int i = 0; i < jsonArrayfromPHP.length(); i++) {
                jsonfromPHP = jsonArrayfromPHP.getJSONObject(i);
//                Boolean yesNo =jsonfromPHP.getBoolean("empty");
//                if(yesNo != null){
//                    Log.i("C_AsyncTaskGetDataFromDatabase","empty 沒有資料 :"+jsonfromPHP.getBoolean("empty"));
//                    return;
//                }
//                    Log.i("JSON", "jsonfromPHP . toS : " + jsonfromPHP.toString());
                    ctiyName = jsonfromPHP.getString("Name");
                    SpotAddress = jsonfromPHP.getString("Add");
                    SpotToldescribe = jsonfromPHP.getString("Toldescribe");
                    SpotLocation_latitude = jsonfromPHP.getString("Py");
                    SpotLocation_longitude = jsonfromPHP.getString("Px");
//                    Log.i("JSON", ctiyName + ":" + SpotAddress);
                    if(jsonfromPHP.getString("Picture1")!=null ){
                         NodeImg = jsonfromPHP.getString("Picture1");
                    }else {
                        NodeImg="";
                    }
                    getDBTaskCompleted.GetDBTaskFinish(ctiyName.trim(), SpotAddress.trim(), SpotToldescribe.trim(),
                            Double.parseDouble(SpotLocation_latitude.trim()), Double.parseDouble(SpotLocation_longitude.trim()), NodeImg.trim() );   //Call function
//
            }
//            re_name = jsonfromPHP.getString("name");
//            Log.i("JSON", "name : " + re_name);
//            re_lat = jsonfromPHP.getDouble("lat");
//            Log.i("JSON", "lat : " + re_lat);
//            re_long = jsonfromPHP.getDouble("long");
//            Log.i("JSON", "long : " + re_long);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ActGoogleMaps.txt_getAttraction.setText(result);
        //GetDBTaskFinish(int ID, String Name, int cityNumber, String address, Double Lcation_lat, Double Lcation_long);
//        getDBTaskCompleted.GetDBTaskFinish(re_name,re_lat,re_long);   //Call function
//        Log.i("JSON","進入 onPostExecute 呼叫介面 taskCompleted" + taskCompleted);
    }


}
