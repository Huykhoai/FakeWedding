package com.example.fakewedding.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.fakewedding.R;
import com.example.fakewedding.activity.LoginActivity;
import com.example.fakewedding.activity.SwapingActivity;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.FragmentProfileBinding;
import com.example.fakewedding.dialog.MyDialog;
import com.example.fakewedding.dialog.MyOwnDialogFragment;
import com.example.fakewedding.model.DetailUser;
import com.example.fakewedding.server.ApiServer;
import com.example.fakewedding.server.Server;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    MyOwnDialogFragment myOwnDialogFragment;
    int id_user;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        navHomeFragment();
        navAccountFragment();
        navImageSwapped();
        navSwapVideo();
        Logout();
        loadIdUser();
        getData();
        return binding.getRoot();
    }

    private void navSwapVideo() {
        binding.profileImageSwappedVideo.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.acction_profileFragment_to_SwapVideo);
        });

    }

    private void navImageSwapped(){
        binding.profileImageSwapped.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.acction_profileFragment_to_ImageSwapped);
        });
    }
    private void navAccountFragment() {
        binding.constraintLayout3.setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.actiont_profileFragment_to_AccountFragment);
        });
    }

    private void navHomeFragment(){
        binding.profileMenu.setOnClickListener(v ->{
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.actiont_profileFragment_to_homeFragmen);
        });
        binding.profileHome.setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.actiont_profileFragment_to_homeFragmen);
        });
    }
    private void Logout(){
        binding.btnLogout.setOnClickListener(v -> {
            myOwnDialogFragment = new MyOwnDialogFragment();
            myOwnDialogFragment.setDialogTitle("Log Out");
            myOwnDialogFragment.setDialogMessage("You really want to log out?");

            myOwnDialogFragment.setListener(new MyOwnDialogFragment.MyOwnDialogListener() {
                @Override
                public void OnConfirm() {

                }
            });
            myOwnDialogFragment.setListener(new MyOwnDialogFragment.MyOwnDialogListener() {
                @Override
                public void OnConfirm() {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
            myOwnDialogFragment.show(getActivity().getSupportFragmentManager(), "profile_dialog");
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
                    Picasso.get().load(user.getLink_avatar()).into(binding.avatarProfile);
                    binding.nameProfile.setText(user.getUser_name());


                }

            }

            @Override
            public void onFailure(Call<DetailUser> call, Throwable t) {

            }
        });
    }
}