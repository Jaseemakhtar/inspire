package com.jsync.freebook;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by sareen on 3/3/18.
 */

public class CustomDialog extends DialogFragment {
    public CustomDialogClickListener mListener;

    View view;
    EditText edtOldBookName;
    EditText edtOldBookDesc;

    TextView txtOldBookImagePath;

    public void setmListener(CustomDialogClickListener mListener){
        this.mListener = mListener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.old_books_entry_dailog,null);
        edtOldBookName = view.findViewById(R.id.edtOldBookName);
        edtOldBookDesc = view.findViewById(R.id.edtOldBookDesc);


        Button btnSelectOldBookImage = view.findViewById(R.id.btnOldBookSelect);
        Button btnUploadOldBook = view.findViewById(R.id.btnOldBookUpload);
        Button btnCancel = view.findViewById(R.id.btnOldBookCancel);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                CustomDialog.this.dismiss();
            }
        });

        btnSelectOldBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickListener(view,null,null);
            }
        });
        btnUploadOldBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickListener(view,edtOldBookName.getText().toString(),edtOldBookDesc.getText().toString());
            }
        });

        builder.setView(view);
        return builder.create();
    }
    public interface CustomDialogClickListener{
        void onClickListener(View view, String oldBookName, String oldBookDesc);
    }


}
