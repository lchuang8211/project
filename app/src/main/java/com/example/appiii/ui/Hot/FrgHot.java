package com.example.appiii.ui.Hot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appiii.R;

public class FrgHot extends Fragment {
    View inflatedView;
    private View.OnClickListener btn_home_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            txt_home.setText("1562156");
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.frg_hot,container,false);
        InitialComponent();
        return inflatedView;
    }

    private void InitialComponent() {
        btn_home = inflatedView.findViewById(R.id.btn_hot);
        btn_home.setOnClickListener(btn_home_click);
        txt_home = inflatedView.findViewById(R.id.txt_hotspot);
    }

    Button btn_home;
    TextView txt_home;
}
