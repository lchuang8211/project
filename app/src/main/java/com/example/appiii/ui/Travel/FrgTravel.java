package com.example.appiii.ui.Travel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.ui.Member.ActAddTravelPlan;
import com.example.appiii.ui.Member.C_MemberRecycleViewAdapter;
import com.example.appiii.ui.Member.C_MySQLite;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FrgTravel extends Fragment {
    View inflatedView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.frg_travel,container,false);
        InitialComponent();

        return inflatedView;
    }

    private void InitialComponent() {

    }

}
