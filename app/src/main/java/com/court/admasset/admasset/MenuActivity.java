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

    // 뒤로가기
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
//                intent.putExtra("id",  sf.getString("id","Nothing"));
//                intent.putExtra("user_name", user_name);
//                intent.putExtra("check_court", check_court);
//                intent.putExtra("group_id", group_id);
//                intent.putExtra("check_id", check_id);
//                intent.putExtra("id_token", id_token);
                startActivity(intent);
                break;

            // List Asset
            case R.id.listViewBtn:
                intent = new Intent(MenuActivity.this,RecyclerViewActivity.class);
                intent.putExtra("status","firstcall");
                startActivity(intent);
                break;

            // Report Asset
            case R.id.assetReportBtn:
                intent = new Intent(MenuActivity.this,SummaryActivity.class);
//                intent.putExtra("id",sf.getString("id","1"));
//                intent.putExtra("user_name",sf.getString("user_name","1"));
//                intent.putExtra("check_court",sf.getString("check_court","1"));
//                intent.putExtra("group_id",sf.getString("group_id","1"));
//                intent.putExtra("id_token", sf.getString("id_token","1"));
                startActivity(intent);
                break;
        }
    }

    //뒤로가기 버튼 - 우선 보여지기만, 제대로 logout 해야함
    @Override
    public void onBackPressed(){
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            this.finish();
            setContentView(R.layout.activity_login);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressing Back once again will log  out", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}