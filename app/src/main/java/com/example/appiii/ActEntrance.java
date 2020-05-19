package com.example.appiii;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActEntrance extends AppCompatActivity {

    private View.OnClickListener btn_Visitors_Signin_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActEntrance.this, ActMain.class);
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_entrance);
        InitialComponent();
    }
    private void InitialComponent() {
        btn_Visitors_Signin = findViewById(R.id.btn_Visitors_Signin);
        btn_Visitors_Signin.setOnClickListener(btn_Visitors_Signin_click);
        btn_Customer_Signin = findViewById(R.id.btn_Customer_Signin);
//        btn_Customer_Signin.setOnClickListener(btn_Customer_Signin_click);
        edtxt_account = findViewById(R.id.edtxt_account);
        edtxt_passwd = findViewById(R.id.edtxt_passwd);
        txt_account = findViewById(R.id.txt_account);
        txt_passwd = findViewById(R.id.txt_passwd);
    }
    Button btn_Visitors_Signin;
    Button btn_Customer_Signin;
    EditText edtxt_account;
    EditText edtxt_passwd;
    TextView txt_account;
    TextView txt_passwd;

}
