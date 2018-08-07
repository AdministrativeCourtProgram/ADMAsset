package com.court.admasset.admasset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SendMsgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);

    }
    public void onClick(View view){
        if(view.getId() == R.id.sendMsgBtn){
            Intent intent = new Intent(SendMsgActivity.this,MenuActivity.class);
            startActivity(intent);
        }
    }

}
