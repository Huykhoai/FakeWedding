package com.app.fakewedding.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.fakewedding.R;
import com.app.fakewedding.activity.SwapingActivity;
import com.app.fakewedding.adapter.TempleAdapter;
import com.app.fakewedding.api.RetrofitClient;
import com.app.fakewedding.databinding.FragmentTempleBinding;
import com.app.fakewedding.model.ListTemple;
import com.app.fakewedding.model.Temple;
import com.app.fakewedding.server.ApiServer;
import com.app.fakewedding.server.Server;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TempleFragment extends Fragment {
    FragmentTempleBinding binding;
    int id_category;
    List<Temple> temple;
    TempleAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTempleBinding.inflate(inflater, container, false);
        getIdcategory();
        getData();
        navFragmentCategory();
        return binding.getRoot();
    }

    private void navFragmentCategory() {
        binding.templeMenu.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_templeFragment_to_categoryFragment);
        });
    }

    private void getData() {
        ApiServer apiServer = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiServer.class);
        Call<ListTemple> call = apiServer.getListTemple(id_category);
        call.enqueue(new Callback<ListTemple>() {
            @Override
            public void onResponse(Call<ListTemple> call, Response<ListTemple> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("listtemple", "onResponse: "+response.body().getGetListTemple().toString());
                    ListTemple listTemple = response.body();
                    if (listTemple != null && listTemple.getGetListTemple() != null) {
                        temple = listTemple.getGetListTemple();
                        Log.d("listtemple", "onResponse: "+temple.get(0).getId());
                        adapter = new TempleAdapter(getActivity(), temple);
                        binding.recycleTemple.setLayoutManager(new GridLayoutManager(getActivity(),2));
                        binding.recycleTemple.setAdapter(adapter);
                        detailImage(Gravity.CENTER);
                        createNow();
                    }
                }else {
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListTemple> call, Throwable t) {
                Log.d("err", "onFailure: "+t.getMessage());
            }
        });
    }
    private void detailImage(int gravity){
        adapter.setOnCickDetail(new TempleAdapter.OnItemClickListenerDetail() {
            @Override
            public void onItemClick(Temple temple) {
                Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.item_detail_temple);

                Window window = dialog.getWindow();
                if(window == null)
                    return;
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.gravity = gravity;
                if(gravity == Gravity.CENTER){
                    dialog.setCancelable(true);
                }else {
                    dialog.setCancelable(false);
                }
               window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                ImageView imageView = dialog.findViewById(R.id.image_temple_detail);
                Picasso.get().load(temple.getImage()).into(imageView);
                dialog.show();
            }
        });
    }
    private void createNow(){
        adapter.setOnclickTemple(new TempleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Temple temple) {
                Intent intent = new Intent(getActivity(), SwapingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("temple", temple);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void getIdcategory() {
        Bundle bundle = getArguments();
        id_category = Integer.parseInt(bundle.getString("idcategory"));
        Log.d("id_category", "getIdcategory: "+id_category);
    }

}