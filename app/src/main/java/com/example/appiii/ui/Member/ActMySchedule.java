package com.example.appiii.ui.Member;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.appiii.R;

public class ActMySchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_schedule);
        setTitle("行程表");
        InitialComponent();

    }

    private void InitialComponent() {
        txt_Schedule = findViewById(R.id.txt_Schedule);
    }
    TextView txt_Schedule;
}
