package com.court.admasset.admasset;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotFoundDialogActivity extends Dialog{

    public NotFoundDialogActivity(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notfound);

        Button okBtn = (Button)findViewById(R.id.exitbtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
