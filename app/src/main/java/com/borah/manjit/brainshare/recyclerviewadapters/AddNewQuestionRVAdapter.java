package com.borah.manjit.brainshare.recyclerviewadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.borah.manjit.brainshare.GameClass;
import com.borah.manjit.brainshare.R;

import java.util.ArrayList;

public class AddNewQuestionRVAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<GameClass> data;

    public AddNewQuestionRVAdapter(Context context, ArrayList<GameClass> data) {
        this.context = context;
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.display_my_qns_view,parent,false);
        SimpleViewHolder simpleViewHolder=new SimpleViewHolder(view);
        return simpleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        SimpleViewHolder simpleViewHolder=(SimpleViewHolder)holder;
        simpleViewHolder.setTextInSpace(data.get(position));
        simpleViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"deleted item at position "+position,Toast.LENGTH_SHORT).show();
                data.remove(position);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder{
        TextView qnAndAns;
        Button deleteBtn;
        public SimpleViewHolder(View itemView){
            super(itemView);
            qnAndAns=itemView.findViewById(R.id.display_qn_and_answer);
            deleteBtn=itemView.findViewById(R.id.delete_qn);

        }
        public void setTextInSpace(GameClass obj){
            String s="Qn : "+obj.qn+"\nOption 1 : "+obj.op1+"\nOption 2 : "+obj.op2+"\nOption 3 : "+obj.op3+"\nOption 4 : "+obj.op4+"\n\nAnswer : Option "+obj.ans;
            qnAndAns.setText(s);
        }
    }


}
