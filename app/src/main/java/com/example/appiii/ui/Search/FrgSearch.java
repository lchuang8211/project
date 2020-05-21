package com.example.appiii.ui.Search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.C_Dictionary;
import com.example.appiii.C_GetDataFromDatabase;
import com.example.appiii.C_RecycleViewAdapter;
import com.example.appiii.Interface_AsyncGetDBTask;
import com.example.appiii.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FrgSearch extends Fragment {
    View inflatedView_Search;
    private ArrayList<Integer> database_ID = new ArrayList<>();
    private ArrayList<String> database_Name = new ArrayList<>();
    private ArrayList<String> database_address = new ArrayList<>();
    private ArrayList<Double> database_lat = new ArrayList<>();
    private ArrayList<Double> database_long = new ArrayList<>();
    private ArrayList<String> database_cityNumber = new ArrayList<>();
    private String cityName = "" ;
    private int cityNameNumber = -1 ;
    private int cityType = -1;
    static final String[] cityNameArray = {
            "基隆市","台北市","新北縣","宜蘭縣","桃園市",
            "新竹縣","苗栗縣","台中市","彰化縣","南投縣",
            "雲林縣","嘉義縣","台南市","高雄市","屏東縣",
            "台東縣","花蓮縣","澎湖縣","金門縣"
    };
    private View.OnClickListener btn_searchDB_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
//            new C_GetDataFromDatabase(new Interface_AsyncGetDBTask(){
//                @Override
//                public void GetDBTaskFinish(int ID, String Name, String cityNumber, String address, Double Lcation_lat, Double Lcation_long, int arraysize){
//                    for(int i = 0 ; i < arraysize ; i++) {
//                        database_ID.add(ID);
//                        database_Name.add(Name);
//                        database_cityNumber.add(cityNumber);
//                        database_address.add(address);
//                        database_lat.add(Lcation_lat);
//                        database_long.add(Lcation_long);
//                    }
//                    InitRecyclerView();
//                }
//            }).execute();
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView_Search = inflater.inflate(R.layout.frg_search,container,false);
        txt_cityNumber = inflatedView_Search.findViewById(R.id.txt_cityNumber);
        btn_searchDB = inflatedView_Search.findViewById(R.id.btn_searchDB);
        btn_searchDB.setOnClickListener(btn_searchDB_click);

        InitialCitySpinner();
        InitialDatabase();
        return inflatedView_Search;
    }
    Spinner spinner_cityName,spinner_spotType;
    TextView txt_cityNumber;
    Button btn_searchDB;
    private void InitialCitySpinner() {
        spinner_cityName = (Spinner)inflatedView_Search.findViewById(R.id.spinner_cityName);
        spinner_spotType = (Spinner)inflatedView_Search.findViewById(R.id.spinner_spotType);
        ArrayAdapter<String> citylist = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                cityNameArray);
        spinner_cityName.setAdapter(citylist);
        spinner_cityName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getItemName = parent.getItemAtPosition(position).toString();
                cityName = getItemName;
                getselectNumber(getItemName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinner_spotType = (Spinner)inflatedView_Search.findViewById(R.id.spinner_spotType);
        ArrayAdapter<String> cityTypelist = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                cityType);
        spinner_spotType.setAdapter(cityTypelist);
        spinner_spotType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getItemType = parent.getItemAtPosition(position).toString();
                getselectType(getItemType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private void getselectType(String getItemType) {
        switch(getItemType){
            case "住宿":
                cityType = C_Dictionary.SPOT_TYPE_HOTEL ;
                break;
            case "景點":
                cityType = C_Dictionary.SPOT_TYPE_VIEW ;
                break;

        }
    }

    private void getselectNumber(String itemName) {
        switch (itemName){
            case "基隆市":
                cityNameNumber = C_Dictionary.CITY_NUMBER_KEELUNG;
                break;
            case "台北市":
                cityNameNumber = C_Dictionary.CITY_NUMBER_TAIPEI;
                break;
            case "新北縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_NEW_TAIPEI;
                break;
            case "宜蘭縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_YILAN;
                break;
            case "桃園市":
                cityNameNumber = C_Dictionary.CITY_NUMBER_TAOYUAN;
                break;
            case "新竹縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_HSINCHU;
                break;
            case "苗栗縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_MIAOLI;
                break;
            case "台中市":
                cityNameNumber = C_Dictionary.CITY_NUMBER_TAICHUNG;
                break;
            case "彰化縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_CHANGHUA;
                break;
            case "南投縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_NANTOU;
                break;
            case "雲林縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_YUNLIN;
                break;
            case "嘉義縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_CHIAYI;
                break;
            case "台南市":
                cityNameNumber = C_Dictionary.CITY_NUMBER_TAINAN;
                break;
            case "高雄市":
                cityNameNumber = C_Dictionary.CITY_NUMBER_KAOHSIUNG;
                break;
            case "屏東縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_PINGTUNG;
                break;
            case "台東縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_TAITUNG;
                break;
            case "花蓮縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_HUALIEN;
                break;
            case "澎湖縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_PENGHU;
                break;
            case "金門縣":
                cityNameNumber = C_Dictionary.CITY_NUMBER_KINMEN;
                break;
        }
    }


    private  void InitialDatabase(){   // 人為載入資料庫
        Log.i(TAG, "initialImageUrl: preparing bitmaps");
        database_Name.add("第一個景點");
        database_address.add("高雄市");
        database_Name.add("第二個景點");
        database_address.add("台南市");
        database_Name.add("第三個景點");
        database_address.add("南投縣");
        database_Name.add("第四個景點");
        database_address.add("花蓮縣");
        database_Name.add("第五個景點");
        database_address.add("嘉義縣");
        database_Name.add("第六個景點");
        database_address.add("新竹縣");

        InitRecyclerView();
    }
    //  RecyclerView : 大樓 , RecycleViewAdapter : 管理員 , ArrayList 載入的資料 : 住戶
    private void InitRecyclerView(){  // 資料載入後才呼叫 RecyclerView 的相關設定
        Log.i(TAG, "InitRecyclerView: init recyclerview");
        RecyclerView recyclerView = inflatedView_Search.findViewById(R.id.recycle_view_search);  // 放在這個 Acticity 的 XML 下的 RecyclerView.ID  recycle_view_search
        C_SearchRecycleViewAdapter adapter = new C_SearchRecycleViewAdapter(getActivity(), database_Name, database_address);  // 建立 Adapter 來載入資料  // 用 this CLASS 建立 Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));  // recyclerView.setLayoutManager(LayoutManager layoutManager)  // ( Context context, int orientation, boolean reverseLayout)
//        recyclerView.setOnItemClickListener();
    }
}
