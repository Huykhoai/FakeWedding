package com.app.fakewedding.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fakewedding.R;
import com.app.fakewedding.adapter.Album_Swapped_adapter;
import com.app.fakewedding.api.RetrofitClient;
import com.app.fakewedding.databinding.FragmentAlbumSwapedBinding;
import com.app.fakewedding.model.Album;
import com.app.fakewedding.model.AlbumSwapped;
import com.app.fakewedding.server.ApiServer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlbumSwapedFragment extends Fragment {
    FragmentAlbumSwapedBinding binding;
    int id_user;
    Album_Swapped_adapter adapter;
    ArrayList<AlbumSwapped> listSukienImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAlbumSwapedBinding.inflate(inflater,container,false);
        loadIdUser();
        //checkData();
        navPofileFragment();
        getData();
        return binding.getRoot();
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
        ApiServer apiServer = RetrofitClient.getInstance("").getRetrofit().create(ApiServer.class);
        Call<List<Album>> call = apiServer.listAlbum();
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful()) {
                    Log.d("Huy", "onResponse: " + response.body());
                    if(response.body().size() != 0){
                        List<Album> list = response.body();
                        listSukienImage = new ArrayList<>();
                        for (Album album: list) {
                           AlbumSwapped albumSwapped = album.getList_sukien_image().get(0);
                            Log.d("Huy", "onResponse: "+albumSwapped);
                            listSukienImage.add(albumSwapped);
                        }
                        adapter = new Album_Swapped_adapter(getActivity(), listSukienImage);
                        binding.recycleViewAlbumSwapped.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        binding.recycleViewAlbumSwapped.setAdapter(adapter);
                        onclickViewDetail();
                    }else {
                        binding.txtThongbao.setVisibility(View.VISIBLE);
                    }

                }
            }
            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }
    private void onclickViewDetail(){
        adapter.setOnclickItem(new Album_Swapped_adapter.onCickItem() {
            @Override
            public void onClick(String id_sk) {
                Log.d("Huy", "onClick: "+id_sk);

                AlbumSwapedFragmentDirections.ActionImageswappedFragmentToDetailSwappedFragment action = AlbumSwapedFragmentDirections.actionImageswappedFragmentToDetailSwappedFragment(id_sk);

                NavHostFragment.findNavController(AlbumSwapedFragment.this).navigate(action);
            }
        });
    }
    private void navPofileFragment(){
        binding.albumSwappedMenu.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_Image_swappedFragment_to_ProfileFragment);
        });
    }
}