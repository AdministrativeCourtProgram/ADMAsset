package com.court.admasset.admasset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sf = getSharedPreferences("asset",0);
    }
    public void onClick(View view){
        Intent intent = null;

        switch (view.getId()){
            // Check Asset
            case R.id.checkAssetBtn:
                intent = new Intent(MenuActivity.this, BarcodeReaderActivity.class);
                startActivity(intent);
                break;

            // List Asset
            case R.id.listViewBtn:
                intent = new Intent(MenuActivity.this,ListViewActivity.class);
                intent.putExtra("status","firstcall");
                startActivity(intent);
                break;

            // Report Asset
            case R.id.assetReportBtn:
                intent = new Intent(MenuActivity.this,SummaryActivity.class);
                startActivity(intent);
                break;

            case R.id.logOutBtn:
                logOut();
        }
    }

    public void logOut(){
        // SharedPreferences datas remove
        SharedPreferences.Editor editor = sf.edit();
        editor.remove("id");
        //editor.remove("user_name");
        editor.remove("check_court");
        editor.remove("group_id");
        //editor.remove("check_id");
        editor.remove("id_token");
        editor.remove("refreshToken");
       //editor.remove("loginDate");
        editor.commit();
        this.finish();
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            logOut();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressing Back once again will log out", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}