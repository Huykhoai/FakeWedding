package com.example.fakewedding.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.fakewedding.R;
import com.example.fakewedding.activity.MainActivity;
import com.example.fakewedding.databinding.FragmentStartSwapFaceBinding;
import com.example.fakewedding.dialog.MyDialog;
import com.example.fakewedding.model.ImageEven;
import com.example.fakewedding.model.ImageViewModel;
import com.example.fakewedding.until.Util;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class StartSwapFaceFragment extends Fragment {
    FragmentStartSwapFaceBinding binding;
    private static final String NO_FACE_DETECTED = "No faces detected";
    private static final String MORE_THAN_ONE_FACE = "More than one face is recognized";
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
    public StartSwapFaceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStartSwapFaceBinding.inflate(inflater,container,false);
        mainActivity = (MainActivity) getActivity();
        navUploadFragment();
        return binding.getRoot();
    }
    private void loadIdUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("id_user",0);
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
            myDialog = MyDialog.getInstance(getActivity());
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

    private void navUploadFragment() {
        binding.btnswap1.setOnClickListener(v -> {
            checkClickSetImageMale =true;
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.action_swapFragment_to_uploadFragment);
        });

        binding.btnswap2.setOnClickListener(v -> {
            checkClickSetImageMale= false;
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.action_swapFragment_to_uploadFragment);
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getImage(ImageEven imageEven){
        Toast.makeText(getActivity(), imageEven.image, Toast.LENGTH_SHORT).show();
        Log.d("UriUpload", "getImage: "+imageEven.image);
        Uri selectedMediaUri = Uri.parse(imageEven.image);
        final InputStream imageStream;
        try {
            imageStream = getActivity().getContentResolver().openInputStream(selectedMediaUri);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        binding.imageswap1.setImageBitmap(selectedImage);
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
//            Log.d("bimap", "getImage: "+bitmap);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Bitmap bitmap;
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedMediaUri);
//            if(selectedMediaUri.toString().contains("image")){
//                if(!checkClickSetImageMale){
//                 detectionFace(bitmap);
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                    if(resultDetech!= null && Objects.equals(resultDetech, "ok")){
//                        try {
//                            imgBase64Female = Util.convertBitmapToBase64(bitmap);
//                            mainActivity.femaleImage = bitmap;
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    isCheckSetImageFemale = true;
//                    }else {
//                      isCheckSetImageFemale = false;
//
//                    }
//                        }
//                    },4000);
//                }else {
//                   detectionFace(bitmap);
//                   Handler handler = new Handler();
//                   handler.postDelayed(new Runnable() {
//                       @Override
//                       public void run() {
//                           if(resultDetech!= null && Objects.equals(resultDetech, "ok")){
//                               try {
//                                   imgBase64Male = Util.convertBitmapToBase64(bitmap);
//                                   mainActivity.maleImage = bitmap;
//                               } catch (IOException e) {
//                                   throw new RuntimeException(e);
//                               }
//                               isCheckSetImageMale = true;
//                           }else {
//                               isCheckSetImageMale = false;
//
//                           }
//                       }
//                   }, 4000);
//                }
//
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
  }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}