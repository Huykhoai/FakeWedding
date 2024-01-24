package com.example.fakewedding.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fakewedding.R;
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
    public static List<Category> categories;
    CategoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater,container, false);
        backHome();
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
                         categories = categoryResponse.getCategories();
                        Log.d("listcategory", "onResponse: "+categories.get(0).getId_cate());
                      adapter = new CategoryAdapter(getActivity(), categories);
                      binding.recycleCategory.setLayoutManager(new GridLayoutManager(getActivity(),2));
                      binding.recycleCategory.setAdapter(adapter);
                      onClickStart();
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
    private void onClickStart(){
        adapter.setOnclickItem(new CategoryAdapter.RecycleInterface() {
            @Override
            public void onCickItem(int position) {
                Category category = categories.get(position);
                CategoryFragmentDirections.ActionCategoryFragmentToTempleFragment action = CategoryFragmentDirections.actionCategoryFragmentToTempleFragment(String.valueOf(category.getId_cate()));
                NavHostFragment.findNavController(CategoryFragment.this).navigate(action);

            }
        });
    }
    private void backHome(){
        binding.categoryMenu.setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.action_categoryFragment_to_homeFragment);
        });

    }
}