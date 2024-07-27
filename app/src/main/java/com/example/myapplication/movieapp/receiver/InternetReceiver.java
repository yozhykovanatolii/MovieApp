package com.example.myapplication.movieapp.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;


import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.util.CheckInternetUtil;
import com.google.android.material.button.MaterialButton;

public class InternetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = CheckInternetUtil.getNetworkInfo(context);
        if(status.equals("disconnected")){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog, null);
            builder.setView(layout_dialog);
            initAlertDialog(builder, layout_dialog, context, intent);
        }
    }

    private void initAlertDialog(AlertDialog.Builder builder, View layout_dialog, Context context, Intent intent){
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        retryInternet(alertDialog, layout_dialog, context, intent);
    }

    private void retryInternet(AlertDialog alertDialog, View layout_dialog, Context context, Intent intent){
        MaterialButton retryButton = layout_dialog.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(view -> {
            alertDialog.dismiss();
            onReceive(context, intent);
        });
    }
}