package com.court.admasset.admasset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.court.admasset.admasset.Model.ScanInfoResult;
import com.court.admasset.admasset.Model.SearchAssetResult;
import com.court.admasset.admasset.Model.SendMsgInfo;
import com.court.admasset.admasset.Model.SendMsgResult;
import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Network.NetworkService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMsgActivity extends AppCompatActivity {
    Intent intent;

    private String asset_name;
    private String asset_no;
    private String organization;
    private TextView numview;
    private TextView nameview;
    private TextView orview;
    private EditText notice_msg;
    private String id;
    private String user_name;
    private String check_court;
    private String group_id;
    private Map<String, String> map;
    private Button sendMsgBtn;
    private SendMsgInfo sendMsgInfo;
    private int flag;

    NetworkService service;

    private SharedPreferences sf;
    private ArrayList<ScanInfoResult.ScanData> scanResult=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);


        intent = getIntent();
//        flag = intent.getIntExtra("flag",1);
//        if(flag == 0) {
            scanResult = (ArrayList<ScanInfoResult.ScanData>) intent.getSerializableExtra("result");

            asset_no = scanResult.get(0).asset_no;
            asset_name = scanResult.get(0).asset_name;
            organization = scanResult.get(0).organization;
//        }
//        else{
//            asset_no = intent.getStringExtra(asset_no);
//            asset_name = intent.getStringExtra(asset_name);
//            organization = intent.getStringExtra(organization);
//        }

        // ====== home Button ====== //
        ImageButton homeBtn = (ImageButton)findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(RecyclerViewActivity.this, MenuActivity.class);
                Intent intent = new Intent(SendMsgActivity.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        Log.v("TAG","1111111111111"+asset_no+asset_name+organization);
        InitView();
        initClickEvent();

    }

    public void InitView() {
        numview = (TextView) findViewById(R.id.assetNum);
        nameview = (TextView) findViewById(R.id.assetName);
        orview = (TextView) findViewById(R.id.assetOrgan);
        notice_msg = (EditText) findViewById(R.id.textMsg);
        sendMsgBtn = (Button) findViewById(R.id.sendMsgBtn);

        numview.setText(" : "+asset_no);
        nameview.setText(" : "+asset_name);
        orview.setText(" : "+organization);

        sf = getSharedPreferences("asset", 0);

        id = sf.getString("id", "1");
        user_name = sf.getString("user_name", "1");
        check_court = sf.getString("check_court", "1");
        group_id = sf.getString("group_id", "1");

        map = new HashMap<>();
        map.put("Content-Type", "application/json");
        map.put("Authorization", "Bearer " + sf.getString("id_token", "1"));
    }

    private void initClickEvent() {
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                service = ApplicationController.getInstance().getNetworkService();
                sendMsgInfo = new SendMsgInfo();
                sendMsgInfo.id = Integer.parseInt(id);
                sendMsgInfo.user_name = user_name;
                sendMsgInfo.check_court = Integer.parseInt(check_court);
                sendMsgInfo.group_id = Integer.parseInt(group_id);
                sendMsgInfo.asset_no = asset_no;
                sendMsgInfo.notice_msg = notice_msg.getText().toString();

                if (notice_msg.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter notice", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.v("TAG","000000000"+sendMsgInfo.id+"000000000");

                Call<SendMsgResult> sendMsgResultCall = service.getSendMsgResult(map,sendMsgInfo);
                sendMsgResultCall.enqueue(new Callback<SendMsgResult>() {
                    @Override
                    public void onResponse(Call<SendMsgResult> call, Response<SendMsgResult> response) {
                        Toast.makeText(SendMsgActivity.this, "Network communication success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<SendMsgResult> call, Throwable t) {
                        Toast.makeText(SendMsgActivity.this, "Network communication failure", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(SendMsgActivity.this, MenuActivity.class);
                startActivity(intent);
                }
            });
    }

}
