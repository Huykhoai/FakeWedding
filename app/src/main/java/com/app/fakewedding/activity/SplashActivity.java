package com.app.fakewedding.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.app.fakewedding.R;
import com.app.fakewedding.api.RetrofitClient;
import com.app.fakewedding.databinding.ActivitySplashBinding;
import com.app.fakewedding.model.ApiGlossary;
import com.app.fakewedding.model.Glossary;
import com.app.fakewedding.server.ApiServer;
import com.app.fakewedding.server.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    String urlAds = "";
    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ads();
    }
    private void ads(){
        ApiServer apiServer = RetrofitClient.getInstance(Server.UrlAds).getRetrofit().create(ApiServer.class);
        Call<ApiGlossary> call = apiServer.getGlossary();
        call.enqueue(new Callback<ApiGlossary>() {
            @Override
            public void onResponse(Call<ApiGlossary> call, Response<ApiGlossary> response) {
                if(response.isSuccessful() && response.body() != null){
                    Glossary glossary = response.body().getGlossary();

                    urlAds = glossary.getLink();
                    Log.d("Huy", "onResponse: "+urlAds);
                    if (urlAds != null && !urlAds.isEmpty()) {
                        Intent intent = new Intent(SplashActivity.this, WedViewActivity.class);
                        intent.putExtra("URL", urlAds);
                        startActivity(intent);
                        finish();
                    }else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiGlossary> call, Throwable t) {

            }
        });
    }
}