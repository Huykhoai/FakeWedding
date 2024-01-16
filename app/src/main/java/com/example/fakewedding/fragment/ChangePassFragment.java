package com.example.fakewedding.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fakewedding.R;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.FragmentChangePassBinding;
import com.example.fakewedding.dialog.MyOwnDialogFragment;
import com.example.fakewedding.model.Login;
import com.example.fakewedding.server.ApiServer;
import com.example.fakewedding.server.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePassFragment extends Fragment {
     FragmentChangePassBinding binding;
     int id_user;
    String password;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePassBinding.inflate(inflater, container,false);
        loadIdUser();
        ChangePassword();
        backAccount();
        return binding.getRoot();
    }
    private void loadIdUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str", "");
        token = sharedPreferences.getString("token","");
        password = sharedPreferences.getString("password", "");
        Log.d("pass_change", "loadIdUser: "+password);
        Log.d("check_user_id", "loadIdUser: "+ id_user_str);
        Log.d("token_Change", "loadIdUser: "+token);
        if (id_user_str == "") {
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }

    }
    private void ChangePassword(){
            binding.btnUpdatePassword.setOnClickListener(v -> {
                String oldpass = binding.editOldPass.getText().toString();
                String newpass = binding.editNewPass.getText().toString();
                String confirm = binding.editCofirmPassword.getText().toString();
                if(validate(oldpass,newpass,confirm)>0){
                    Log.d("oldpass", "ChangePassword: "+binding.editOldPass.getText().toString());
                   apiChangepass(id_user,token,oldpass, newpass);
                }
            });
    }
    private void apiChangepass(int id_user,String token,String oldpass,String newpass){
        Log.d("api", "apiChangepass: "+id_user +" "+token+" "+oldpass+" "+newpass);
        ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiServer.class);
        Call<Object> call = apiServer.changepass(id_user,"Bearer "+token, oldpass,newpass);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                 if(response.isSuccessful()){
                     Log.d("change_pass_body", "onResponse: "+response.body());
                     AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                     builder.setTitle("changed password successfully");
                     builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                            binding.editOldPass.setHint("Old password");
                            binding.editNewPass.setHint("New password");
                            binding.editCofirmPassword.setHint("Cofirm password");
                         }
                     });
                 }else {
                     Log.d("error_changepass", "onResponse: error");
                 }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "error_change"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private int validate(String oldpass,String newpass, String confirm){
        if(oldpass.isEmpty()){
            binding.tvErrorOldpass.setVisibility(View.VISIBLE);
            return -1;
        }else {
            if(!oldpass.equals(password)){
                binding.tvErrorOldpass.setText("Old password is incorrect");
                binding.tvErrorOldpass.setVisibility(View.VISIBLE);
                return -1;
            }else {
                binding.tvErrorOldpass.setVisibility(View.INVISIBLE);
            }
            binding.tvErrorOldpass.setVisibility(View.INVISIBLE);
        }
        if(newpass.isEmpty()){
            binding.tvErrorNewpass.setVisibility(View.VISIBLE);
            return -1;
        }else {
            binding.tvErrorNewpass.setVisibility(View.INVISIBLE);
        }
        if(confirm.isEmpty()){
            binding.tvErrorCofirm.setVisibility(View.VISIBLE);
            return -1;
        }else {
            if(!confirm.equals(newpass)){
                binding.tvErrorCofirm.setText("Confirmation password is incorrect");
                binding.tvErrorCofirm.setVisibility(View.VISIBLE);
                return -1;
            }else {
                binding.tvErrorCofirm.setVisibility(View.INVISIBLE);
            }
            binding.tvErrorCofirm.setVisibility(View.INVISIBLE);
        }
        return 1;
    }
    private void backAccount(){
        binding.changepassMenu.setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.action_changeFragment_to_AccountFragment);
        });

    }
}