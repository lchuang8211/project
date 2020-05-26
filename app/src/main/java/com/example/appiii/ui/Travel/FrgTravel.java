package com.example.appiii.ui.Travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appiii.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FrgTravel extends Fragment {
    View inflatedView;
    private View.OnClickListener btn_addPlan_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ActAddTravelPlan.class);
            startActivity(intent);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.frg_travel,container,false);
        InitialComponent();
        return inflatedView;
    }

    private void InitialComponent() {
        fab = inflatedView.findViewById(R.id.fab);
        btn_addPlan = inflatedView.findViewById(R.id.btn_addPlan);
        btn_addPlan.setOnClickListener(btn_addPlan_click);
    }
    FloatingActionButton fab;
    Button btn_addPlan;
}
