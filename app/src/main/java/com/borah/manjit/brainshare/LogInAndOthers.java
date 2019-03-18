package com.borah.manjit.brainshare;

import android.app.DialogFragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.borah.manjit.brainshare.dialoginterfaces.LogInDialogFragment;
import com.borah.manjit.brainshare.dialoginterfaces.ShowErrorOnNetworkUnavailability;
import com.borah.manjit.brainshare.dialoginterfaces.SignUpDialogFragment;
import com.borah.manjit.brainshare.global.GlobalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInAndOthers extends AppCompatActivity implements LogInDialogFragment.OnClickLogIn, SignUpDialogFragment.OnClickSignUp {

    int count;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_and_others);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        setGloabalData(null,null);


        count=0;
    }

    private void setGloabalData(FirebaseAuth auth,FirebaseUser user) {
        try{
            ((GlobalData) getApplication()).setAuthToken(auth);
            ((GlobalData) getApplication()).setUser(user);
        }
        catch (ClassCastException ex){

        }

    }

    public void displayDialog(View v){
        if(!(new ShowErrorOnNetworkUnavailability()).showDialog(this)){
            switch (v.getId()){
                case R.id.sign_up_btn:{
                    SignUpDialogFragment signUpDialogFragment=new SignUpDialogFragment();
                    signUpDialogFragment.setCancelable(false);

                    signUpDialogFragment.show(getFragmentManager(),"signupdialog");

                    break;
                }
                case R.id.log_in_btn:{
                    DialogFragment fragment=new LogInDialogFragment();
                    fragment.setCancelable(false);

                    fragment.show(getFragmentManager(),"logindialog");
                    break;
                }
                case R.id.skip:{
                    MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.click_sound);
                    mediaPlayer.start();
                    startActivity(new Intent(LogInAndOthers.this,FirstActivity.class));
                    break;
                }
            }
        }

    }

    @Override
    public void logIn(final String email, String pwd) {
        final CustomProgressDialog customProgressDialog=new CustomProgressDialog(this,"Logging you in");
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();
        auth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                customProgressDialog.cancel();
                if(task.isSuccessful()){
                    setGloabalData(auth,task.getResult().getUser());
                    Toast.makeText(getApplicationContext(),"Logged in as "+email,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogInAndOthers.this,FirstActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage()+"",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void resetPassword(final String email) {
        if(!email.isEmpty()){
            auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"reset link sent to "+email,Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"empty email field",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void signUp(String email, String pwd) {
        final CustomProgressDialog dialog=new CustomProgressDialog(this,"Signing up");
        dialog.setCancelable(false);
        dialog.show();
        auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.cancel();
                if(task.isSuccessful()){
//                    Toast.makeText(getApplicationContext(),"Account created..Log In to continue "+task.getResult().getUser(),Toast.LENGTH_SHORT).show();
                    setGloabalData(auth,task.getResult().getUser());
                    startActivity(new Intent(LogInAndOthers.this,FirstActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),""+task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        count++;
        if(count>1){
            finishAffinity();
        }
        Toast.makeText(this,"press one more time to exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count=0;
            }
        },2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CustomProgressDialog dialog=new CustomProgressDialog(this,"checking for logins");
        dialog.setCancelable(false);
        dialog.show();
        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            dialog.cancel();
            startActivity(new Intent(LogInAndOthers.this,FirstActivity.class));
            setGloabalData(auth,user);
        }
        else{
            dialog.cancel();
            Toast.makeText(getApplicationContext(),"Not logged in",Toast.LENGTH_SHORT).show();
        }

    }
}
