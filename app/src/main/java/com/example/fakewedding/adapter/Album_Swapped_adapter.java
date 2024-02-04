package com.example.fakewedding.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakewedding.R;
import com.example.fakewedding.model.AlbumSwapped;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Album_Swapped_adapter extends RecyclerView.Adapter<Album_Swapped_adapter.ViewHolder> {
     Context context;
     ArrayList<AlbumSwapped> list;
     onCickItem onCickItem;
     public void setOnclickItem(onCickItem onCickItem){
         this.onCickItem = onCickItem;
     }
    public Album_Swapped_adapter(Context context, ArrayList<AlbumSwapped> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_image_swapped, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          AlbumSwapped albumSwapped = list.get(position);
        Picasso.get().load(albumSwapped.getLink_da_swap()).into(holder.imageView);
        holder.txtdate.setText(albumSwapped.getThoigian_sukien());
        holder.txtbtn_view_detail.setOnClickListener(v -> {
         if(onCickItem!= null){
             onCickItem.onClick(albumSwapped.getId_sk_album());
         }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();

    }
    public interface onCickItem{
        void onClick(String id_sk);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtdate,txtbtn_view_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_swapped);
            txtdate = itemView.findViewById(R.id.item_txtdate_swapped);
            txtbtn_view_detail= itemView.findViewById(R.id.item_view_detail_swapped);
        }
    }
}
