package com.app.fakewedding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fakewedding.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.Viewholder> {
    Context context;
    List<String> list;
    private OnclickImageUploaded imageUploaded;
    public ImageUploadAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }
     public void setOnclickImageUploaded(OnclickImageUploaded imageUploaded){
        this.imageUploaded = imageUploaded;
     }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_uploaded_image, parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
           String image = list.get(position);
           Picasso.get().load(image).into(holder.imageView);
           holder.itemView.setOnClickListener(v -> {
               if(imageUploaded!= null){
                   imageUploaded.setOnclick(image);
               }
           });
    }

    @Override
    public int getItemCount() {
        if (list != null)
        return list.size();
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item_uploaded);
        }
    }
    public interface OnclickImageUploaded{
        void setOnclick(String image);
    }
}
