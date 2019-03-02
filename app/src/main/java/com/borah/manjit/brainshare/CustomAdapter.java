package com.borah.manjit.brainshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(@NonNull Context context, ArrayList<String> resource) {
        super(context,R.layout.list_display,resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v= LayoutInflater.from(getContext()).inflate(R.layout.list_display,null);
        TextView ltv=v.findViewById(R.id.list_text);
        ltv.setText(getItem(position));
        return v;
    }
}
