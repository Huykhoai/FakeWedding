package com.example.fakewedding.fragment;

import android.os.Bundle;

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
import android.widget.Toast;

import com.example.fakewedding.R;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.FragmentForgotPassBinding;
import com.example.fakewedding.dialog.MyDialog;
import com.example.fakewedding.model.Message;
import com.example.fakewedding.server.ApiServer;
import com.example.fakewedding.server.Server;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;


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