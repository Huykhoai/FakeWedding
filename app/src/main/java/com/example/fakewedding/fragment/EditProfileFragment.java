package com.example.fakewedding.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.example.fakewedding.model.DetailUser;
import com.example.fakewedding.server.ApiServer;
import com.example.fakewedding.server.Server;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;

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
        binding.btnUploadAvatarAccount.setOnClickListener(v -> {
            openDialog();
        });
        return binding.getRoot();
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
        Log.d("check_user_id", "loadIdUser: "+ id_user_str);
        if (id_user_str == "") {
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }

    }
    private void getData(){
        ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN3).getRetrofit().create(ApiServer.class);
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
                }

            }

            @Override
            public void onFailure(Call<DetailUser> call, Throwable t) {

            }
        });
    }
    private void UploadImage(){

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_PICKER_SELECT && resultCode== RESULT_OK && data!= null){
            Uri selectedImage = data.getData();

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
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
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