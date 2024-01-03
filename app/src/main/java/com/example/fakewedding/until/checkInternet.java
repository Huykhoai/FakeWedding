package com.example.fakewedding.until;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class checkInternet {
    public static String getNetworkInfo(Context context){
     String status = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info != null){
          status = "connected";
        }else {
          status = "disconnected";
        }
        return status;
    }
}
