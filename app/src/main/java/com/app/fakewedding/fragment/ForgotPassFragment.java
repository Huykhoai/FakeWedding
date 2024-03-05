package com.app.fakewedding.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.app.fakewedding.R;
import com.app.fakewedding.api.RetrofitClient;
import com.app.fakewedding.databinding.FragmentForgotPassBinding;
import com.app.fakewedding.dialog.MyDialog;
import com.app.fakewedding.model.Message;
import com.app.fakewedding.server.ApiServer;
import com.app.fakewedding.server.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPassFragment extends Fragment {
    private FragmentForgotPassBinding binding;
    private MyDialog myDialog;
    public ForgotPassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPassBinding.inflate(inflater, container, false);
        ResetPass();
        binding.btnBackLogin.setOnClickListener(v -> backLogin());
        return binding.getRoot();
    }

    private void backLogin() {
        NavController nav = NavHostFragment.findNavController(this);
        nav.navigate(R.id.action_forgotpassFragment_to_loginFragment);
    }

    private void ResetPass() {
        binding.btnSendForgot.setOnClickListener(v -> {
            String email = binding.editForgotpass.getText().toString();
            ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiServer.class);
            Call<Message> call = apiServer.sendData(email);
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                       myDialog = getDialog();
                    if(response.isSuccessful()){
                        Message result = response.body();
                        myDialog.setTitle("RESET SUCCESS");
                        myDialog.setContent(result.getMessage());
                        myDialog.setContentButton("ok");
                        myDialog.show();

                    }else {
                        myDialog = MyDialog.getInstance();
                        myDialog.setContent("request failed");
                        myDialog.setTitle("ERROR");
                        myDialog.setContentButton("ok");
                        myDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {

                }
            });
        });
    }
    private MyDialog getDialog() {
        if (myDialog == null) {
            myDialog = MyDialog.getInstance(getActivity());
            Window window = myDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.gravity = Gravity.CENTER;// Thiết lập vị trí ở giữa dưới
                layoutParams.y = 300; // Đặt khoảng cách dịch chuyển theo chiều dọc (30dp)
                window.setAttributes(layoutParams);
            }
        }
        return myDialog;
    }
}