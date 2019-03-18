package com.borah.manjit.brainshare.dialoginterfaces;

import android.app.AlertDialog;
import android.content.Context;

import com.borah.manjit.brainshare.CheckConnectivity;

public class ShowErrorOnNetworkUnavailability {
    public boolean showDialog(Context context){
        CheckConnectivity checkConnectivity=new CheckConnectivity(context);
        if(!checkConnectivity.isConnected()){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage("Not connected to the internet").setTitle("Oooops !!!").setCancelable(true);
            builder.create().show();
            return true;
        }
        return false;
    }
}
