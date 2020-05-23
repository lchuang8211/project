package com.example.appiii.ui.Member;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;

public class FrgmMember extends Fragment {
    View inflatedView_Member;
    Bundle budle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView_Member = inflater.inflate(R.layout.frg_member,container,false);
        budle = this.getArguments();
        InitialComponent();
        switch(budle.getString(C_Dictionary.USER_STATUS)){
            case C_Dictionary.USER_STATUS_MEMBER:
                memberStatus.setText("你已是會員");
                break;
            case C_Dictionary.USER_STATUS_VISITORS:
                memberStatus.setText("你還不是會員");
                break;

        }
        return inflatedView_Member;
    }

    private void InitialComponent() {
        memberStatus = inflatedView_Member.findViewById(R.id.memberStatus);



    }
    TextView memberStatus;
}
