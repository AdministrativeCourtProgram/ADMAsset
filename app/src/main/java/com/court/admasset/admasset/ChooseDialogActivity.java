package com.court.admasset.admasset;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChooseDialogActivity extends Dialog {
    private Context context;
    Intent intent;

    public ChooseDialogActivity(Context context){
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosedialog);

        Button Camera = (Button)findViewById(R.id.camerabtn);
        Button Typing = (Button)findViewById(R.id.typingbtn);


        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG","카메라다이얼로그");
                dismiss();
            }
        });

        Typing.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                CallWritecodeDialogActivity();
                Log.v("TAG","쓰기다이얼로그");
            }
        });

    }

    public void CallWritecodeDialogActivity(){
        Log.v("TAG","쓰기인탠트다이얼로그");
        intent = new Intent(getContext(), WritecodeDialogActivity.class);
        getContext().startActivity(intent);
    }
}
