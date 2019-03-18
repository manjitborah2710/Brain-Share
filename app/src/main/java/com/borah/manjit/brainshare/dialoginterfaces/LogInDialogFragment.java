package com.borah.manjit.brainshare.dialoginterfaces;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.borah.manjit.brainshare.R;

public class LogInDialogFragment extends DialogFragment {

    OnClickLogIn onClickLogIn;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View view=inflater.inflate(R.layout.login_dialog_view,null,false);

        final EditText email=view.findViewById(R.id.email_log_in);
        final EditText pwd=view.findViewById(R.id.pwd_login);
        TextView forgotPwd=view.findViewById(R.id.forgot_pwd);
        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onClickLogIn.resetPassword(email.getText().toString());
                getDialog().dismiss();
            }
        });




        Button logIn=view.findViewById(R.id.login_btn_log_in_dialog);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(email.getText().toString().equals("") || pwd.getText().toString().equals(""))){
                    onClickLogIn.logIn(email.getText().toString(),pwd.getText().toString());
                    getDialog().dismiss();
                }
            }
        });






        Button cancel=view.findViewById(R.id.cancel_btn_log_in_dialog);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });




        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setCancelable(false);
        MediaPlayer mediaPlayer=MediaPlayer.create(getActivity(),R.raw.whoosh);
        mediaPlayer.start();
        return builder.create();
    }

    public interface OnClickLogIn{
        public void logIn(String email,String pwd);
        public void resetPassword(String email);
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onClickLogIn= (OnClickLogIn) context;
        } catch (ClassCastException ex) {
            Log.d("mn",ex.getLocalizedMessage()+"\n"+ex.getStackTrace());
        }
    }

}
