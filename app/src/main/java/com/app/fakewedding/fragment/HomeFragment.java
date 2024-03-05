package com.app.fakewedding.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fakewedding.R;
import com.app.fakewedding.adapter.Home6Viewpaper;
import com.app.fakewedding.databinding.FragmentHomeBinding;
import com.app.fakewedding.model.PagerHome6;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    Home6Viewpaper home6Viewpaper;
    List<PagerHome6> list;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        ViewpaperHome6();
        navSwapFragment();
        navProfileFragment();
        return binding.getRoot();
    }
    private void ViewpaperHome6(){

        list = new ArrayList<>();
        list.add(new PagerHome6(R.drawable.image1_home6, "Lorem ipsum dolor sit amet consectetur. Porttitor rhoncus arcu nec erat felis scelerisque turpis maecenas imperdiet. Quam ut ultrices erat massa blandit malesuada purus. Integer pulvinar congue facilisi leo nec ut tellus at. Feugiat condimentum magna tellus feugiat cras quis pulvinar congue."));
        list.add(new PagerHome6(R.drawable.image2_home6,"Lorem ipsum dolor sit amet consectetur. Porttitor rhoncus arcu nec erat felis scelerisque turpis maecenas imperdiet. Quam ut ultrices erat massa blandit malesuada purus. Integer pulvinar congue facilisi leo nec ut tellus at. Feugiat condimentum magna tellus feugiat cras quis pulvinar congue."));
        list.add(new PagerHome6(R.drawable.image3_home6,"Lorem ipsum dolor sit amet consectetur. Porttitor rhoncus arcu nec erat felis scelerisque turpis maecenas imperdiet. Quam ut ultrices erat massa blandit malesuada purus. Integer pulvinar congue facilisi leo nec ut tellus at. Feugiat condimentum magna tellus feugiat cras quis pulvinar congue."));

        home6Viewpaper = new Home6Viewpaper(getActivity(),list);
        binding.viewPaperHome6.setAdapter(home6Viewpaper);
        binding.circleIndicator.setViewPager(binding.viewPaperHome6);
    }
   private void navSwapFragment(){
        binding.btnstartSwaping.setOnClickListener(v -> {
           NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_CategoryFragment);
        });
        binding.btnCreateSwap.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_CategoryFragment);
        });
    }
    private void navProfileFragment(){
        binding.homeMenu.setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.action_homeFragment_to_profileFragment);
        });
    }
}