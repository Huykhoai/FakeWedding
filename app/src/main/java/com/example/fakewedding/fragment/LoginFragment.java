package com.example.fakewedding.fragment;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fakewedding.R;
import com.example.fakewedding.activity.LoginActivity;
import com.example.fakewedding.activity.MainActivity;
import com.example.fakewedding.api.QueryValueCallBack;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.FragmentLoginBinding;
import com.example.fakewedding.model.Login;
import com.example.fakewedding.server.ApiServer;
import com.example.fakewedding.server.Server;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding ;

    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        checkLogin();
        btnRegister();
        BtnLogin();
        navForgotFragment();

        return view;
    }

    private void checkLogin() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("isLoginStatus", Context.MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        Intent intent;
        if (isLogin) {
            intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish(); // Kết thúc hoạt động hiện tại sau khi chuyển đổi
        }

    }

    private void navForgotFragment(){
        binding.forgotpass.setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.action_loginFragment_to_forgotFragment);
        });
    }
    private void BtnLogin(){

            binding.btnLogin.setOnClickListener(v -> {
                String email = binding.editEmailLogin.getText().toString();
                String password = binding.editPassLogin.getText().toString();

                    if (isCompletedInfomation(email, password)) {
                        checkAccountRegister(email, password);
                        remember(email, password, true);
                }
            });
    }
    private void checkAccountRegister(String email,String pass){
        callLoginApi(new QueryValueCallBack() {
            @Override
            public void onQueryValueReceived(String query) {
                if(query.contains("Logined in successfully")){
                    SharedPreferences preferences = getActivity().getSharedPreferences("isLoginStatus", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isLogin", true);
                    editor.apply();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    binding.textLayoutLoginPass.setHelperText("");
                }else if(query.contains("Invalid in Password!!")){
                    binding.textLayoutLoginPass.setHelperText("Invalid in Password!!");
                }else {
                    binding.textLayoutLoginPass.setHelperText("");
                }
            }

            @Override
            public void onApiCallFailed(Throwable tb) {

            }
        },email,pass);
    }
    private void callLoginApi(QueryValueCallBack callBack, String email, String pass){

        ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiServer.class);
        Call<Login> call = apiServer.login(email,pass);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.isSuccessful()&& response.body()!= null){
                    String token = response.body().getToken();
                    Log.d("Huytoken", "onResponse: "+token);
                    String user_id = String.valueOf(response.body().getId_user());
                    if(!user_id.equals("0")){
                        callBack.onQueryValueReceived("Logined in successfully");

                    }else {
                        callBack.onQueryValueReceived("Invalid in Password!!");
                    }
                    Log.d("check_login", "onResponse: "+user_id);
                    SharedPreferences spf = getActivity().getSharedPreferences("id_user",0);
                    SharedPreferences.Editor edit = spf.edit();
                    edit.putString("id_user_str", user_id);
                    edit.putString("password",binding.editPassLogin.getText().toString());
                    edit.putString("token", token);
                    edit.apply();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }
    private void btnRegister(){
        binding.txtsignup.setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.action_loginFragment_to_resgisterFragment);
        });
    }

    private boolean isCompletedInfomation(String email,String pass){
            if (email.isEmpty()){
                binding.textLayoutLoginEmail.setHelperText("Please don't leave it blank");
            } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.textLayoutLoginEmail.setHelperText("Follow the email rule: abc@abc.abc");
            }else {
                binding.textLayoutLoginEmail.setHelperText("");
                    return true;
                }
            if (pass.isEmpty()) {
                binding.textLayoutLoginPass.setHelperText("please don't leave it blank");
            } else {
                binding.textLayoutLoginPass.setHelperText("");
                return true;
            }
        return false;
    }
    private void remember(String phone,String pass, boolean status){
        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("THONGTIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!status){
            editor.clear();
        }else {
            editor.putString("EMAIL", phone);
            editor.putString("PASSWORD" , pass);
            editor.putBoolean("REMEMBER", status);
        }
        editor.commit();
    }
}