package com.example.appiii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class ActRecycle extends AppCompatActivity {
    private static final String TAG = "ActRecycle";
    //varlues
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> myImageUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recycle);
        Log.i(TAG, "onCreate: started");
        InitialImageBitmaps();
        InitialComponent();
    }

    private void InitialComponent() {
//        txt_click = findViewById(R.id.txt_click);
    }
    TextView txt_click;
    private  void InitialImageBitmaps(){
        Log.i(TAG, "initialImageUrl: preparing bitmaps");
        myImageUrl.add("https://i1.kknews.cc/SIG=1pp30fg/ctp-vzntr/37ps6r6qsp114rss99503s2oqo600548.jpg");
        mName.add("pig1");
        myImageUrl.add("https://png.pngtree.com/png-clipart/20190516/original/pngtree-lovely-kawaii-wow-come-on-png-image_3911238.jpg");
        mName.add("cat1");
        myImageUrl.add("https://miro.medium.com/max/1352/1*XEgA1TTwXa5AvAdw40GFow.png");
        mName.add("dora1");
        myImageUrl.add("https://i1.kknews.cc/SIG=1pp30fg/ctp-vzntr/37ps6r6qsp114rss99503s2oqo600548.jpg");
        mName.add("pig2");
        myImageUrl.add("https://png.pngtree.com/png-clipart/20190516/original/pngtree-lovely-kawaii-wow-come-on-png-image_3911238.jpg");
        mName.add("cat2");
        myImageUrl.add("https://miro.medium.com/max/1352/1*XEgA1TTwXa5AvAdw40GFow.png");
        mName.add("dora2");
        myImageUrl.add("https://i1.kknews.cc/SIG=1pp30fg/ctp-vzntr/37ps6r6qsp114rss99503s2oqo600548.jpg");
        mName.add("pig3");
        myImageUrl.add("https://png.pngtree.com/png-clipart/20190516/original/pngtree-lovely-kawaii-wow-come-on-png-image_3911238.jpg");
        mName.add("cat3");
        myImageUrl.add("https://miro.medium.com/max/1352/1*XEgA1TTwXa5AvAdw40GFow.png");
        mName.add("dora3");
        myImageUrl.add("https://i1.kknews.cc/SIG=1pp30fg/ctp-vzntr/37ps6r6qsp114rss99503s2oqo600548.jpg");
        mName.add("pig4");
        myImageUrl.add("https://png.pngtree.com/png-clipart/20190516/original/pngtree-lovely-kawaii-wow-come-on-png-image_3911238.jpg");
        mName.add("cat4");
        myImageUrl.add("https://miro.medium.com/max/1352/1*XEgA1TTwXa5AvAdw40GFow.png");
        mName.add("dora4");
        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.i(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        RecycleViewAdapter adapter =new RecycleViewAdapter(this, mName, myImageUrl);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    
}
