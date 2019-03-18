package com.borah.manjit.brainshare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.cardemulation.HostApduService;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.borah.manjit.brainshare.dialoginterfaces.ShowErrorOnNetworkUnavailability;
import com.borah.manjit.brainshare.global.GlobalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener{

    Context context;
    TextView tv_app_name;
    final String APP_NAME="Brain Share";

    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    ArrayList<String> del_from_games;
    ArrayList<String> del_from_gamenames;
    ListView lv;
    int count;
    Button createQuiz,logout,deleteAcc;

    FirebaseUser user;
    String user_email;
    String user_email_stripped;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity_main_layout);
        context=this;



        final CustomProgressDialog dialog=new CustomProgressDialog(this,"Loading....");
        dialog.setCancelable(false);

        dialog.show();



        count=0;


        logout=findViewById(R.id.log_out_btn_first_activity);
        logout.setOnClickListener(this);
        createQuiz=findViewById(R.id.create_your_own_quiz_btn);
        createQuiz.setOnClickListener(this);
        deleteAcc=findViewById(R.id.delete_account_btn);
        deleteAcc.setOnClickListener(this);
        del_from_gamenames=new ArrayList<>();
        del_from_games=new ArrayList<>();




        lv=findViewById(R.id.list_of_games);

        list=new ArrayList<String>();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("gamenames");
        final ShowErrorOnNetworkUnavailability showErrorOnNetworkUnavailability=new ShowErrorOnNetworkUnavailability();


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> itr=dataSnapshot.getChildren();
                for(DataSnapshot a : itr){
                    list.add(a.getValue(String.class));
                }
                adapter=new CustomAdapter(getApplicationContext(),list);

                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (!showErrorOnNetworkUnavailability.showDialog(context)) {
                            String s = (String) lv.getItemAtPosition(position);
                            MediaPlayer mediaPlayer = MediaPlayer.create(FirstActivity.this, R.raw.click_sound);
                            mediaPlayer.start();
                            startActivity(new Intent(FirstActivity.this, MainActivity.class).putExtra("game", s));
                        }
                    }
                });

                dialog.cancel();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FirstActivity.this,LogInAndOthers.class));
    }


    @Override
    public void onClick(View v) {
        ShowErrorOnNetworkUnavailability showErrorOnNetworkUnavailability=new ShowErrorOnNetworkUnavailability();

        if(!showErrorOnNetworkUnavailability.showDialog(context)) {
            switch (v.getId()) {
                case R.id.log_out_btn_first_activity: {
                    try {
                        ((GlobalData) getApplication()).getAuthToken().signOut();

                        startActivity(new Intent(FirstActivity.this, LogInAndOthers.class));
                    } catch (ClassCastException ex) {

                    }
                    break;
                }
                case R.id.create_your_own_quiz_btn: {
                    startActivity(new Intent(FirstActivity.this, MyGames.class));
                    break;
                }
                case R.id.delete_account_btn: {
                    deleteAccOnConfirmation();
                    break;
                }
            }
        }
    }


    private void deleteAccOnConfirmation() {
        final CustomProgressDialog customProgressDialog=new CustomProgressDialog(this,false,null,"collecting user data ...");
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false).setTitle("Are you sure you want to delete the account ?").setMessage("This will delete all your user data and the games that you created").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(user!=null){
                    customProgressDialog.show();
                    final DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();
                    //delete from game names
                    dbRef.child("users").child(user.getUid()).child("games").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                final String games_key=ds.getValue(String.class).toLowerCase()+"-"+user_email_stripped.toLowerCase();

                                del_from_games.add(games_key);

                                final String gamenames_key=ds.getValue(String.class).toLowerCase().replace(" ","")+user_email_stripped.toLowerCase();

                                del_from_gamenames.add(gamenames_key);
                            }
                            customProgressDialog.cancel();
                            deleteData(del_from_games,del_from_gamenames);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            customProgressDialog.cancel();
                        }
                    });

                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog=builder.create();
        dialog.show();

    }
    public void deleteData(ArrayList<String> del_from_games,ArrayList<String> del_from_gamenames){
        final CustomProgressDialog customProgressDialog=new CustomProgressDialog(this,false,null,"deleting user ...");
        customProgressDialog.show();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();
        for(String s:del_from_games){
            dbRef.child("games").child(s).removeValue().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    customProgressDialog.cancel();
                    return;
                }
            });
        }
        for(String s : del_from_gamenames){
            dbRef.child("gamenames").child(s).removeValue().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    customProgressDialog.cancel();
                    return;
                }
            });
        }
        dbRef.child("users").child(user.getUid()).removeValue().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                customProgressDialog.cancel();
                return;
            }
        });

        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    customProgressDialog.cancel();
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(),"Deleted user",Toast.LENGTH_SHORT).show();
                    ((GlobalData)getApplication()).setUser(null);
                    ((GlobalData)getApplication()).setAuthToken(null);
                    startActivity(new Intent(FirstActivity.this,LogInAndOthers.class));
                    return;
                }
                else{
                    customProgressDialog.cancel();
                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        if(user!=null){
            logout.setVisibility(View.VISIBLE);
            createQuiz.setVisibility(View.VISIBLE);
            deleteAcc.setVisibility(View.VISIBLE);
            user_email=user.getEmail();
            user_email_stripped=user_email.replace(".com","");
        }

        ((GlobalData)getApplication()).setAuthToken(auth);
        ((GlobalData)getApplication()).setUser(user);

    }
}
