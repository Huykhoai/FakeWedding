package com.example.fakewedding.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.fakewedding.databinding.DialogBottomBinding;
import com.example.fakewedding.databinding.FragmentUploadBinding;
import com.example.fakewedding.dialog.MyDialog;
import com.example.fakewedding.model.ImageEven;
import com.example.fakewedding.model.ImageViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UploadFragment extends Fragment {
    private static final String NO_FACE_DETECTED = "No faces detected";
    private static final String MORE_THAN_ONE_FACE = "More than one face is recognized";
     private DialogBottomBinding bottomBinding;
     private FragmentUploadBinding binding;
     private BottomSheetDialog bottom;
    private static final int IMAGE_PICKER_SELECT = 1889;
    private static final int CAMERA_REQUEST = 1888;
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final int REQUEST_CODE_PERMISSIONS_STORAGE = 101;
    private boolean checkClickSetImageMale;
    private String resultDetech;
    private MyDialog myDialog;
    int id_user;

    private File imageFile;
    ImageViewModel imageViewModel;
    public UploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUploadBinding.inflate(inflater, container, false);
        backSwap();

        binding.btnUpload.setOnClickListener(v -> {
         openDialog();
        });

        return binding.getRoot();
    }

    private void openDialog(){
        bottomBinding = DialogBottomBinding.inflate(LayoutInflater.from(getContext()));
         bottom = new BottomSheetDialog(requireContext());
        bottom.setContentView(bottomBinding.getRoot());
        bottomBinding.btnSelectImage.setOnClickListener(v -> {
              openStorage();
        });
        bottomBinding.btnOpenCamera.setOnClickListener(v -> {
            try {
                openCamera();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        bottom.show();
    }
    private void openStorage(){
       if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            startStorage();
       }else {
           ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_STORAGE);
       }
    }
    private void startStorage(){
        closeDialog();
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
    }



    private void openCamera() throws FileNotFoundException {
     if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
       startCamera();
     }else {
       ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);

     }
    }


    private void startCamera() throws FileNotFoundException {
        closeDialog();
        File cacheDir = requireActivity().getApplicationContext().getCacheDir();
        // start default camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            String folderPath = cacheDir.getPath() + "/image/";
            File photoFile = new File(folderPath);
            if (!photoFile.exists()) {
                photoFile.mkdirs();
            }
            imageFile = new File(photoFile, System.currentTimeMillis() + ".jpg");

            Uri photoURI = FileProvider.getUriForFile(requireContext(),
                    "com.example.fakewedding.fileprovider",
                    imageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }
    @SuppressLint("StaticFieldLeak")
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_PICKER_SELECT && resultCode==RESULT_OK && data != null ){
            Uri selectedMediaUri =data.getData();
            EventBus.getDefault().post(new ImageEven(String.valueOf(selectedMediaUri),1));
        if(selectedMediaUri != null){
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.action_uploadFragment_to_swapFragment);
        }
        };
    }
    @Subscribe
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    startCamera();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getActivity(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_CODE_PERMISSIONS_STORAGE) {
            startStorage();
        }
    }
    private void closeDialog() {
        if (bottom.isShowing()) {
            bottom.dismiss();
        }
    }

    private void backSwap(){
        binding.backUpload.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_uploadFragment_to_swapFragment);

        });
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectedImage(ImageEven imageEven){
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