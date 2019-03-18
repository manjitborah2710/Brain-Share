package com.borah.manjit.brainshare;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.borah.manjit.brainshare.dialoginterfaces.ShowErrorOnNetworkUnavailability;
import com.borah.manjit.brainshare.global.GlobalData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyGames extends AppCompatActivity implements View.OnClickListener{
    ListView gamesList;
    Button addBtn;
    DatabaseReference dbRef;
    FirebaseUser user;
    String user_email;
    ArrayList<String> games;
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_games);
        gamesList=findViewById(R.id.my_games_list_my_games_activity);
        addBtn=findViewById(R.id.add_game_my_games_activity);
        addBtn.setOnClickListener(this);
        games=new ArrayList<>();
        adapter=new CustomAdapter(this,games);
        gamesList.setAdapter(adapter);
        final ShowErrorOnNetworkUnavailability showErrorOnNetworkUnavailability=new ShowErrorOnNetworkUnavailability();

        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!showErrorOnNetworkUnavailability.showDialog(parent.getContext())) {
                    Intent intent = new Intent(MyGames.this, AddTopicAndQuestions.class);
                    intent.putExtra("selected_game", games.get(position));
                    startActivity(intent);
                }
            }
        });
        gamesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(parent.getContext());
                builder.setTitle("Confirm Delete").setMessage("Are you sure you want to delete the game?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteGameFromDB(dialog,position);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false);
                AlertDialog dialog=builder.create();
                dialog.show();
                return true;
            }
        });
    }

    boolean s=true;
    private void deleteGameFromDB(DialogInterface dialog, int position) {
        ShowErrorOnNetworkUnavailability showErrorOnNetworkUnavailability=new ShowErrorOnNetworkUnavailability();
        if(!showErrorOnNetworkUnavailability.showDialog(this)) {
            CustomProgressDialog customProgressDialog = new CustomProgressDialog(this, "deleting");
            customProgressDialog.setCancelable(false);
            customProgressDialog.show();
            String games_key, gamenames_key;
            s = true;
            games_key = games.get(position).toLowerCase() + "-" + user_email.toLowerCase().replace(".com", "");
            gamenames_key = games.get(position).toLowerCase().replace(" ", "") + user_email.toLowerCase().replace(".com", "");
            dbRef.child("games").child(games_key).removeValue().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    s = false;
                }
            });
            if (!s) {
                Toast.makeText(getApplicationContext(), "could not delete !", Toast.LENGTH_SHORT).show();
                return;
            }
            dbRef.child("gamenames").child(gamenames_key).removeValue().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    s = false;
                }
            });
            if (!s) {
                Toast.makeText(getApplicationContext(), "could not delete !", Toast.LENGTH_SHORT).show();
                return;
            }
            dbRef.child("users").child(user.getUid()).child("games").child(gamenames_key).removeValue().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    s = false;
                }
            });
            if (!s) {
                Toast.makeText(getApplicationContext(), "could not delete !", Toast.LENGTH_SHORT).show();
                return;
            }
            customProgressDialog.cancel();
            Toast.makeText(getApplicationContext(), "game deleted", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user=((GlobalData)getApplication()).getUser();
        user_email=user.getEmail();
        dbRef= FirebaseDatabase.getInstance().getReference();
        dbRef.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> ds_itr=dataSnapshot.child("games").getChildren();
                for(DataSnapshot ds : ds_itr){
                    String game_name=ds.getValue(String.class);
                    if(!games.contains(game_name)){
                        games.add(game_name);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"No games",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_game_my_games_activity:{
                startActivity(new Intent(MyGames.this,AddTopicAndQuestions.class));
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyGames.this,FirstActivity.class));
    }
}
