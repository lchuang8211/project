package com.example.appiii;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

public class C_EntranceTask extends AsyncTask<Bundle, Void, String> {
    private static final String TAG = "C_EntranceTask";
    private Interface_AsyncEntrance memberCheck = null;
    private String userAccout , userPassword, userNickName, passMember;
    private Context mContext;
    private HttpURLConnection urlConnection = null;
    private InputStream getinputStream = null;
    private OutputStream out = null;
    private DataOutputStream writer = null;
    private StringBuilder builder;
    private URL urlAPI;
    {
        try {
            urlAPI = new URL("http://hhlc.ddnsking.com/userMember.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    C_EntranceTask(Context mContext, Interface_AsyncEntrance memberCheck){
        this.mContext=mContext;
        this.memberCheck = memberCheck;
    }
    SharedPreferences sh;
    @Override
    protected String doInBackground(Bundle... bundles) {
        JSONObject userMember = new JSONObject();
        sh = mContext.getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);


        Bundle bundle = bundles[0];
        int userSign_in_or_up = bundle.getInt(C_Dictionary.USER_SIGNIN_OR_SIGNUP);

        try {
            if(userSign_in_or_up==1) {
                userAccout = bundle.getString(C_Dictionary.USER_ACCOUNT);
                userPassword = bundle.getString(C_Dictionary.USER_PASSWORD);
                userMember.put(C_Dictionary.USER_ACCOUNT,userAccout);
                userMember.put(C_Dictionary.USER_PASSWORD,userPassword);
//                userMember.put(C_Dictionary.USER_NICK_NAME, URLEncoder.encode(sh.getString(C_Dictionary.USER_NICK_NAME,""),"UTF-8") );
            }else if(userSign_in_or_up==2){
                userAccout = bundle.getString(C_Dictionary.USER_SIGNUP_ACCOUNT);
                userPassword = bundle.getString(C_Dictionary.USER_SIGNUP_PASSWORD);
                userNickName = bundle.getString(C_Dictionary.USER_NICK_NAME);
                userMember.put(C_Dictionary.USER_SIGNUP_ACCOUNT,userAccout);
                userMember.put(C_Dictionary.USER_SIGNUP_PASSWORD,userPassword);
                userMember.put(C_Dictionary.USER_NICK_NAME, URLEncoder.encode(userNickName,"UTF-8"));
            }
            userMember.put(C_Dictionary.USER_SIGNIN_OR_SIGNUP,String.valueOf(userSign_in_or_up));
            Log.i("userMember","userMember :"+userMember.toString());
            urlConnection = (HttpURLConnection) urlAPI.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");   // STEP 2 : 連線設定 /  設定檔案型別:
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setDoInput(true);    //允許輸入流，即允許下載
            urlConnection.setDoOutput(true);

            passMember = userMember.toString();
            out = urlConnection.getOutputStream();
            writer = new DataOutputStream(out);  // 把要送出的資料寫入輸出串流  //Step 4 : 要傳送的檔案/方式 (JSON=TXT檔)
            writer.writeBytes(passMember);
            writer.flush();
            out.close();
            writer.close();    // Step 5 : close

            getinputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(getinputStream));
            String line;
            builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
//            userMember.getBoolean("checkMember");
            Log.i("builder","echo builder :"+builder.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    return builder.toString().trim();

    }
    SharedPreferences.Editor w;
    boolean check = false;
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {

            JSONObject js = new JSONObject(result);
        switch (js.getInt("ENTRANCE_NUM")){
            case 1:
                check=true;
                Log.i("builder","1echo check :"+check);
                SharedPreferences sh = mContext.getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);
                SharedPreferences.Editor w = sh.edit();
                w.putString(C_Dictionary.USER_HEAD_IMG, js.getString("head_img")).apply();
                Log.i(TAG, "onPostExecute:head_img : "+js.getString("head_img"));
                w.putString(C_Dictionary.USER_U_ID, js.getString("u_id")).apply();
                w.putString(C_Dictionary.USER_NICK_NAME, js.getString(C_Dictionary.USER_NICK_NAME)).apply();
                break;
            case 0:
                check=false;
                Log.i("builder","2echo check :"+check);
            break;
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("builder","3echo check :"+check);
        memberCheck.memberCheckFinish(check);
    }
}
