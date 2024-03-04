package com.example.fakewedding.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fakewedding.R;
import com.example.fakewedding.adapter.AboutUsAdapter;
import com.example.fakewedding.adapter.Home6Viewpaper;
import com.example.fakewedding.databinding.FragmentAboutUsBinding;
import com.example.fakewedding.model.PagerAboutUs;
import com.example.fakewedding.model.PagerHome6;

import java.util.ArrayList;


public class AboutUsFragment extends Fragment implements AboutUsAdapter.onClick{
    FragmentAboutUsBinding binding;
    ArrayList<PagerAboutUs> arrayList;
    ArrayList<PagerHome6> arrayList2;
    ArrayList<PagerHome6> arrayList3;
    AboutUsAdapter adapter;
    Home6Viewpaper home6Viewpaper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutUsBinding.inflate(inflater,container,false);
        viewpager();
        viewpager2();
        viewpager3();
        return binding.getRoot();
    }

    private void viewpager3() {
        arrayList3 = new ArrayList<>();
        arrayList3.add(new PagerHome6(R.drawable.abu_image16,"2022"));
        arrayList3.add(new PagerHome6(R.drawable.abu_image17,"2022"));
        arrayList3.add(new PagerHome6(R.drawable.abu_image18,"2022"));
        arrayList3.add(new PagerHome6(R.drawable.abu_image19,"2022"));
        arrayList3.add(new PagerHome6(R.drawable.abu_image20,"2022"));
        home6Viewpaper = new Home6Viewpaper(getActivity(), arrayList2);
        binding.abuViewpager3.setAdapter(home6Viewpaper);
    }

    private void viewpager2() {
        arrayList2 = new ArrayList<>();
        arrayList2.add(new PagerHome6(R.drawable.abu_image12,"Workplace"));
        arrayList2.add(new PagerHome6(R.drawable.abu_image13,"Workplace"));
        arrayList2.add(new PagerHome6(R.drawable.abu_image14,"Friendly Membership"));
        arrayList2.add(new PagerHome6(R.drawable.abu_image15,"Workplace"));
        arrayList2.add(new PagerHome6(R.drawable.abu_image21,"Workplace"));
        home6Viewpaper = new Home6Viewpaper(getActivity(), arrayList2);
        binding.abuViewpager2.setAdapter(home6Viewpaper);
    }

    private void viewpager() {
          arrayList = new ArrayList<>();
          arrayList.add(new PagerAboutUs(R.drawable.abu_image7,"Draw AI - Avatar Generator", "Transform your photos with AI","https://apps.apple.com/us/app/dawn-ai-avatar-generator/id1643890882"));
          arrayList.add(new PagerAboutUs(R.drawable.abu_image8,"Prank air, horn, fart, clipper", "Clipper prank & funny sounds","https://apps.apple.com/us/app/prank-air-horn-fart-clipper/id1623746709"));
          arrayList.add(new PagerAboutUs(R.drawable.abu_image9,"Manga Reader: Top Manga Here", "Best App For Manga & Novel Reader","https://apps.apple.com/us/app/manga-reader-top-manga-here/id1635298030"));
          arrayList.add(new PagerAboutUs(R.drawable.abu_image10,"Face Dance: Photo Animator App", "Lip Sync, Funny Face Animation","https://apps.apple.com/us/app/mimic-ai-photo-face-animator/id1590841930"));
          arrayList.add(new PagerAboutUs(R.drawable.abu_image11,"Celebirity Voice Change Parody", "100+ Voice Live & AI Singing","https://apps.apple.com/us/app/mimic-ai-photo-face-animator/id1590841930"));
          adapter = new AboutUsAdapter(getActivity(), arrayList,AboutUsFragment.this);
          binding.abuViewpager.setAdapter(adapter);

    }

    @Override
    public void onClickItem(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        // Kiểm tra xem có ứng dụng nào có thể mở URL này không
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Mở URL bằng ứng dụng mặc định hoặc trình duyệt web
            startActivity(intent);
        }else {
            Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
        }
    }
}