package com.example.fakewedding.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fakewedding.R;

public class MyDialog extends Dialog {
    public static MyDialog instance;
    public String content;

    Context context;
    public String contentButton;
    public String title;


    public static MyDialog getInstance() {
        return instance;
    }

    public static void setInstance(MyDialog instance) {
        MyDialog.instance = instance;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public void setContext(Context context) {
        this.context = context;
    }

    public String getContentButton() {
        return contentButton;
    }

    public void setContentButton(String contentButton) {
        this.contentButton = contentButton;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MyDialog(@NonNull Context context, String title, String content, String contentButton) {
        super(context);
        this.content = content;
        this.context = context;
        this.contentButton = contentButton;
        this.title = title;
    }

    public MyDialog(@NonNull Context context) {
        super(context);
    }

    public static MyDialog getInstance(Context context){
        if (instance==null){
            instance = new MyDialog(context);
        }
        return instance;
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_dialog);
        setCancelable(false);
        TextView tvTitle =  findViewById(R.id.tv_title_dialog);
        TextView tvContent = findViewById(R.id.tv_content_dialog);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button button =  findViewById(R.id.btn_dialog);

        if (title!=null&&!title.trim().equals("")){
            tvTitle.setText(title);
        }
        tvContent.setText(content);


        if (contentButton != null && !contentButton.trim().equals("")) {
            button.setVisibility(View.VISIBLE);
            button.setText(contentButton);
        } else {
            button.setVisibility(View.GONE);
        }

        button.setOnClickListener(view -> MyDialog.this.dismiss());
    }
}

