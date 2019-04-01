package com.borah.manjit.brainshare.dialoginterfaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowReportDialog {
    Context context;
    String key;
    public ShowReportDialog(Context context,String key){
        this.context=context;
        this.key=key;
    }
    public void show(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this.context);
        builder.setCancelable(true).setTitle("Report ?").setMessage("Report if questions or answers are wrong,misleading or inappropriate");
        builder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference();
                dbRef.child("reported").push().setValue(key);
                dialog.dismiss();
                Toast.makeText(context.getApplicationContext(),"Will be reported",Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
