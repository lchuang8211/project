package com.example.appiii.ui.Member;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appiii.R;

public class ActMyCollect extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mycollect);
        setTitle("我的最愛");
        InitialComponent();
    }

    private void InitialComponent() {

    }
}
