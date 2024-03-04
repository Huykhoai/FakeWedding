package com.example.fakewedding.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fakewedding.R;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.ActivitySwapVideoBinding;
import com.example.fakewedding.dialog.MyDialog;
import com.example.fakewedding.model.SukienVideoResponse;
import com.example.fakewedding.model.SwapVideo;
import com.example.fakewedding.model.Temple;
import com.example.fakewedding.server.ApiServer;
import com.example.fakewedding.server.Server;
import com.example.fakewedding.until.RealPathUtil;
import com.example.fakewedding.until.Util;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;
import com.squareup.picasso.Picasso;

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

public class SwapVideoActivity extends AppCompatActivity {
   int idVideo;
    ActivitySwapVideoBinding binding;
    private static final String NO_FACE_DETECTED = "No faces detected";
    private static final String MORE_THAN_ONE_FACE = "More than one face is recognized";
    private static final int REQUEST_CODE_SELECT = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    private String resultDetech;
    private String imgBase64Male;
    private boolean checkClickSetImageMale;
    private boolean isCheckSetImageFemale = false;
    private boolean isCheckSetImageMale = false;
    int id_user ;
    String author;
    Temple temple;
    String uriResponseMale;
    String uriResponseFemale;
    File imageFile;
    Uri selectedImage;
    private MyDialog myDialog;
    String imgBase64Female;
    Dialog dialogUpload;
    Dialog dialogSwap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySwapVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadIdUser();
        getIdTempleVideo();
        btnSwapVideo();
    }
    private void loadIdUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str", "");
        author = sharedPreferences.getString("token", "");
        Log.d("check_user_author", "loadIdUser: "+ author);
        Log.d("check_user_id", "loadIdUser: "+ id_user_str);
        if (id_user_str == "") {
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }

    private void btnSwapVideo() {
        binding.btnswapvideo.setOnClickListener(v -> {
                Intent intent = new Intent(SwapVideoActivity.this, UploadActivity.class);
                intent.putExtra("male", true);
                launcher.launch(intent);
        });
       binding.btnGenerateVideo.setOnClickListener(v -> {
           if (!isCheckSetImageMale ) {
               myDialog = getDialog();
               myDialog.setTitle("Can not Face recognition");
               myDialog.setContent("Not enough faces have been identified");
               myDialog.setContentButton("Ok");
               myDialog.show();
           }else {
               lockBtnSelectImage();
               swapVideo();
               dialogSwap= DialogUpload();
               dialogSwap.show();
           }
       });
    }
    private void swapVideo(){
        ApiServer apiServer = RetrofitClient.getInstance("").getRetrofit().create(ApiServer.class);
        Log.d("Huy", "swapVideo: "+author+"+"+ uriResponseMale+"+"+id_user+"+"+idVideo);
        Call<SukienVideoResponse> call = apiServer.SwapVideo("Bearer "+author,"fsfdsfs",123,id_user, uriResponseMale,idVideo);
        call.enqueue(new Callback<SukienVideoResponse>() {
            @Override
            public void onResponse(Call<SukienVideoResponse> call, Response<SukienVideoResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d("Huy", "onResponse: "+response.body());
                    SwapVideo sukienVideo = response.body().getSukienVideo();
                    Intent intent = new Intent(SwapVideoActivity.this, ResultVideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sukienVideo",sukienVideo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    dialogSwap.dismiss();
                    binding.videoswap.setImageResource(R.drawable.imageswap);
                }else {
                    Log.d("Huy", "Error: "+response.toString());
                }
            }

            @Override
            public void onFailure(Call<SukienVideoResponse> call, Throwable t) {
                Log.d("HuyFail", "onFailure: "+t.getMessage());
            }
        });
    }
    private void getIdTempleVideo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        idVideo = Integer.parseInt(bundle.getString("id_temple_video"));
        Log.d("Huy", "getIdTempleVideo: "+idVideo);
    }
    private void getData(Uri imageselected){
        Log.d("UriimageSelected", "getData: "+imageselected);
        String filePath ="";
        if(Build.VERSION.SDK_INT<11){
            filePath = RealPathUtil.getRealPathFromURI_BelowAPI11(this,imageselected);
        }else if(Build.VERSION.SDK_INT<19){
            filePath = RealPathUtil.getRealPathFromURI_API11to18(this,imageselected);
        }else {
            filePath = RealPathUtil.getRealPathFromURI_API19(this,imageselected);
        }
        imageFile =new File(filePath);
        Log.d("check_upload_image", "getData_0: "+ imageFile);
        RequestBody requestBody =RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart =MultipartBody.Part.createFormData("src_img",imageFile.getName(),requestBody);
        ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiServer.class);
        Log.d("check_upload_image", "getData: "+ id_user + imagePart);
            Call<String> call = apiServer.uploadImage(id_user, "src_nam", imagePart);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        Log.d("Huy", "onResponse_: "+ response.body());
                        uriResponseMale = response.body();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.btnswapvideo.setEnabled(true);
                                dialogUpload.dismiss();
                            }
                        },2000);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("check_upload_image", "onFailure: "+ t.getMessage());
                }
            });
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == REQUEST_CODE_SELECT) {
                        try {
                            Intent intent = o.getData();
                            if (intent != null) {
                                Bundle bundle = intent.getExtras();
                                String filePath = bundle.getString("selected_image");
                                selectedImage = Uri.parse(filePath);
                                Log.d("selectedImage", "ResultSelected: "+selectedImage);
                                Bitmap bitmap;
                                bitmap = MediaStore.Images.Media.getBitmap(SwapVideoActivity.this.getContentResolver(), selectedImage);
                                Log.d("Huybimap", "onActivityResult: "+bitmap);
                                    detectionFace(bitmap);
                                    dialogUpload = DialogUpload();
                                    lockBtnSelectImage();
                                    dialogUpload.show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("result", "detectionFace: "+resultDetech);
                                            if (resultDetech != null && resultDetech.equals("ok")) {
                                                try {
                                                    imgBase64Male = Util.convertBitmapToBase64(bitmap);
                                                    getData(selectedImage);
                                                } catch (IOException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                isCheckSetImageMale = true;
                                            } else {
                                                isCheckSetImageMale = false;
                                            }

                                        }
                                    }, 1500);
                                }


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else if(o.getResultCode()==REQUEST_CODE_CAMERA){
                        Intent intent = o.getData();
                        if (intent != null) {
                            Bundle bundle = intent.getExtras();
                            selectedImage = Uri.parse(bundle.getString("camera_image"));
                            Log.d("huy", "imageCamera: "+selectedImage);
                            Bitmap bitmap =rotaImageHadlee(String.valueOf(selectedImage));
                                detectionFace(bitmap);
                                dialogUpload = DialogUpload();
                                lockBtnSelectImage();
                                dialogUpload.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("result", "detectionFace: " + resultDetech);
                                        if (resultDetech != null && resultDetech.equals("ok")) {
                                            try {
                                                imgBase64Male = Util.convertBitmapToBase64(bitmap);

                                                getData(selectedImage);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            isCheckSetImageMale = true;
                                        } else {
                                            isCheckSetImageMale = false;
                                        }
                                    }
                                },1500);
                            }
                    }else if(o.getResultCode()==3){
                        Intent intent = o.getData();
                        if (intent != null) {
                            Bundle bundle = intent.getExtras();
                            selectedImage = Uri.parse(bundle.getString("image_uploaded"));
                            Log.d("selectedImage", "ResultSelected: "+selectedImage);
                            String strimage = selectedImage.toString();
                            uriResponseMale = strimage.replace("https://futurelove.online","/var/www/build_futurelove");
                            Log.d("HuyuriResponse", "onActivityResult: "+uriResponseMale);
                            Picasso.get().load(selectedImage).into(binding.videoswap);
                            isCheckSetImageMale = true;

                        }
                    }
                }
            });
    private Dialog DialogUpload(){
        Dialog dialog = new Dialog(SwapVideoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress_upload);
        Window window = dialog.getWindow();
        if(window == null){
            return null;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 300 );
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        LottieAnimationView lottie = dialog.findViewById(R.id.lotteAnimationView);
        lottie.playAnimation();
        return dialog;
    }
    private String detectionFace(Bitmap bitmap) {
        resultDetech = "";
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setMinFaceSize(0.15f)
                        .enableTracking()
                        .build();

        FaceDetector faceDetector = FaceDetection.getClient(options);

        InputImage image = InputImage.fromBitmap(bitmap, 0);

        // Nhận dạng khuôn mặt từ ảnh
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
                       binding.videoswap.setImageBitmap(bitmap);
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(SwapVideoActivity.this, "Fail to recognize face", Toast.LENGTH_SHORT).show();
                });

        return resultDetech;
    }
    private MyDialog getDialog(){
        if(myDialog == null){
            myDialog = MyDialog.getInstance(SwapVideoActivity.this);
            Window window =myDialog.getWindow();
            if(window!= null){
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.CENTER;
                params.y= 300;//đặt
                window.setAttributes(params);
            }
        }
        return myDialog;
    }
    private String processFaceDetectionResult(List<Face> faces) {
        String result = "ok";
        List<Face> faceList = new ArrayList<>();

        for (Face face : faces) {
            Rect bounds = face.getBoundingBox();
            if (bounds.width() >= 150 || bounds.height() >= 150) {
                faceList.add(face);
            }
        }
        if (faceList.size() == 0) {
            return NO_FACE_DETECTED;
        }
        if (faceList.size() > 1) {
            return MORE_THAN_ONE_FACE;
        }
//


        Face face = faceList.get(0);
//        Rect bounds = face.getBoundingBox();
//        if (bounds.width() <= 30 || bounds.height() <= 30) {
//            return "face size is too small ";
//        }

        float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
        float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
        if (rotY > 30 || rotZ > 30 || rotZ < -30 || rotY < -30
        ) {
            return "the photo is tilted or because there are not enough eyes, nose, mouth";
        }
        FaceLandmark leftEye = face.getLandmark(FaceLandmark.LEFT_EYE);
        FaceLandmark rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE);
        FaceLandmark nose = face.getLandmark(FaceLandmark.NOSE_BASE);
        FaceLandmark mouth = face.getLandmark(FaceLandmark.MOUTH_BOTTOM);
        FaceLandmark cheekRight = face.getLandmark(FaceLandmark.RIGHT_CHEEK);
        FaceLandmark cheekLeft = face.getLandmark(FaceLandmark.LEFT_CHEEK);
        if (leftEye == null || rightEye == null || nose == null || mouth == null || cheekLeft == null || cheekRight == null) {
            return "the picture is too blurry or because there are not enough eyes, nose, mouth";
        }
        return result;
    }

    private Bitmap rotaImageHadlee(String path) {
        Bitmap bitmap;
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        // Tạo ma trận xoay để hiển thị ảnh đúng hướng
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                // Không xoay ảnh
                break;
        }

// Đọc ảnh từ đường dẫn và áp dụng ma trận xoay (nếu có)
        bitmap = BitmapFactory.decodeFile(path);
        Bitmap photo = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return photo;
    }
    private void lockBtnSelectImage() {
        binding.btnswapvideo.setEnabled(false);
    }
}