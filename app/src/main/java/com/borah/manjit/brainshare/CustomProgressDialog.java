package com.borah.manjit.brainshare;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CustomProgressDialog extends AlertDialog {
    String displayText;
    TextView text;

    protected CustomProgressDialog(Context context,String displayText) {
        super(context);
        this.displayText=displayText;
    }

    protected CustomProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener,String displayText) {
        super(context, cancelable, cancelListener);
        this.displayText=displayText;
    }

    protected CustomProgressDialog(Context context, int themeResId, String displayText) {
        super(context, themeResId);
        this.displayText=displayText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog_view);
        text=findViewById(R.id.content_text_progress_dialog);
        text.setText(this.displayText);
    }
}
