package com.example.appiii.ui.Member;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.example.appiii.C_Dictionary;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

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
    private FileInputStream fis = null;
    private DataOutputStream writer = null;
    private StringBuilder input_stringbuilder;
    String mimeType = null;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    public String getMimeType(Uri uri) {    //  get ContenType

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = mContext.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
    Uri uri;
    String MimeType;
    String filetype="";
    @Override
    protected Void doInBackground(Uri... uris) {
        boundary = "===" + System.currentTimeMillis() + "===";
        Log.i(TAG, "doInBackground: 222 boundary " + boundary);
        uri = uris[0];
        MimeType = getMimeType(uri);
        Log.i(TAG, "doInBackground: 222 fileType " + MimeType);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);
        UserName = sharedPreferences.getString(C_Dictionary.USER_U_ID,"dafault_user");

        CompressImage();
        ConnectHTTP();

        return null;
    }
    String UserName;
    int retryTime;
    private void ConnectHTTP() {
        try {

            retryTime = 0;
            urlConnection = (HttpURLConnection) urlAPI.openConnection();  // STEP 1
            Log.i("JSON", "urlConnection :" + urlConnection);
            /* optional request header */
            // 把Content Type設為multipart/form-data
            // 以及設定Boundary，Boundary很重要!
            // 當你不只一個參數時，Boundary是用來區隔參數的
            /* optional request header */
//            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("image", "headShot");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setChunkedStreamingMode(128 * 1024); // 128K
            /* for POST request */
            urlConnection.setRequestMethod("POST"); // method一定要是POST
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setDoInput(true);    //允許輸入流，即允許下載
            urlConnection.setDoOutput(true);   //允許輸出流，即允許上傳
            urlConnection.setUseCaches(false); //設置是否使用緩存
            urlConnection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);   // STEP 2 : 連線設定 /  設定檔案型別:
            urlConnection.connect();    // // STEP 3 : 連線開啟  ( 安卓 4拿掉 ? )
            // OutputStream
            out = urlConnection.getOutputStream();  // 輸出的初始化
            // DataOutputStream
            writer = new DataOutputStream(out);  // 把要送出的資料寫入輸出串流  //Step 4 : 要傳送的檔案/方式 (JSON=TXT檔)


            writer.writeBytes(twoHyphens + boundary + lineEnd);
            writer.writeBytes("Content-Disposition: form-data; name=\"uploadHeadShot\"; filename=\""+ UserName + filetype +"\"" + lineEnd);
            writer.writeBytes("Content-Type: "+ MimeType + lineEnd);
            writer.writeBytes(lineEnd);

//                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            // FileInputStream
            fis = new FileInputStream( mContext.getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor() );
//            fis = new FileInputStream(byteArray);
//             URI inputstream
            byte[] buffer = new byte[8192*2]; // 8k*2
            int count = 0;
            while ((count = fis.read(buffer)) != -1){
                writer.write(buffer, 0, count);
            }
            fis.close();
//            writer.writeBytes(Arrays.toString(byteArray));

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
            if(retryTime < 3){
                retryTime++;
                Log.i(TAG, "connecthttp: IOException : "+retryTime);
                ConnectHTTP();
            }
        }
    }
    int contrl;
    byte[] byteArray;
    private void CompressImage() {
        Log.i(TAG, "CompressImage: ");
        InputStream imageStream = null;
        try {
            contrl = 80;
            imageStream = mContext.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream baosstream = new ByteArrayOutputStream();

        switch(MimeType){
            case "image/jpeg" :
                filetype = ".jpeg";
//                while (baosstream.toByteArray().length / 1024 > 500) {  //迴圈判斷如果壓縮後圖片是否大於500kb,大於繼續壓縮
//                    baosstream.reset();//重置baos即清空baos
//                    contrl -= 10;//每次都減少10
//                    bmp.compress(Bitmap.CompressFormat.JPEG, contrl, baosstream);//這裡壓縮options%,把壓縮後的資料存放到baos中
//                    bmp = BitmapFactory.decodeStream(new ByteArrayInputStream(baosstream.toByteArray()));
//                Log.i(TAG, "CompressImage: 後 :"+baosstream.toByteArray().length);
//                }
                break;
            case "image/png" :
                filetype = ".png";
//                while (baosstream.toByteArray().length / 1024 > 500) {  //迴圈判斷如果壓縮後圖片是否大於500kb,大於繼續壓縮
//                    baosstream.reset();//重置baos即清空baos
//                    contrl -= 10;//每次都減少10
//                    bmp.compress(Bitmap.CompressFormat.PNG, contrl, baosstream);//這裡壓縮options%,把壓縮後的資料存放到baos中
//                    bmp = BitmapFactory.decodeStream(new ByteArrayInputStream(baosstream.toByteArray()));
//                }
                break;
        }

//       byteArray = baosstream.toByteArray();
        try {
            baosstream.close();
            baosstream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
