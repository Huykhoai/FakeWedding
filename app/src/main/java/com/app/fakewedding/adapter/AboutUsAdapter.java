package com.app.fakewedding.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.fakewedding.R;
import com.app.fakewedding.model.PagerAboutUs;

import java.util.ArrayList;

public class AboutUsAdapter extends PagerAdapter {
    Context context;
    ArrayList<PagerAboutUs> list;
    onClick onClick;
    public AboutUsAdapter(Context context, ArrayList<PagerAboutUs> list, onClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pager_about_us, container,false);
        ImageView imageView = view.findViewById(R.id.item_viewpager_image_aboutus);
        TextView title = view.findViewById(R.id.item_viewpager_title_aboutus);
        TextView messege = view.findViewById(R.id.item_viewpager_messege_aboutus);
        PagerAboutUs pagerAboutUs = list.get(position);
        imageView.setImageResource(pagerAboutUs.getImage());
        title.setText(pagerAboutUs.getTitle());
        messege.setText(pagerAboutUs.getMessege());
        title.setOnClickListener(v -> {
            onClick.onClickItem(pagerAboutUs.getUrl());
        });
        container.addView(view);
        return view;
    }
    public interface onClick{
        void onClickItem(String url);
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

