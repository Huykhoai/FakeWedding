package com.app.fakewedding.until;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Util {
    public static String convertBitmapToBase64(Bitmap bitmap) throws IOException {

//        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public static String uploadImage2(String imageBase64, Context context) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", Constant.KEY_IMG_BB)
                .addFormDataPart("image", imageBase64)
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(requestBody)
                .build();
        String imageUrl = "";
        try {
            // Thực hiện yêu cầu và lấy phản hồi trả về
            okhttp3.Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            response.close();
            // Trích xuất URL của hình ảnh từ phản hồi JSON của ImgBB
            JSONObject jsonObject = new JSONObject(responseBody);
            imageUrl = jsonObject.getJSONObject("data").getString("url");
            // In ra URL của hình ảnh đã tải lê
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return imageUrl;
    }
    public static long getTimeStampNow() {
        Instant instant = null;
        long timestamp = 0;
        ZonedDateTime zonedDateTime = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            ZoneId zoneId = ZoneId.of("Pacific/Kiritimati");

            zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), zoneId);
            instant = zonedDateTime.toInstant();
            timestamp = instant.toEpochMilli();
        }
        return timestamp;
    }
}
