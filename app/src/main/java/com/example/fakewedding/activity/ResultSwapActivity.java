package com.example.fakewedding.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.fakewedding.R;
import com.example.fakewedding.adapter.ResultAdapter;
import com.example.fakewedding.databinding.ActivityResultSwapBinding;
import com.example.fakewedding.model.SwapEventInfo;
import com.example.fakewedding.until.DownloadImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResultSwapActivity extends AppCompatActivity {
      ActivityResultSwapBinding binding;
      SwapEventInfo swapEventInfo;
      List<String> listImage;
      ResultAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultSwapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        getResult();
        initButton();

    }

    private void initButton() {
        binding.btndownload.setOnClickListener(v -> {
        DownloadImage.downloadImages(ResultSwapActivity.this,"image",listImage);
    });
        binding.btnCreateNew.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.resultMenu.setOnClickListener(v -> {onBackPressed();});
    }

    private void getResult() {
        String image_goc_nam = swapEventInfo.getLinkSrcGoc();
        String image_goc_nu = swapEventInfo.getLinkTarGoc();
        String image_goc_nam_replace = image_goc_nam.replace("/var/www/build_futurelove","https://futurelove.online");
        String image_goc_nu_replace = image_goc_nu.replace("/var/www/build_futurelove","https://futurelove.online");
        Picasso.get().load(image_goc_nam_replace).into(binding.imageResultMale);
        Picasso.get().load(image_goc_nu_replace).into(binding.imageResultFemale);
        adapter = new ResultAdapter(ResultSwapActivity.this, listImage);
        binding.recycleResult.setLayoutManager(new GridLayoutManager(ResultSwapActivity.this,2));
        binding.recycleResult.setAdapter(adapter);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        swapEventInfo = (SwapEventInfo) bundle.getSerializable("swapeventinfo");
        Log.d("Huy", "getDataResult: "+swapEventInfo.getIdSaved());
        listImage = bundle.getStringArrayList("listimage");
        Log.d("Huy", "listImage: "+listImage.get(0));
    }
}