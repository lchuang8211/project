package com.example.appiii.ui.mMember;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appiii.R;

public class FrgmMember extends Fragment {
    View inflatedView_Member;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView_Member = inflater.inflate(R.layout.frg_mmember,container,false);
        InitialComponent();
        return inflatedView_Member;
    }

    private void InitialComponent() {

    }
}
