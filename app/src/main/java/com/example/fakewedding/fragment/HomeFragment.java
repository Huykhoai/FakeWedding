package com.example.fakewedding.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fakewedding.R;
import com.example.fakewedding.adapter.Home6Viewpaper;
import com.example.fakewedding.databinding.FragmentHomeBinding;
import com.example.fakewedding.model.PagerHome6;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


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
}