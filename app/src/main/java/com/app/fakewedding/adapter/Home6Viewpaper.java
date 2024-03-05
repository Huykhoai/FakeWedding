package com.app.fakewedding.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.fakewedding.R;
import com.app.fakewedding.model.PagerHome6;

import java.util.List;

public class Home6Viewpaper extends PagerAdapter {
    Context context;
    List<PagerHome6> list;

    public Home6Viewpaper(Context context, List<PagerHome6> list) {
        this.context = context;
        this.list = list;
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
        View view = inflater.inflate(R.layout.layout_home6, container,false);
        ImageView imageView = view.findViewById(R.id.imagehome6);
        TextView textView = view.findViewById(R.id.txthome6);
         PagerHome6 home6 = list.get(position);
         imageView.setImageResource(home6.getImage());
         textView.setText(home6.getTitle());
        Log.d("TAG", "instantiateItem: "+home6.getTitle());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
