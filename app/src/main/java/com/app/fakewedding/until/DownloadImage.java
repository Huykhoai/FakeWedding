package com.app.fakewedding.until;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class DownloadImage {

    public static void downloadImages(Context context, String filenames,List<String> downloadUrls) {
        Log.d("Huy", "downloadImages: "+downloadUrls);
       try {
           for(int i=0;i<downloadUrls.size();i++){
               String namefile = filenames+i;
               Uri urlDownload = Uri.parse(downloadUrls.get(i));
               DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
               DownloadManager.Request request = new DownloadManager.Request(urlDownload);
               request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
               request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
               request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, namefile);
               downloadManager.enqueue(request);

           }
           Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
       }catch (Exception e){
           Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
       }


    }
}
