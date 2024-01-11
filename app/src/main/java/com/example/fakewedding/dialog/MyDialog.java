package com.example.fakewedding.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDialog extends Dialog {
    public static MyDialog instance;
    public String content;
    public Context context;
    public String contentButton;
    public String title;

    public MyDialog(@NonNull Context context) {
        super(context);
    }

    public MyDialog(@NonNull Context context, String title, String content, String contentButton) {
        super(context);
        this.content = content;
        this.context = context;
        this.title = title;
        this.contentButton = contentButton;
    }
    public static  MyDialog getInstance(Context context){
      if(instance== null){
        instance = new MyDialog(context);
      }
      return instance;
    }
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
}
