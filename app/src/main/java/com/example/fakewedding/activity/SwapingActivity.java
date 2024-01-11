package com.example.fakewedding.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.fakewedding.R;
import com.example.fakewedding.databinding.ActivitySwapingBinding;
import com.example.fakewedding.databinding.FragmentStartSwapFaceBinding;
import com.example.fakewedding.dialog.MyDialog;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.util.ArrayList;
import java.util.List;

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

    private String maleName = "";
    private String femaleName = "";
    private MainActivity mainActivity;
    private MyDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySwapingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadIdUser();
        navUploadActivity();
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
                    Toast.makeText(mainActivity, "Fail to recognize face", Toast.LENGTH_SHORT).show();
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
    private void navUploadActivity() {
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
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode()== REQUEST_CODE){
                        Intent intent = o.getData();
                        if(intent!= null){
                            Bundle bundle = intent.getExtras();
                            Uri selectedImage = Uri.parse(bundle.getString("selected_image"));
                            if(checkClickSetImageMale){
                                binding.imageswap1.setImageURI(selectedImage);
                            }else {
                                binding.imageswap2.setImageURI(selectedImage);
                            }

                        }
                    }

                }
            });
}