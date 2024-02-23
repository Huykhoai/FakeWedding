package com.example.fakewedding.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fakewedding.adapter.TempleVideoAdapter;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.FragmentSwapVideoBinding;
import com.example.fakewedding.model.ListTempleVideo;
import com.example.fakewedding.model.TempleVideo;
import com.example.fakewedding.server.ApiServer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SwapVideoFragment extends Fragment {
    FragmentSwapVideoBinding binding;
    ArrayList<TempleVideo> arrayList;
    TempleVideoAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSwapVideoBinding.inflate(inflater, container, false);
        getData();
        return binding.getRoot();
    }

    private void getData() {
        ApiServer apiServer = RetrofitClient.getInstance("").getRetrofit().create(ApiServer.class);
        Call<ListTempleVideo> call = apiServer.getListVideo();
        call.enqueue(new Callback<ListTempleVideo>() {
            @Override
            public void onResponse(Call<ListTempleVideo> call, Response<ListTempleVideo> response) {
                if(response.isSuccessful()&& response.body() != null){
                    Log.d("Huy", "onResponse: "+response.body());
                    ListTempleVideo listTempleVideo = response.body();
                    List<TempleVideo> videoList = listTempleVideo.getList();
                    Log.d("Huy", "onResponse: "+videoList.get(0).getLink_video());
                    arrayList = new ArrayList<>();
                    for(TempleVideo templeVideo:videoList){
                        arrayList.add(templeVideo);
                    }
                   adapter = new TempleVideoAdapter(getActivity(), arrayList);
                    binding.recycleListVideo.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    binding.recycleListVideo.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ListTempleVideo> call, Throwable t) {

            }
        });
    }

}