package com.example.appiii.ui.Hot;

import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.appiii.ActEntrance;
import com.example.appiii.C_Dictionary;
import com.example.appiii.R;

public class FrgHot extends Fragment {
    View inflatedView;
    private View.OnClickListener btn_home_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            txt_home.setText(getUserStatus_home+"btn");
        }
    };
    static String getUserStatus_home;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.frg_hot,container,false);

        Bundle bundle = this.getArguments();


//        Bundle args = getArguments();
        InitialComponent();
        if(bundle!=null){
            getUserStatus_home = bundle.getString(C_Dictionary.USER_STATUS);
            txt_home.setText(getUserStatus_home);
        }else {
            Toast toast = Toast.makeText(getContext(), getUserStatus_home, Toast.LENGTH_LONG);
            toast.show();
        }


//        String myValue = this.getArguments().getString("message");
//        txt_home.setText(myValue);

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
