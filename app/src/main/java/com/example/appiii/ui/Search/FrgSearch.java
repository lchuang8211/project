package com.example.appiii.ui.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appiii.R;

public class FrgSearch extends Fragment {
    View inflatedView_Search;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView_Search = inflater.inflate(R.layout.frg_search,container,false);
        InitialComponent();
        return inflatedView_Search;
    }

    private void InitialComponent() {
    }
}
