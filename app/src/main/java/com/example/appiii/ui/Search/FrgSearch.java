package com.example.appiii.ui.Search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.C_Dictionary;
import com.example.appiii.C_GetDataFromDatabase;
import com.example.appiii.Interface_AsyncGetDBTask;
import com.example.appiii.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FrgSearch extends Fragment {
    View inflatedView_Search;
    private ArrayList<Integer> database_ID = new ArrayList<>();
    private ArrayList<String> database_Name = new ArrayList<>();
    private ArrayList<String> database_address = new ArrayList<>();
    private ArrayList<String> mySpotToldescribe = new ArrayList<>();
    private ArrayList<Double> database_lat = new ArrayList<>();
    private ArrayList<Double> database_long = new ArrayList<>();
    private ArrayList<String> database_cityNumber = new ArrayList<>();
    private String cityName = "" ;
    private int cityNameNumber = -1 ;
    private String spotType = "";
    static final String[] cityNameArray = {
            "基隆市","臺北市","新北市","桃園市","新竹縣","新竹市",
            "苗栗縣","臺中市","彰化縣","南投縣",
            "雲林縣","嘉義縣","嘉義市","臺南市","高雄市","屏東縣",
            "宜蘭縣","臺東縣","花蓮縣",
            "澎湖縣","金門縣","連江縣"
    };
    static final String[] cityTypeArray={
            "風景","住宿"
    };
    private View.OnClickListener btn_searchDB_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(edtxt_searchInput.getText().toString().matches("")){
                bundle.putString("EDITTXT_SEARCH_INPUT","");
            }else
                bundle.putString("EDITTXT_SEARCH_INPUT",edtxt_searchInput.getText().toString());
            new C_GetDataFromDatabase(new Interface_AsyncGetDBTask(){
                @Override
//                public void GetDBTaskFinish(int ID, String Name, String cityNumber, String address, Double Lcation_lat, Double Lcation_long, int arraysize){
                    public void GetDBTaskFinish(String Name, String address, String Toldescribe ,Double Lcation_lat, Double Lcation_long){
                    database_Name.add(Name.trim());
                    database_address.add(address.trim());
                    mySpotToldescribe.add(Toldescribe);
                    database_lat.add(Lcation_lat);
                    database_long.add(Lcation_long);
//                    txt_searchCount.setText(database_Name.size()+" 筆結果");
                    InitRecyclerView();
                }
            }).execute(bundle);
            if (database_Name.size()>0 || database_address.size()>0 ){   // 如果有上一筆資料 即刪除
                database_Name.clear();
                database_address.clear();
                mySpotToldescribe.clear();
                database_lat.clear();
                database_long.clear();
            }
        }
    };
    private View.OnClickListener btn_addTravel_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Toast toast = Toast.makeText(getContext(),"ok",Toast.LENGTH_LONG);
            toast.show();
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        inflatedView_Search = inflater.inflate(R.layout.frg_search,container,false);

        btn_searchDB = inflatedView_Search.findViewById(R.id.btn_searchDB);
        btn_searchDB.setOnClickListener(btn_searchDB_click);
        edtxt_searchInput = inflatedView_Search.findViewById(R.id.edtxt_searchInput);

        InitialCitySpinner();
        InitialDatabase();
        return inflatedView_Search;
    }

    Spinner spinner_cityName,spinner_spotType;
    TextView txt_searchCount;
    Button btn_searchDB;
    Bundle bundle = new Bundle();
    private void InitialCitySpinner() {

        spinner_cityName = (Spinner)inflatedView_Search.findViewById(R.id.spinner_cityName);
        ArrayAdapter<String> citylist = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                cityNameArray);
        spinner_cityName.setAdapter(citylist);
        spinner_cityName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getItemName = parent.getItemAtPosition(position).toString();
//                getSelectedName(getItemName);
                // bundle  <<--- 放 getCityName
                bundle.putString(C_Dictionary.CITY_NAME_REQUEST,getItemName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinner_spotType = (Spinner)inflatedView_Search.findViewById(R.id.spinner_spotType);
        ArrayAdapter<String> cityTypelist = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                cityTypeArray);
        spinner_spotType.setAdapter(cityTypelist);
        spinner_spotType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String getItemType = parent.getItemAtPosition(position).toString();
                getSelectedType(parent.getItemAtPosition(position).toString());
                // bundle.put  <<--- 放 getCityType
                bundle.putString(C_Dictionary.SPOT_TYPE_REQUEST, spotType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private String getSelectedType(String getItemType) {
        switch(getItemType){
            case "風景":
                spotType = C_Dictionary.SPOT_TYPE_VIEW;
                break;
            case "住宿":
                spotType = C_Dictionary.SPOT_TYPE_HOTEL;
                break;

        }
        return spotType;
    }

    private  void InitialDatabase(){

    }
    EditText edtxt_searchInput;
    //  RecyclerView : 大樓 , RecycleViewAdapter : 管理員 , ArrayList 載入的資料 : 住戶
    C_SearchRecycleViewAdapter adapter;
    private void InitRecyclerView(){  // 資料載入後才呼叫 RecyclerView 的相關設定
        Log.i(TAG, "InitRecyclerView: init recyclerview");
        RecyclerView recyclerView = inflatedView_Search.findViewById(R.id.recycle_view_search);  // 放在這個 Acticity 的 XML 下的 RecyclerView.ID  recycle_view_search
        adapter = new C_SearchRecycleViewAdapter(getActivity(), database_Name, database_address, mySpotToldescribe, database_lat, database_long);  // 建立 Adapter 來載入資料  // 用 this CLASS 建立 Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));  // recyclerView.setLayoutManager(LayoutManager layoutManager)  // ( Context context, int orientation, boolean reverseLayout)
//        recyclerView.setOnItemClickListener();
    }
}
