package com.example.appiii.ui.Member;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.example.appiii.C_Dictionary;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class C_AsyncUploadImg extends AsyncTask<Uri, Void, Void> {
    private String TAG = "C_AsyncUploadImg";
    Context mContext;
    public C_AsyncUploadImg(Context mContext){
        this.mContext = mContext;
    }

    private URL urlAPI;
    {
        try {
            urlAPI = new URL("http://hhlc.ddnsking.com/uploadImg.php");  //pushtoclound  getplanlist
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private HttpURLConnection urlConnection = null;
    private InputStream getinputStream = null;
    private OutputStream out = null;
    private FileOutputStream fout;
    private DataOutputStream writer = null;
    private StringBuilder input_stringbuilder;
    private ByteArrayOutputStream byteArrayOutputStream;
    private Bitmap bitmap;


    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    @Override
    protected Void doInBackground(Uri... uris) {
        boundary = "===" + System.currentTimeMillis() + "===";


        Uri uri = uris[0];
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "doInBackground: 222 uri " + uri);
        File file = new File(uri.getPath());
        Log.i(TAG, "doInBackground: 222 file " + file.toString());
        String filePath = uri.getPath();
        Log.i(TAG, "doInBackground: filePath :" + filePath);
        String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
        Log.i(TAG, "doInBackground: file_extn :" + file_extn);
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 3;
        byte[] bitmapArray = new byte[bitmap.getWidth()*bitmap.getHeight()];
//        bitmap = BitmapFactory.decodeFile(uri.getPath(),options);
        byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        Log.i(TAG, "doInBackground: bitmap: " + bitmap);
//        byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
        bitmapArray = byteArrayOutputStream.toByteArray();
        String encode_string = Base64.encodeToString(bitmapArray,0);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("imgName",encode_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.i(TAG, "doInBackground: encode_string: "+encode_string);

        try {
            urlConnection = (HttpURLConnection) urlAPI.openConnection();  // STEP 1
            Log.i("JSON", "urlConnection :" + urlConnection);
            /* optional request header */
            // 把Content Type設為multipart/form-data
            // 以及設定Boundary，Boundary很重要!
            // 當你不只一個參數時，Boundary是用來區隔參數的

            urlConnection.setRequestProperty("Charset", "UTF-8");
            /* optional request header */
//            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("image", "headShot");
            /* for Get request */
            urlConnection.setRequestMethod("POST"); // method一定要是POST
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setDoInput(true);    //允許輸入流，即允許下載
            urlConnection.setDoOutput(true);   //允許輸出流，即允許上傳
            urlConnection.setUseCaches(false); //設置是否使用緩存
            urlConnection.setRequestProperty("Content-Type","multipart/form-data; boundary="+boundary);   // STEP 2 : 連線設定 /  設定檔案型別:
            urlConnection.connect();    // // STEP 3 : 連線開啟  ( 安卓 4拿掉 ? )






                    out = urlConnection.getOutputStream();  // 輸出的初始化
            writer = new DataOutputStream(out);  // 把要送出的資料寫入輸出串流  //Step 4 : 要傳送的檔案/方式 (JSON=TXT檔)
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);
            String filename = sharedPreferences.getString(C_Dictionary.TABLE_SCHEMA_ACCOUNT,"dafault_user");

            filename = filename+".jpg";


            writer.writeBytes(twoHyphens + boundary + lineEnd);
            writer.writeBytes("Content-Disposition: form-data; name=\"uploadHeadShot\"; filename=\""+filename +"\"" + lineEnd);
            writer.writeBytes("Content-Type: image/*"+ lineEnd);
            writer.writeBytes(lineEnd);

            writer.writeBytes(encode_string);

            writer.writeBytes(lineEnd);
            writer.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            Log.i(TAG, "doInBackground: writer : "+writer.toString());
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