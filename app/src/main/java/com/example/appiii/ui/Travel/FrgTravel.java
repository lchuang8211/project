package com.example.appiii.ui.Travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appiii.R;

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
