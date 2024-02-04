package com.example.fakewedding.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.fakewedding.R;
import com.example.fakewedding.activity.UploadActivity;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.DialogBottomBinding;
import com.example.fakewedding.databinding.FragmentEditProfileBinding;
import com.example.fakewedding.model.ChangeAvatar;
import com.example.fakewedding.model.DetailUser;
import com.example.fakewedding.server.ApiServer;
import com.example.fakewedding.server.Server;
import com.example.fakewedding.until.RealPathUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileFragment extends Fragment {
   int id_user;
    DialogBottomBinding bottomBinding;
   FragmentEditProfileBinding binding;
    private BottomSheetDialog bottom;
    private static final int IMAGE_PICKER_SELECT = 1889;
    private static final int CAMERA_REQUEST = 1888;
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final int REQUEST_CODE_PERMISSIONS_STORAGE = 101;
    public static final int RESULT_OK= -1;
    private File imageFile;
    String token;
    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container,false);
        loadIdUser();
        getData();
        clickChangepass();
        navProfileFragment();
        clickChangeAvatar();
        return binding.getRoot();
    }
    private void clickChangeAvatar(){
        binding.btnUploadAvatarAccount.setOnClickListener(v -> {
            openDialog();
        });
    }

    private void clickChangepass() {
        binding.navChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(requireContext(), v);

                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.option1){
                            navFragmentChangePass();
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }

        });
    }
    private void navFragmentChangePass() {
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.action_AccountFragment_to_ChangePass);
    }
    private void navProfileFragment(){
        binding.accountMenu.setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.action_AccountFragment_to_profileFragment);
        });
    }

    private void loadIdUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str", "");
        token = sharedPreferences.getString("token","");
        Log.d("check_user_id", "loadIdUser: "+ id_user_str);
        if (id_user_str == "") {
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }

    }
    private void getData(){
        ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiServer.class);
        Call<DetailUser> call = apiServer.getUSer(id_user);
        call.enqueue(new Callback<DetailUser>() {
            @Override
            public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
                if(response.isSuccessful()&& response.body() != null){
                    DetailUser user = response.body();
                    Picasso.get().load(user.getLink_avatar()).into(binding.avatarAccount);
                    binding.editNameAccount.setText(user.getUser_name());
                    binding.editNameAccount.setEnabled(false);
                    binding.editEmailAccount.setText(user.getEmail());
                    binding.editEmailAccount.setEnabled(false);
                    binding.editLocationAccount.setEnabled(false);
                    binding.editLocationAccount.setText(""+user.getIp_register());
                }

            }

            @Override
            public void onFailure(Call<DetailUser> call, Throwable t) {

            }
        });
    }
    private void UploadImage(String imageupload){
      ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiServer.class);
      Call<ChangeAvatar> call = apiServer.changeavatar(id_user, "Bearer "+token, imageupload, "upload");
      call.enqueue(new Callback<ChangeAvatar>() {
          @Override
          public void onResponse(Call<ChangeAvatar> call, Response<ChangeAvatar> response) {
              if(response.isSuccessful()){
                  ChangeAvatar changeAvatar = response.body();
                  Log.d("Huy", "onResponse: "+changeAvatar.getImage());
                  Picasso.get().load(imageupload).into(binding.avatarAccount);
              }else {
                  Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
              }

          }

          @Override
          public void onFailure(Call<ChangeAvatar> call, Throwable t) {
              Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
          }
      });
    }
    private void getData(Uri imageselected){
        Log.d("UriimageSelected", "getData: "+imageselected);
        String filePath ="";
        if(Build.VERSION.SDK_INT<11){
            filePath = RealPathUtil.getRealPathFromURI_BelowAPI11(getActivity(),imageselected);
        }else if(Build.VERSION.SDK_INT<19){
            filePath = RealPathUtil.getRealPathFromURI_API11to18(getActivity(),imageselected);
        }else {
            filePath = RealPathUtil.getRealPathFromURI_API19(getActivity(),imageselected);
        }
        imageFile =new File(filePath);
        Log.d("Huy", "getData_0: "+ imageFile);
        RequestBody requestBody =RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart =MultipartBody.Part.createFormData("src_img",imageFile.getName(),requestBody);
        ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiServer.class);
        Log.d("Huy", "getData: "+ id_user + imagePart);
            Call<String>call = apiServer.uploadImage(id_user, "src_nam", imagePart);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        Log.d("Huy", "getDataResult: "+ response.body());
                        String ImageReplace = response.body().replace("/var/www/build_futurelove","https://futurelove.online");
                        UploadImage(ImageReplace);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("Huy", "onFailure: "+ t.getMessage());
                }
            });




    }
    private void openDialog(){
        bottomBinding = DialogBottomBinding.inflate(LayoutInflater.from(getActivity()));
        bottom = new BottomSheetDialog(getActivity());
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
    @SuppressLint("Static0FieldLeak")
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_PICKER_SELECT && resultCode== RESULT_OK && data!= null){
            Uri selectedImage = data.getData();
            Log.d("Huy", "onActivityResult: "+selectedImage);
            getData(selectedImage);
        }else if(requestCode==CAMERA_REQUEST){
            Log.d("Huy", "onActivityResult: "+imageFile);
            Uri imagefile = Uri.parse(imageFile.getAbsolutePath());
            getData(imagefile);
        }
    }
    private void openStorage(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            startStorage();
        }else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_STORAGE);
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
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            startCamera();
        }else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);

        }
    }


    private void startCamera() throws FileNotFoundException {
        closeDialog();
        File cacheDir = getActivity().getApplicationContext().getCacheDir();
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

            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.example.fakewedding.fileprovider",
                    imageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
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
}