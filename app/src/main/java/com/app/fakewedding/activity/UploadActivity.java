package com.app.fakewedding.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.fakewedding.R;
import com.app.fakewedding.adapter.ImageUploadAdapter;
import com.app.fakewedding.api.RetrofitClient;
import com.app.fakewedding.databinding.ActivityUploadBinding;
import com.app.fakewedding.databinding.BottomNavUploadedImageBinding;
import com.app.fakewedding.databinding.DialogBottomBinding;
import com.app.fakewedding.dialog.MyDialog;
import com.app.fakewedding.model.ImageUploadNam;
import com.app.fakewedding.server.ApiServer;
import com.app.fakewedding.server.Server;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    BottomNavUploadedImageBinding bindingBottom;
    BottomSheetDialog bottomSheetDialog;
    int id_user;
    boolean male;
    List<ImageUploadNam> list;
    private File imageFile;
    ImageUploadAdapter adapter;
    private List<String> listUPload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadIdUser();
        getStatusMale();
        backSwap();
        getImageUploaded();
        binding.btnUpload.setOnClickListener(v -> {
            openDialog();
        });
        binding.uploadSeemore.setOnClickListener(v -> {
            openDialogBottom();
        });

    }
    private void loadIdUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str", "");
        Log.d("check_user_id", "loadIdUser: "+ id_user_str);
        if (id_user_str == "") {
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }
    private void getStatusMale(){
        Intent intent = getIntent();
        male = intent.getBooleanExtra("male", false);
        Log.d("male", "getStatusMale: "+male);
    }
    private void openDialogBottom() {
         bottomSheetDialog = new BottomSheetDialog(UploadActivity.this);
         bindingBottom = BottomNavUploadedImageBinding.inflate(LayoutInflater.from(UploadActivity.this));
         bottomSheetDialog.setContentView(bindingBottom.getRoot());
        ViewGroup.LayoutParams params =bindingBottom.getRoot().getLayoutParams();
        params.height = (int) (getResources().getDisplayMetrics().heightPixels *0.6);
        bindingBottom.getRoot().setLayoutParams(params);

        adapter = new ImageUploadAdapter(UploadActivity.this, listUPload);
        bindingBottom.recycleUploadedImage.setLayoutManager(new GridLayoutManager(UploadActivity.this,2));
        bindingBottom.recycleUploadedImage.setAdapter(adapter);

        adapter.setOnclickImageUploaded(new ImageUploadAdapter.OnclickImageUploaded() {
            @Override
            public void setOnclick(String image) {
                Log.d("imageadapter", "setOnclick: "+image);
                Intent inten= new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("image_uploaded", image);

                inten.putExtras(bundle);
                setResult(3,inten);
                finish();
            }
        });
        bottomSheetDialog.show();
    }
    private void getImageUploaded(){
        ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiServer.class);
            Call<ImageUploadNam> call = apiServer.getImageNam(id_user, male?"nam": "nu");
            call.enqueue(new Callback<ImageUploadNam>() {
                @Override
                public void onResponse(Call<ImageUploadNam> call, Response<ImageUploadNam> response) {
                    listUPload = new ArrayList<>();
                    if(response.isSuccessful()){
                        if(male){
                            Log.d("links uploaded", "onResponse: "+response.body().getLinks_nam());
                            listUPload.addAll(response.body().getLinks_nam());
                        }else {
                            listUPload.addAll(response.body().getLinks_nu());
                        }

                    }
                }

                @Override
                public void onFailure(Call<ImageUploadNam> call, Throwable t) {

                }
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
            ActivityCompat.requestPermissions(UploadActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
             Manifest.permission.MANAGE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_STORAGE);
        }
    }
    private void startStorage(){
        closeDialog();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            // Trước KITKAT (API level 19), sử dụng Intent.ACTION_GET_CONTENT
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICKER_SELECT);
        } else {
            // KITKAT và sau đó, sử dụng Intent.ACTION_OPEN_DOCUMENT
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICKER_SELECT);
        }
    }



    private void openCamera() throws FileNotFoundException {
        if(ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            startCamera();
        }else {
            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);

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
            Log.d("CameraUri", "startCamera: "+photoURI);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @SuppressLint("Static0FieldLeak")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_PICKER_SELECT && resultCode== RESULT_OK && data!= null){
            Uri uri = data.getData();
            Log.d("realPath", "onActivityResult: "+uri);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("selected_image", String.valueOf(uri));
            intent.putExtras(bundle);
            setResult(1,intent);
            finish();
        }
        if(requestCode==CAMERA_REQUEST && resultCode ==RESULT_OK){
             String imagefile = imageFile.getAbsolutePath();
            Log.d("Imagefile", "onActivityResult: "+imagefile);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("camera_image", imagefile);
            intent.putExtras(bundle);
            setResult(2,intent);
            finish();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startStorage();
            } else {
                Toast.makeText(UploadActivity.this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
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