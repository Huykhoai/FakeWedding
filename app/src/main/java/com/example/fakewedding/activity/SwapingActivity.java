package com.example.fakewedding.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.fakewedding.R;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.ActivitySwapingBinding;
import com.example.fakewedding.databinding.FragmentStartSwapFaceBinding;
import com.example.fakewedding.dialog.MyDialog;
import com.example.fakewedding.server.ApiServer;
import com.example.fakewedding.server.Server;
import com.example.fakewedding.until.Util;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SwapingActivity extends AppCompatActivity {
    ActivitySwapingBinding binding;
    private static final String NO_FACE_DETECTED = "No faces detected";
    private static final String MORE_THAN_ONE_FACE = "More than one face is recognized";
    private static final int REQUEST_CODE = 1;
    private String resultDetech;
    private String imgBase64Male;
    private boolean checkClickSetImageMale;
    private boolean isCheckSetImageFemale = false;
    private boolean isCheckSetImageMale = false;
    int id_user ;
    private String imgBase64Female;
    private String urlImageMale;
    private String urlImageFemale;
    String uriResponse;
    File imageFile;
    Uri selectedImage;
    private MyDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySwapingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadIdUser();
        initListener();
    }
    private void loadIdUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user", "");
        Log.d("check_user_id", "loadIdUser: "+ id_user_str);
        if (id_user_str == "") {
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }

    private String detectionFace(Bitmap bitmap){
        resultDetech ="";
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setMinFaceSize(0.15f)
                        .enableTracking()
                        .build();
        FaceDetector faceDetector = FaceDetection.getClient(options);

        //tạo đối tượng InputImage từ bitmap
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        //nhận diện khuôn mặt từ ảnh
        Task<List<Face>> result = faceDetector.process(image)
                .addOnSuccessListener(faces -> {
                    resultDetech = processFaceDetectionResult(faces);
                    if (!resultDetech.equals("ok")) {
                        myDialog = getDialog();
                        myDialog.setTitle("Can not process the face recognition");
                        myDialog.setContent(resultDetech);
                        myDialog.setContentButton("OK");
                        myDialog.show();
                    } else {
                        if (!checkClickSetImageMale) {
                            binding.imageswap1.setImageBitmap(bitmap);
                        } else {
                            binding.imageswap2.setImageBitmap(bitmap);
                        }
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SwapingActivity.this, "Fail to recognize face", Toast.LENGTH_SHORT).show();
                });
        return resultDetech;
    }
    private MyDialog getDialog(){
        if(myDialog == null){
            myDialog = MyDialog.getInstance(SwapingActivity.this);
            Window window =myDialog.getWindow();
            if(window!= null){
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.CENTER;
                params.y= 300;//đặt
            }
        }
        return myDialog;
    }
    private String processFaceDetectionResult(List<Face> faces) {
        String result = "";
        List<Face> faceList = new ArrayList<>();
        for(Face face: faces){
            Rect bounds =face.getBoundingBox();
            if(bounds.width()>=150|| bounds.height()>=150){
                faceList.add(face);
            }
        }
        if(faceList.size()==0){
            return NO_FACE_DETECTED;
        }
        if(faceList.size()>1){
            return MORE_THAN_ONE_FACE;
        }
        Face face = faceList.get(0);
        float rotY = face.getHeadEulerAngleY();
        float rotZ = face.getHeadEulerAngleZ();

        if(rotY>30 || rotZ>30|| rotZ< -30 || rotY< -30){
            return  "the photo is tilted or because there are not enough eyes, nose, mouth";
        }
        FaceLandmark leftEye = face.getLandmark(FaceLandmark.LEFT_EYE);
        FaceLandmark rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE);
        FaceLandmark nose = face.getLandmark(FaceLandmark.NOSE_BASE);
        FaceLandmark mouth = face.getLandmark(FaceLandmark.MOUTH_BOTTOM);
        FaceLandmark cheekRight = face.getLandmark(FaceLandmark.RIGHT_CHEEK);
        FaceLandmark cheekLeft = face.getLandmark(FaceLandmark.LEFT_CHEEK);
        if(leftEye == null || rightEye == null || nose == null || mouth == null || cheekLeft == null || cheekRight == null){
            return "the picture is too blurry or because there are not enough eyes, nose, mouth";
        }
        return result;
    }
    private void initListener() {
        binding.btnswap1.setOnClickListener(v -> {
            checkClickSetImageMale =true;
            Intent intent = new Intent(SwapingActivity.this, UploadActivity.class);
            launcher.launch(intent);
        });

        binding.btnswap2.setOnClickListener(v -> {
            checkClickSetImageMale= false;
            Intent intent = new Intent(SwapingActivity.this, UploadActivity.class);
            launcher.launch(intent);
        });
        binding.btnGenerate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
//                if (!isCheckSetImageFemale || !isCheckSetImageMale) {
//                    myDialog = getDialog();
//                    myDialog.setTitle("Can not Face recognition");
//                    myDialog.setContent("Not enough faces have been identified");
//                    myDialog.setContentButton("Ok");
//                    myDialog.show();
//                }else {
//                    Toast.makeText(SwapingActivity.this, "Please waiting a time!", Toast.LENGTH_SHORT).show();
//                    lockBtnSelectImage();
//                    new AsyncTask<Void, Void, Void>() {
//                        @Override
//                        protected Void doInBackground(Void... voids) {
//                            urlImageMale = Util.uploadImage2(imgBase64Male,SwapingActivity.this);
//                            urlImageFemale = Util.uploadImage2(imgBase64Female,SwapingActivity.this);
//                            Log.d("Huy", "Male Image URL: " + urlImageMale + "\nFemale Image URL: " + urlImageFemale);
//                            return null;
//                        }
//                    };
//                }
            }
        });
    }
   private void getData(){
        String filePath =getRealPathFromURI(SwapingActivity.this,selectedImage);
        imageFile =new File(filePath);
       Log.d("check_upload_image", "getData_0: "+ imageFile);
       RequestBody requestBody =RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);
       MultipartBody.Part imagePart =MultipartBody.Part.createFormData("src_img",imageFile.getName(),requestBody);
       ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiServer.class);
       Log.d("check_upload_image", "getData: "+ id_user + imagePart);
       if(checkClickSetImageMale){
           Call<String>call = apiServer.uploadImage(id_user, "src_nam", imagePart);
           call.enqueue(new Callback<String>() {
               @Override
               public void onResponse(Call<String> call, Response<String> response) {
                  if(response.isSuccessful()){
                      Log.d("check_upload_image", "onResponse: "+ response.body());
                        uriResponse = response.body();
                        binding.imageswap1.setImageURI(selectedImage);
                  }
               }

               @Override
               public void onFailure(Call<String> call, Throwable t) {
                   Log.d("check_upload_image", "onFailure: "+ t.getMessage());
               }
           });
       }else {
           Call<String>call = apiServer.uploadImage(id_user, "src_nu", imagePart);
           call.enqueue(new Callback<String>() {
               @Override
               public void onResponse(Call<String> call, Response<String> response) {
                   if(response.isSuccessful()){
                       Log.d("check_upload_image", "onResponse: "+ response.body());
                       uriResponse = response.body();
                       binding.imageswap2.setImageURI(selectedImage);
                   }
               }

               @Override
               public void onFailure(Call<String> call, Throwable t) {
                   Log.d("check_upload_image", "onFailure: "+ t.getMessage());
               }
           });
       }


   }
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

        if (cursor == null) {
            return null;
        }

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath;
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == REQUEST_CODE) {
                        try {
                            Intent intent = o.getData();
                            if (intent != null) {
                                Bundle bundle = intent.getExtras();
                                 selectedImage = Uri.parse(bundle.getString("selected_image"));
                                 getData();
                                 Bitmap bitmap;

                                bitmap = MediaStore.Images.Media.getBitmap(SwapingActivity.this.getContentResolver(), selectedImage);
                                Log.d("Huybimap", "onActivityResult: "+bitmap);
                                if (checkClickSetImageMale) {
                                    detectionFace(bitmap);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (resultDetech != null && resultDetech.equals("ok")) {
                                                try {
                                                    imgBase64Male = Util.convertBitmapToBase64(bitmap);
                                                } catch (IOException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                isCheckSetImageMale = true;
                                            } else {
                                                isCheckSetImageMale = false;
                                            }
                                        }
                                    }, 4000);
                                } else {
                                    detectionFace(bitmap);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (resultDetech != null && resultDetech.equals("ok")) {
                                                try {
                                                    imgBase64Female = Util.convertBitmapToBase64(bitmap);
                                                } catch (IOException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                isCheckSetImageFemale = true;
                                            } else {
                                                isCheckSetImageFemale = false;
                                            }
                                        }
                                    }, 4000);
                                }

                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
    private void lockBtnSelectImage() {
        binding.btnswap1.setEnabled(false);
        binding.btnswap2.setEnabled(false);
        binding.btnGenerate.setEnabled(false);
    }
}