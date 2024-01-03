package com.example.fakewedding.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.fakewedding.R;
import com.example.fakewedding.activity.LoginActivity;
import com.example.fakewedding.api.QueryValueCallBack;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.FragmentRegisterBinding;
import com.example.fakewedding.dialog.MyOwnDialogFragment;
import com.example.fakewedding.model.Login;
import com.example.fakewedding.server.ApiServer;
import com.example.fakewedding.server.Server;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {
    private final String EXISTED_ACCOUNT_STR = "{message=Account already exists!}";
    private FragmentRegisterBinding binding;
    private LoginActivity loginActivity;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater,container,false);
        loginActivity = (LoginActivity) getActivity();
        setupTextWatch();
        BtnRegister();
        BtnLogin();
        return binding.getRoot();

    }
    private void BtnLogin() {
        binding.txtsignin.setOnClickListener(v -> {
            navLoginFragment();
        });
    }
    private void BtnRegister(){
        binding.btnRegister.setOnClickListener(v -> {
            String confirm = binding.editConfirmRegister.getText().toString();
            String email = binding.editEmailRegister.getText().toString();
            String pass = binding.editPassRegister.getText().toString();
            if(isCompletedInformation(confirm,email,pass)){
                checkAccountRegister(email,pass);
            }
        });
    }
    private void checkAccountRegister(String email,String pass){
        callSigninApi(new QueryValueCallBack() {
            @Override
            public void onQueryValueReceived(String query) {
                if(!query.contains(EXISTED_ACCOUNT_STR)){
                    Log.d("Huy", "onQueryValueReceived: "+query);
                    Dialog dialog = showRegistrationSuccessDialog();
                    new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            navLoginFragment();
                        }
                    },5000);
                }else {
                    MyOwnDialogFragment myOwnDialogFragment = new MyOwnDialogFragment("Existed Account" , "Would you like to login now", R.drawable.ic_warning);
                    myOwnDialogFragment.setListener(new MyOwnDialogFragment.MyOwnDialogListener() {
                        @Override
                        public void OnConfirm() {
                           navLoginFragment();
                        }
                    });
                    myOwnDialogFragment.show(loginActivity.getSupportFragmentManager(),"register_dialog");
                }
            }

            @Override
            public void onApiCallFailed(Throwable tb) {

            }
        },email,pass);
    }

    private void callSigninApi(QueryValueCallBack callBack, String email, String pass){
        ApiServer apiServer = RetrofitClient.getInstance(Server.URI).getRetrofit().create(ApiServer.class);
        Call<Object> call = apiServer.signup(email,pass,"abc","https://i.pinimg.com/564x/3f/94/70/3f9470b34a8e3f526dbdb022f9f19cf7.jpg", "fsffe");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()&& response.body()!=null){
                    callBack.onQueryValueReceived(response.body().toString());
                    Log.d("Huy", "body: "+response.body());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }
    private Dialog showRegistrationSuccessDialog(){
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_register_success);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        return dialog;
    }
    private boolean isCompletedInformation(String confirm, String email, String password) {
        if(isValidateConfirm(confirm, password)&& isValidateEmail(email)&&isValidatePassword(password)){
            return true;
        }
        return false;
    }
    private void setupTextWatch(){
        binding.editConfirmRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmlAlertVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.editEmailRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailAlertVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.editPassRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passAlertVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void emailAlertVisibility(){
        String email = Objects.requireNonNull(binding.editEmailRegister.getText().toString());
        boolean isValidateemail = isValidateEmail(email);
        binding.tvEmailAlertRegister.setVisibility(isValidateemail? View.GONE : View.VISIBLE);
    }
    private void confirmlAlertVisibility(){
        String confirm = Objects.requireNonNull(binding.editConfirmRegister.getText().toString());
        String password = binding.editPassRegister.getText().toString();
        boolean isValidateConfirm = isValidateConfirm(confirm,password);
        binding.tvConfirmAlertRegister.setVisibility(isValidateConfirm? View.GONE : View.VISIBLE);
    }
    private void passAlertVisibility(){
        String pass = Objects.requireNonNull(binding.editPassRegister.getText().toString());
        boolean isValidatePass= isValidatePassword(pass);
        binding.tvPassAlertRegister.setVisibility(isValidatePass? View.GONE : View.VISIBLE);
    }
    private boolean isValidateEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidatePassword(String pass){
        return pass.length()>=8 && !containSpecialCharacters(pass);
    }
    private boolean isValidateConfirm(String confirm,String password){
        if(confirm.equals(password)){
            return true;
        }else {
            binding.tvConfirmAlertRegister.setText("Confirmation password is incorrect");
        }
        return false;
    }
    private boolean containSpecialCharacters(String pass){
        String special = "~`!@#$%^&*()+={}[]|\\:;\"<>,.?/ ";
        for(int i=0;i<pass.length();i++){
            if(special.contains(String.valueOf(pass.charAt(i)))){
                return true;
            }
        }
        return  false;
    }
    private void navLoginFragment(){
        NavHostFragment.findNavController(RegisterFragment.this).navigate(R.id.action_registerFragment_to_loginFragment);
    }
}