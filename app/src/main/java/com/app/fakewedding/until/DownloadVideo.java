package com.app.fakewedding.until;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class DownloadVideo {
    public static void downloadVideo(Context context, String filename,String urlVideo){
        try {
            Uri uriVideo = Uri.parse(urlVideo);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uriVideo);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, filename);
            manager.enqueue(request);
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
        }

    }
}
