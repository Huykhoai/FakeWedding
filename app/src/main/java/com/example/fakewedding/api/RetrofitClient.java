package com.example.fakewedding.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance;
    private static Retrofit retrofit;

    private RetrofitClient(String domain){
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request =chain.request();
                Request.Builder builder =request.newBuilder();
                return chain.proceed(builder.build());
            }
        };
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
         okBuilder.addInterceptor(interceptor)
                 .callTimeout(200000, TimeUnit.MILLISECONDS)
                 .connectTimeout(200000, TimeUnit.MILLISECONDS)
                 .readTimeout(200000,TimeUnit.MILLISECONDS);

        Gson gson =new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(domain)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okBuilder.build())
                .build();

    }
    public static synchronized RetrofitClient getInstance(String domain){
        if(instance==null){
            instance = new RetrofitClient(domain);
        }
        return instance;
    }

    public  Retrofit getRetrofit() {
        return retrofit;
    }

}
