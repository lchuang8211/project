package com.example.appiii;


import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class ActDBconect extends AppCompatActivity  {
//    public String myIP = "http://hhlc.ddnsking.com/";
    public URL urlAPI =  new URL("http://hhlc.ddnsking.com/getAndroid.php");
    InputStream inputStream = null;

    String urlWithParams = urlAPI.toString();

    private View.OnClickListener btn_send_click=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Log.i("JSON","Send按鍵觸發");
            postDatatest(txtinput.getText().toString());
//            webView.loadUrl("http://118.171.121.119/page3.php?phoneDataJson="+txtinput.getText().toString());
            Log.i("JSON","Send按鍵結束");
        }
    };
    private View.OnClickListener btn_get_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Log.i("JSON","Get按鍵觸發");
            getDatatest();
            Log.i("JSON","Get按鍵結束");
        }
    };

    public ActDBconect() throws MalformedURLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dbconect);
        InitialComponent();
        webView_all();

    }
    JSONObject jsonfromPhone, jsonfromPHP;
    Gson gson;
    String data ;
    myTask_TophpSQL myAsyncTask1;
    private void postDatatest(String input)  {
        try {
            //手機輸入字串(input)轉成 JSON格式
            jsonfromPhone = new JSONObject();
            input =  URLEncoder.encode(input,"UTF-8");  //避免中文亂碼而暫時避掉中文
            jsonfromPhone.put("phoneDataJson", input);
            Log.i("JSON","JSON包裝成功 : "+jsonfromPhone);
            data =  input;
            Log.i("JSON","data包裝 : "+data);
            myAsyncTask1 = new myTask_TophpSQL(); //初始化非同步任務函數
            myAsyncTask1.execute(input);
            Log.i("JSON","myAsyncTask 執行緒開啟");
        }catch(JSONException | UnsupportedEncodingException je) {
            System.err.println(je);
            Log.i("JSON","JSON包裝失敗");
        }
    }
    void getDatatest(){
        if(builder!=null){
            txtoutput.setText(builder.toString());
            Log.i("JSON", "解析JSON成功 "+ jsonfromPhone);
        }else
            return;
    }

    String phoneDataJson;
    StringBuilder response, builder;
    String re_name;
    String re_lat,re_long;
    HttpURLConnection urlConnection = null;
    InputStream getinputStream = null;
    OutputStream out = null;
    DataOutputStream writer = null;
    class myTask_TophpSQL extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... strings) {

            try {

                urlConnection = (HttpURLConnection) urlAPI.openConnection();  // STEP 1
                Log.i("JSON","urlConnection :"+ urlConnection);
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
                Log.i("JSON","Json tostring :"+ phoneDataJson);
                out = urlConnection.getOutputStream();  // 輸出的初始化
                Log.i("JSON","os urlConnection.getOutputStream() :" + out);
//                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
//                bw.write(phoneDataJson);
                writer = new DataOutputStream( out );  // 把要送出的資料寫入輸出串流  //Step 4 : 要傳送的檔案/方式 (JSON=TXT檔)
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
                response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
//                    response.append(line);
                    System.err.println(line);
//                    response.append('\r');
                }
                Log.i("JSON", "builder >>>>>>" + builder.toString());
                jsonfromPHP = new JSONObject(builder.toString());
                re_name =jsonfromPHP.getString("name");
                Log.i("JSON","name : " + re_name);
                re_lat =jsonfromPHP.getString("lat");
                Log.i("JSON","lat : " + re_lat);
                re_long =jsonfromPHP.getString("long");
                Log.i("JSON","long : " + re_long);
                reader.close();
                urlConnection.disconnect();
                Log.i("JSON", "disconnect urlConnection");
            }catch (Exception e){
                System.err.println(e);
            }
            phoneshowdata.setText(re_name +", " +re_lat+", "+re_long);
            return null;
        }
    }

    private void InitialComponent() {
        phoneshowdata= findViewById(R.id.phoneshowdata);
        txtoutput = findViewById(R.id.txtoutput);
        txtinput = findViewById(R.id.txtinput);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(btn_send_click);
        btn_get = findViewById(R.id.btn_get);
        btn_get.setOnClickListener(btn_get_click);
    }

    private void webView_all() {
        webView = findViewById(R.id.webView);
//        webView.loadUrl(myIP+"page5.php");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);   // 取得網頁JS效果
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setBuiltInZoomControls(true); //是否支持手指縮放
    }

    EditText txtinput;
    TextView txtoutput,phoneshowdata;
    Button btn_send,btn_get;
    WebView webView;
}