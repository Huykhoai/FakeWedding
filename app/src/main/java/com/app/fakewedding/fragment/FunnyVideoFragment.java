package com.app.fakewedding.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fakewedding.R;
import com.app.fakewedding.activity.ResultVideoActivity;
import com.app.fakewedding.adapter.AllVideoSwappedAdapter;
import com.app.fakewedding.api.RetrofitClient;
import com.app.fakewedding.databinding.FragmentFunnyVideoBinding;
import com.app.fakewedding.model.AllVideoResponse;
import com.app.fakewedding.model.SwapVideo;
import com.app.fakewedding.server.ApiServer;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FunnyVideoFragment extends Fragment implements AllVideoSwappedAdapter.onCickStart{
    FragmentFunnyVideoBinding binding;
    ArrayList<SwapVideo> arrayList;
    AllVideoSwappedAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFunnyVideoBinding.inflate(inflater, container,false);
        getData();
        back();
        return binding.getRoot();
    }

    private void back() {
        binding.allVideoSwapMenu.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void getData() {
        ApiServer apiServer = RetrofitClient.getInstance("").getRetrofit().create(ApiServer.class);
        Call<AllVideoResponse> call = apiServer.getAllVideoSwapped();
        call.enqueue(new Callback<AllVideoResponse>() {
            @Override
            public void onResponse(Call<AllVideoResponse> call, Response<AllVideoResponse> response) {
                if(response.isSuccessful()&& response.body() != null){
                    arrayList = response.body().getList();
                    Log.d("Huy", "onResponse: "+arrayList.get(0).getLink_vid_da_swap());
                    adapter = new AllVideoSwappedAdapter(getActivity(), arrayList,FunnyVideoFragment.this);
                    binding.recycleAllVideo.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    binding.recycleAllVideo.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<AllVideoResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(SwapVideo swapVideo) {
        Intent intent = new Intent(getActivity(), ResultVideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sukienVideo",swapVideo);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}