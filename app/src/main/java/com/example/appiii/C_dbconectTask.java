package com.example.appiii;

import android.os.AsyncTask;
import android.util.Log;

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

public class C_dbconectTask extends AsyncTask<String, Integer, String> {

    public Interface_AsyncDBTask taskCompleted = null;

    public C_dbconectTask(Interface_AsyncDBTask task_Completed) {
        this.taskCompleted = task_Completed;   //Assigning call back interfacethrough constructor
        Log.d("JSON","進來 C_dbconectTask(Interface_AsyncDBTask asyncResponse) taskCompleted  : "+ taskCompleted);
    }

    private JSONObject jsonfromPhone, jsonfromPHP;
    private String Stringinput, StringOutput;
    private Double[] LocationOutput = new Double[2];
    private String phoneDataJson;
    private StringBuilder builder;
    private String re_name;
    double re_lat, re_long;
    private HttpURLConnection urlConnection = null;
    private InputStream getinputStream = null;
    private OutputStream out = null;
    private DataOutputStream writer = null;
    public C_dbconectTask(){};
    private URL urlAPI;
    {
        try {
            urlAPI = new URL("http://hhlc.ddnsking.com/getAndroid.php");
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
    protected String doInBackground(String... getStrings) {
            try {
                Log.i("JSON","String 進來 : "+ getStrings);
                Stringinput = getStrings[0];
                //手機輸入字串(input)轉成 JSON格式
                jsonfromPhone = new JSONObject();
                Stringinput =  URLEncoder.encode(Stringinput,"UTF-8");  //避免中文亂碼而暫時避掉中文
                Log.i("JSON","String 進來 URLEncoder : "+ Stringinput);
                jsonfromPhone.put("phoneDataJson", URLEncoder.encode(getStrings[0],"UTF-8"));
                Log.i("JSON","JSON包裝成功 : "+ jsonfromPhone);
                Log.i("JSON","myAsyncTask 執行緒開啟");

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
                Log.i("JSON", "getinputStream  :" + getinputStream);
                BufferedReader reader = new BufferedReader(new InputStreamReader(getinputStream));
                String line;
                builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                Log.i("JSON", "respone builder :" + builder.toString());
                jsonfromPHP = new JSONObject(builder.toString());
                Log.i("JSON", "jsonfromPHP . toS : " + jsonfromPHP.toString());


                reader.close();
                urlConnection.disconnect();
                Log.i("JSON", "disconnect urlConnection");

                LocationOutput[0] = re_lat;
                LocationOutput[1] = re_long;
            }catch(JSONException | UnsupportedEncodingException je) {
                System.err.println(je);
            }catch (Exception e){
                System.err.println(e);
            }
        return jsonfromPHP.toString();
    }
    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        Log.i("JSON","進入 onPostExecute : "+result);
        try {
            re_name = jsonfromPHP.getString("name");
            Log.i("JSON", "name : " + re_name);
            re_lat = jsonfromPHP.getDouble("lat");
            Log.i("JSON", "lat : " + re_lat);
            re_long = jsonfromPHP.getDouble("long");
            Log.i("JSON", "long : " + re_long);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        ActGoogleMaps.txt_getAttraction.setText(result);
        taskCompleted.AsyncTaskFinish(re_name,re_lat,re_long);   //Call function
        Log.i("JSON","進入 onPostExecute 呼叫介面 taskCompleted" + taskCompleted);
    }
}
