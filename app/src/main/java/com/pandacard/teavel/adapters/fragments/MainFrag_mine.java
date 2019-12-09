package com.pandacard.teavel.adapters.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pandacard.teavel.R;


public class MainFrag_mine extends Fragment {
    private static String TAG = "MainFrag_mine";


    public MainFrag_mine() {
    }

    public static MainFrag_mine newInstance() {
        MainFrag_mine fragment = new MainFrag_mine();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_frag_mine, container, false);
    }




    @Override
    public void onDetach() {
        super.onDetach();

    }

}
