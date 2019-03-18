package com.borah.manjit.brainshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.borah.manjit.brainshare.dialoginterfaces.NewQnDialogFragment;
import com.borah.manjit.brainshare.dialoginterfaces.ShowErrorOnNetworkUnavailability;
import com.borah.manjit.brainshare.global.GlobalData;
import com.borah.manjit.brainshare.recyclerviewadapters.AddNewQuestionRVAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddTopicAndQuestions extends AppCompatActivity implements View.OnClickListener,NewQnDialogFragment.AddNewQnInterface{

    Button addQnBtn,saveBtn;
    RecyclerView recyclerView;
    ArrayList<GameClass> questions;
    AddNewQuestionRVAdapter addNewQuestionRVAdapter;
    EditText topic;
    boolean successful=true;
    FirebaseUser user;
    ShowErrorOnNetworkUnavailability showErrorOnNetworkUnavailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic_and_questions);
        addQnBtn=findViewById(R.id.add_qn_btn_add_topic);
        addQnBtn.setOnClickListener(this);
        recyclerView=findViewById(R.id.recycler_view_add_topic);
        questions=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addNewQuestionRVAdapter=new AddNewQuestionRVAdapter(this,questions);
        recyclerView.setAdapter(addNewQuestionRVAdapter);

        saveBtn=findViewById(R.id.save_btn_add_topic);
        saveBtn.setOnClickListener(this);
        topic=findViewById(R.id.topic_et_add_topic);
        user=((GlobalData)getApplication()).getUser();
        showErrorOnNetworkUnavailability=new ShowErrorOnNetworkUnavailability();



        if(getIntent().getExtras()!=null) {
            downnloadQuestions(getIntent().getExtras().getString("selected_game"));
        }



    }

    private void downnloadQuestions(final String gameName) {
        final CustomProgressDialog customProgressDialog=new CustomProgressDialog(this,false,null,"Downloading..");
        customProgressDialog.show();
        questions.clear();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();
        String call=gameName.toLowerCase()+"-"+user.getEmail().toLowerCase().replace(".com","");
        dbRef.child("games").child(call).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    questions.add(ds.getValue(GameClass.class));
                }
                addNewQuestionRVAdapter.notifyDataSetChanged();
                topic.setText(gameName);
                customProgressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                customProgressDialog.cancel();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_qn_btn_add_topic:{
                NewQnDialogFragment dialogFragment=new NewQnDialogFragment();
                dialogFragment.setCancelable(false);
                dialogFragment.show(getFragmentManager(),"hello world");
                break;
            }
            case R.id.save_btn_add_topic: {
                if (!showErrorOnNetworkUnavailability.showDialog(this)) {
                    if (questions.isEmpty() || topic.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Either topic name is missing or no questions are added", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadQuestions();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void addNewQn(GameClass qn) {
        questions.add(qn);
        addNewQuestionRVAdapter.notifyDataSetChanged();
    }

    public void uploadQuestions(){
        CustomProgressDialog customProgressDialog=new CustomProgressDialog(this,false,null,"Uploading..");
        customProgressDialog.show();

        successful=true;

        final String topicname=topic.getText().toString();
        final String user_email=user.getEmail();

        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference();

        //constant above

        if(getIntent().getExtras()==null){
            dbRef.child("users").child(user.getUid()).child("games").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(topicname.toLowerCase().replace(" ","")+user_email.toLowerCase().replace(".com",""))){
                        successful=false;
                        Toast.makeText(getApplicationContext(),"Topic already exists",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        if(!successful) {
            Toast.makeText(getApplicationContext(),"could not upload !",Toast.LENGTH_SHORT).show();
            customProgressDialog.cancel();
            return;
        }







        deletePrevData(dbRef,topicname,user_email);

        dbRef.child("users").child(user.getUid()).child("name").setValue(user_email).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successful=false;
            }
        });

        if(!successful) {
            Toast.makeText(getApplicationContext(),"could not upload !",Toast.LENGTH_SHORT).show();
            customProgressDialog.cancel();
            return;
        }

        dbRef.child("users").child(user.getUid()).child("games").child(topicname.toLowerCase().replace(" ","")+user_email.toLowerCase().replace(".com","")).setValue(topicname).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successful=false;
            }
        });


        if(!successful) {
            Toast.makeText(getApplicationContext(),"could not upload !",Toast.LENGTH_SHORT).show();
            customProgressDialog.cancel();
            return;
        }


        dbRef.child("gamenames").child(topicname.toLowerCase().replace(" ","")+user_email.toLowerCase().replace(".com","")).setValue(topicname+"-"+user_email.replace(".com","")).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successful=false;
            }
        });

        if(!successful) {
            Toast.makeText(getApplicationContext(),"could not upload !",Toast.LENGTH_SHORT).show();
            customProgressDialog.cancel();
            return;
        }





        for(int i=0;i<questions.size();i++){
            successful=true;
            dbRef.child("games").child(topicname.toLowerCase()+"-"+user_email.toLowerCase().replace(".com","")).child("qn"+(i+1)).setValue(questions.get(i)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    successful=false;
                }
            });
            if(!successful) {
                Toast.makeText(getApplicationContext(),"could not upload !",Toast.LENGTH_SHORT).show();
                customProgressDialog.cancel();
                return;
            }

        }
        customProgressDialog.cancel();
        Toast.makeText(getApplicationContext(),"Successfully uploaded questions",Toast.LENGTH_SHORT).show();

    }

    private void deletePrevData(final DatabaseReference dbRef, final String topic, final String email) {

        dbRef.child("games").child(topic.toLowerCase()+"-"+email.toLowerCase().replace(".com","")).removeValue();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddTopicAndQuestions.this,MyGames.class));
    }
}
