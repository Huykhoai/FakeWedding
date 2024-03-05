package com.app.fakewedding.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fakewedding.R;
import com.app.fakewedding.model.AlbumSwapped;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumById_Adapter extends RecyclerView.Adapter<AlbumById_Adapter.ViewHolder> {
    Context context;
    ArrayList<AlbumSwapped> list;
    onCickItem onCickItem;
    onCickAlbum onCickAlbum;
    public void setOnclickItem(onCickItem onCickItem){
        this.onCickItem = onCickItem;
    }
    public void setOnclickAlbum(onCickAlbum onCickAlbum){
        this.onCickAlbum = onCickAlbum;
    }
    public AlbumById_Adapter(Context context, ArrayList<AlbumSwapped> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_album_byid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlbumSwapped albumSwapped = list.get(position);
        Picasso.get().load(albumSwapped.getLink_da_swap()).into(holder.imageView);
        holder.txtbtn_view_detail.setOnClickListener(v -> {
            if(onCickItem!= null){
                onCickItem.onClick(albumSwapped.getId_sk_album_byid());
            }
        });
        holder.txtbtn_view_album.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.item_detail_temple);

            Window window = dialog.getWindow();
            if(window == null)
                return;
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            if(Gravity.CENTER == Gravity.CENTER){
                dialog.setCancelable(true);
            }else {
                dialog.setCancelable(false);
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            ImageView imageView = dialog.findViewById(R.id.image_temple_detail);
            Picasso.get().load(albumSwapped.getLink_da_swap()).into(imageView);
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();

    }
    public interface onCickItem{
        void onClick(String id_sk);
    }
    public interface onCickAlbum{
        void onClick(String name);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtbtn_view_album,txtbtn_view_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_swapped);
            txtbtn_view_album = itemView.findViewById(R.id.item_view_album);
            txtbtn_view_detail= itemView.findViewById(R.id.item_view_detail_swapped);
        }
    }
}
