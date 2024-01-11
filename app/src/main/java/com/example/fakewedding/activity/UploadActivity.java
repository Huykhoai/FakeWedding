package com.example.fakewedding.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.fakewedding.R;
import com.example.fakewedding.databinding.ActivityUploadBinding;
import com.example.fakewedding.databinding.DialogBottomBinding;
import com.example.fakewedding.databinding.FragmentUploadBinding;
import com.example.fakewedding.dialog.MyDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;

public class UploadActivity extends AppCompatActivity {
     ActivityUploadBinding binding;
    private static final String NO_FACE_DETECTED = "No faces detected";
    private static final String MORE_THAN_ONE_FACE = "More than one face is recognized";
    private DialogBottomBinding bottomBinding;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        backSwap();
        binding.btnUpload.setOnClickListener(v -> {
            openDialog();
        });
    }
    private void openDialog(){
        bottomBinding = DialogBottomBinding.inflate(LayoutInflater.from(UploadActivity.this));
        bottom = new BottomSheetDialog(UploadActivity.this);
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
        if(ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            startStorage();
        }else {
            ActivityCompat.requestPermissions(UploadActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_STORAGE);
        }
    }
    private void startStorage(){
        closeDialog();
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
    }



    private void openCamera() throws FileNotFoundException {
        if(ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            startCamera();
        }else {
            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);

        }
    }


    private void startCamera() throws FileNotFoundException {
        closeDialog();
        File cacheDir = UploadActivity.this.getApplicationContext().getCacheDir();
        // start default camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(UploadActivity.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            String folderPath = cacheDir.getPath() + "/image/";
            File photoFile = new File(folderPath);
            if (!photoFile.exists()) {
                photoFile.mkdirs();
            }
            imageFile = new File(photoFile, System.currentTimeMillis() + ".jpg");

            Uri photoURI = FileProvider.getUriForFile(UploadActivity.this,
                    "com.example.fakewedding.fileprovider",
                    imageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_PICKER_SELECT && resultCode== RESULT_OK && data!= null){
            Uri selectedImage = data.getData();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("selected_image", String.valueOf(selectedImage));
            intent.putExtras(bundle);
            setResult(1,intent);
            finish();
        }
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
                Toast.makeText(UploadActivity.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
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
           finish();

        });
    }
}