package com.borah.manjit.brainshare.dialoginterfaces;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.borah.manjit.brainshare.GameClass;
import com.borah.manjit.brainshare.R;

public class NewQnDialogFragment extends DialogFragment {
    EditText qn,op1,op2,op3,op4;
    RadioButton selected_btn;
    RadioGroup radioGroup;
    Button add,cancel;
    int ans=1;

    AddNewQnInterface addNewQnInterface;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.new_qn_dialog_view,null,false);
        qn=view.findViewById(R.id.qn_add_qn_dialog);
        op1=view.findViewById(R.id.op1_add_qn_dialog);
        op2=view.findViewById(R.id.op2_add_qn_dialog);
        op3=view.findViewById(R.id.op3_add_qn_dialog);
        op4=view.findViewById(R.id.op4_add_qn_dialog);
        radioGroup=view.findViewById(R.id.radiogroup_ans_add_qn_dialog);

        add=view.findViewById(R.id.add_qn_add_qn_dialog);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(qn.getText().toString().isEmpty() || op1.getText().toString().isEmpty() || op2.getText().toString().isEmpty() || op3.getText().toString().isEmpty() || op4.getText().toString().isEmpty())){
                    try{
                        switch (radioGroup.getCheckedRadioButtonId()){
                            case R.id.op1_radio_btn_new_qn_dialog:
                                ans=1;
                                break;
                            case R.id.op2_radio_btn_new_qn_dialog:
                                ans=2;
                                break;
                            case R.id.op3_radio_btn_new_qn_dialog:
                                ans=3;
                                break;
                            case R.id.op4_radio_btn_new_qn_dialog:
                                ans=4;
                                break;
                        }
                        addNewQnInterface.addNewQn(new GameClass(ans,op1.getText().toString(),op2.getText().toString(),op3.getText().toString(),op4.getText().toString(),qn.getText().toString()));
                        getDialog().dismiss();
                    }
                    catch (Exception ex){
                        Toast.makeText(getActivity(),ex.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"fields must not be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });





        cancel=view.findViewById(R.id.cancel_add_qn_dialog);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public interface AddNewQnInterface{
        public void addNewQn(GameClass qn);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            this.addNewQnInterface=(AddNewQnInterface) context;
        }
        catch (ClassCastException ex){
            ex.printStackTrace();
        }
    }
}
