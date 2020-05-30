package com.example.appiii.ui.Member;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;

import java.util.ArrayList;

public class ActMyCollect extends AppCompatActivity {

    private ArrayList<String> collectNodeName = new ArrayList<>();
    private ArrayList<String> collectNodeDescribe = new ArrayList<>();
    private ArrayList<Double> collectNodeLatitude = new ArrayList<>();
    private ArrayList<Double> collectNodeLongitude = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mycollect);
        setTitle("我的最愛");
        InitialComponent();
        InitialRecycler();
    }


    private void InitialComponent() {
        SQLite_helper = new C_MySQLite(this);
        sqLiteDatabase = SQLite_helper.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("select * from "+ C_Dictionary.MY_COLLECTION_TABLE,null);
        if( collectNodeName.size()>0 || collectNodeDescribe.size()>0 || collectNodeLatitude.size()>0 || collectNodeLongitude.size()>0 ){
            collectNodeName.clear(); collectNodeDescribe.clear(); collectNodeLatitude.clear(); collectNodeLongitude.clear();
        }
        if (cursor.getCount()>0){
            while(cursor.moveToNext()){
                collectNodeName.add(cursor.getString(0));
                collectNodeDescribe.add(cursor.getString(1));
                collectNodeLatitude.add(cursor.getDouble(2));
                collectNodeLongitude.add(cursor.getDouble(3));
            }
        }else
            return;
    }


    private void InitialRecycler() {
        recyclerView = findViewById(R.id.rcle_mycollect);
        adapter = new C_MyCollectRecyclerViewAdapter(ActMyCollect.this, collectNodeName, collectNodeDescribe, collectNodeLatitude, collectNodeLongitude);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    C_MySQLite SQLite_helper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    RecyclerView recyclerView;
    C_MyCollectRecyclerViewAdapter adapter;

}
