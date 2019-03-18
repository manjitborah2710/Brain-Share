package com.borah.manjit.brainshare.dialoginterfaces;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.borah.manjit.brainshare.R;

public class SignUpDialogFragment extends DialogFragment {

    OnClickSignUp onClickSignUp;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.sign_up_dialog_view,null,false);
        final EditText email=view.findViewById(R.id.email_sign_up);
        final EditText pwd=view.findViewById(R.id.pwd_sign_up);
        final EditText cpwd=view.findViewById(R.id.confirm_pwd_sign_up);
        Button cancel,signup;

        cancel=view.findViewById(R.id.cancel_btn_sign_up_dialog);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        signup=view.findViewById(R.id.signup_btn_sign_up_dialog);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(email.getText().toString().equals("") || pwd.getText().toString().equals("") || cpwd.getText().toString().equals(""))){
                    onClickSignUp.signUp(email.getText().toString(),pwd.getText().toString());
                    getDialog().dismiss();
                }
            }
        });

        builder.setView(view);
        MediaPlayer mediaPlayer=MediaPlayer.create(getActivity(),R.raw.whoosh);
        mediaPlayer.start();
        return builder.create();
    }
    public interface OnClickSignUp{
        public void signUp(String email,String pwd);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            onClickSignUp=(OnClickSignUp) context;
        }
        catch (ClassCastException e){
            e.printStackTrace();
        }
    }

}
