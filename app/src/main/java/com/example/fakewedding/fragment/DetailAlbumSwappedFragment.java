package com.example.fakewedding.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fakewedding.R;
import com.example.fakewedding.adapter.ResultAdapter;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.FragmentDetailAlbumSwappedBinding;
import com.example.fakewedding.model.AlbumSwapped;
import com.example.fakewedding.model.DetailAlbum;
import com.example.fakewedding.server.ApiServer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailAlbumSwappedFragment extends Fragment {
    FragmentDetailAlbumSwappedBinding binding;
    String id_sk;
    int id_user;
    ResultAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailAlbumSwappedBinding.inflate(inflater, container,false);
        getIdSk();
        loadIdUser();
        getData();
        navAlbumSwapped();
        return binding.getRoot();
    }

    private void navAlbumSwapped() {
       binding.detailAlbumSwappedMenu.setOnClickListener(v -> {
           NavHostFragment.findNavController(DetailAlbumSwappedFragment.this).navigate(R.id.action_detail_image_SwappedFragment_to_ImageSwapped);
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
    private void getData() {
        ApiServer apiServer = RetrofitClient.getInstance("").getRetrofit().create(ApiServer.class);
        Call<DetailAlbum> call = apiServer.listDetailAlbum(id_user, id_sk);
        call.enqueue(new Callback<DetailAlbum>() {
            @Override
            public void onResponse(Call<DetailAlbum> call, Response<DetailAlbum> response) {
                if(response.isSuccessful()){
                    List<AlbumSwapped> albumSwappeds = response.body().getDetailAlbum();
                    String imageNam = albumSwappeds.get(0).getLink_nam_goc();
                    String replaceImageName = imageNam.replace("/var/www/build_futurelove","https://futurelove.online");
                    Picasso.get().load(replaceImageName).into(binding.detailImageNam);
                    String imageNu = albumSwappeds.get(0).getLink_nu_goc();
                    String replaceImageNameNu = imageNu.replace("/var/www/build_futurelove","https://futurelove.online");
                    Picasso.get().load(replaceImageNameNu).into(binding.detailImageNu);
                  List<String> listImage = new ArrayList<>();
                  for(int i=0;i<response.body().getDetailAlbum().size();i++){
                      String strImage = response.body().getDetailAlbum().get(i).getLink_da_swap();
                      listImage.add(strImage);
                  }
                  adapter = new ResultAdapter(getActivity(), listImage);
                  binding.detailRecycleview.setLayoutManager(new GridLayoutManager(getActivity(),2));
                  binding.detailRecycleview.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<DetailAlbum> call, Throwable t) {

            }
        });
    }

    private void getIdSk() {
        Bundle bundle = getArguments();
        id_sk = bundle.getString("id_sk");
        Log.d("Huy", "getIdSk: "+id_sk);
    }

}