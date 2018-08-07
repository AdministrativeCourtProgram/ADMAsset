package com.court.admasset.admasset;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.court.admasset.admasset.Model.CheckedListResult;

import retrofit2.Callback;


public class DuplicateDialogActivity extends Dialog {

    public DuplicateDialogActivity(Context context){
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_dialog);

        Button okBtn = (Button)findViewById(R.id.exitbtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
