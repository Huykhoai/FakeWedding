package com.example.fakewedding.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakewedding.R;
import com.example.fakewedding.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
    Context context;
    List<Category> arrayList;

    public CategoryAdapter(Context context, List<Category> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CategoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_category,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Viewholder holder, int position) {
           Category category = arrayList.get(position);
           holder.txtTitle.setText(category.getName_cate());
        Log.d("getname", "onBindViewHolder: "+category.getName_cate());
        Picasso.get().load(category.getImage_sample()).into(holder.imageView);
        holder.btn_start.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        if(arrayList != null)
            return arrayList.size();
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txtTitle;
        Button btn_start;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_category);
            txtTitle = itemView.findViewById(R.id.title_category);
            btn_start = itemView.findViewById(R.id.btn_category);
        }
    }
}
