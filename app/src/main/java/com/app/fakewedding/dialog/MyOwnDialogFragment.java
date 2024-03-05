package com.app.fakewedding.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyOwnDialogFragment extends DialogFragment {
  private MyOwnDialogListener listener;
  private String dialogTitle;
  private String dialogMessage;
  private int imageSrc;

    public MyOwnDialogFragment(String dialogTitle, String dialogMessage, int imgSrc, MyOwnDialogListener myOwnDialogListener) {
        this.listener = myOwnDialogListener;
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
        this.imageSrc = imgSrc;
    }
    public MyOwnDialogFragment(String dialogTitle, String dialogMessage, int imgSrc) {
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
        this.imageSrc = imgSrc;
    }

    public MyOwnDialogFragment() {
    }

    public MyOwnDialogListener getListener() {
        return listener;
    }

    public void setListener(MyOwnDialogListener listener) {
        this.listener = listener;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getDialogMessage() {
        return dialogMessage;
    }

    public void setDialogMessage(String dialogMessage) {
        this.dialogMessage = dialogMessage;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(int imageSrc) {
        this.imageSrc = imageSrc;
    }

    public interface MyOwnDialogListener{
      void OnConfirm();
  }
  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle(this.dialogTitle);
      builder.setMessage(this.dialogMessage);
      builder.setIcon(this.imageSrc);
      builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            if(listener!= null){
                listener.OnConfirm();
            }
          }
      });
      builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

          }
      });
      return builder.create();
  }
}
