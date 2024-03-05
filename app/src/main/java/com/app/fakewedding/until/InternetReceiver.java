package com.app.fakewedding.until;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class InternetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
     String status = checkInternet.getNetworkInfo(context);
     if(status.equals("connected")){
         Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
     }else if(status.equals("disconnected")){
         Toast.makeText(context, "No connected Internet" , Toast.LENGTH_SHORT).show();
     }
    }
}
