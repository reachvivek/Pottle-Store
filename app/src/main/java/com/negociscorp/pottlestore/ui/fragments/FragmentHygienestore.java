package com.negociscorp.pottlestore.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.negociscorp.pottlestore.R;

/**
 * Code written by Vivek Kumar Singh on 11/07/2020.
 */

public class FragmentHygienestore extends Fragment {

    View v;

    public FragmentHygienestore() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.hygienestore_fragment, container, false);
        return v;
    }
}
