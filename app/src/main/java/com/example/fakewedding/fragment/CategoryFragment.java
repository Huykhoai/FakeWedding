package com.example.fakewedding.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fakewedding.adapter.CategoryAdapter;
import com.example.fakewedding.api.RetrofitClient;
import com.example.fakewedding.databinding.FragmentCategoryBinding;
import com.example.fakewedding.model.Category;
import com.example.fakewedding.model.ListCategory;
import com.example.fakewedding.server.ApiServer;
import com.example.fakewedding.server.Server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment {
    FragmentCategoryBinding binding;
    List<ListCategory> arrayList;
    CategoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater,container, false);
        getData();
        return binding.getRoot();
    }
    private void getData(){
        ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiServer.class);
        Call<ListCategory> call = apiServer.getCategory();
        call.enqueue(new Callback<ListCategory>() {
            @Override
            public void onResponse(Call<ListCategory> call, Response<ListCategory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ListCategory categoryResponse = response.body();
                    if (categoryResponse != null && categoryResponse.getCategories() != null) {
                        List<Category> categories = categoryResponse.getCategories();
                        Log.d("listcategory", "onResponse: "+categories.get(0).getId_cate());
                      adapter = new CategoryAdapter(getActivity(), categories);
                      binding.recycleCategory.setLayoutManager(new GridLayoutManager(getActivity(),2));
                      binding.recycleCategory.setAdapter(adapter);
                    }
                } else {
                    // Xử lý khi có lỗi
                    Log.e("Category", "Failed to get data. Error code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ListCategory> call, Throwable t) {

            }
        });
    }
}