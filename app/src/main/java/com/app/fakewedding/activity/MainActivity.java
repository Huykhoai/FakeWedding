package com.app.fakewedding.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.app.fakewedding.R;
import com.app.fakewedding.databinding.ActivityMainBinding;
import com.app.fakewedding.until.InternetReceiver;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        broadcastReceiver= new InternetReceiver();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

}