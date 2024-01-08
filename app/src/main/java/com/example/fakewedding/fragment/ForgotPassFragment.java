package com.example.fakewedding.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fakewedding.R;
import com.example.fakewedding.databinding.FragmentForgotPassBinding;


public class ForgotPassFragment extends Fragment {
    private FragmentForgotPassBinding binding;

    public ForgotPassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPassBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}