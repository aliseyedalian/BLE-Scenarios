package com.example.blesenarios;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

public abstract class MakeDialog {
    private AlertDialog.Builder dialogBuilder;
    private View dialogView;
    private final AlertDialog alertDialog;
    public MakeDialog(@NonNull Context context, @NonNull @LayoutRes int idLayout) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogBuilder = new AlertDialog.Builder(context);
        dialogView = layoutInflater.inflate(idLayout, null);
        dialogBuilder.setView(dialogView);
        init();
        alertDialog = dialogBuilder.create();
    }
    protected View getDialogView() {
        return dialogView;
    }
    public void dismiss(){
        alertDialog.dismiss();
    }
    protected abstract void init();

    public void show(){
        alertDialog.show();
    }

    public void setDialogShape(@DrawableRes int idShape){
        alertDialog.getWindow().setBackgroundDrawableResource(idShape);
    }
    public AlertDialog.Builder getDialogBuilder() {
        return dialogBuilder;
    }
}
