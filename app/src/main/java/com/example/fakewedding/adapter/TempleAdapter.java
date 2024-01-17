package com.example.fakewedding.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fakewedding.R;
import com.example.fakewedding.model.Temple;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TempleAdapter extends RecyclerView.Adapter<TempleAdapter.TempleViewHolder> {
    Context context;
    private List<Temple> templeList;
    private OnItemClickListener listener;
    private OnItemClickListenerDetail listenerDetail;

    public interface OnItemClickListener {
        void onItemClick(Temple temple);
    }
    public interface OnItemClickListenerDetail {
        void onItemClick(Temple temple);
    }
    public TempleAdapter(Context context, List<Temple> templeList) {
        this.context = context;
        this.templeList = templeList;
    }
    public void setOnclickTemple(OnItemClickListener onclickTemple){
        this.listener = onclickTemple;
    }
    public void setOnCickDetail(OnItemClickListenerDetail onCickDetail){
        this.listenerDetail = onCickDetail;
    }

    @NonNull
    @Override
    public TempleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_temple, parent, false);
        return new TempleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempleViewHolder holder, int position) {
        Temple temple = templeList.get(position);
        Picasso.get().load(temple.getImage()).into(holder.imageView);
        holder.btnCreateNow.setOnClickListener(v -> {
            listener.onItemClick(temple);
        });
        holder.btnDetail.setOnClickListener(v -> {
            listenerDetail.onItemClick(temple);
        });
    }

    @Override
    public int getItemCount() {
        return templeList.size();
    }

    static class TempleViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView btnCreateNow;
        TextView btnDetail;

        TempleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            btnCreateNow = itemView.findViewById(R.id.btnCreateNow);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
