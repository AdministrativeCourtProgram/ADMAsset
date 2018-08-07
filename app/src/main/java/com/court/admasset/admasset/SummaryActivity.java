package com.court.admasset.admasset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Network.NetworkService;
import com.court.admasset.admasset.Model.GetReportInfo;
import com.court.admasset.admasset.Model.GetReportInfoResult;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryActivity extends AppCompatActivity {

    private TextView checkNum;
    private TextView allNum;

    NetworkService service;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_report);

        // network
        service = ApplicationController.getInstance().getNetworkService();

        sf = getSharedPreferences("asset",0);

        //get checkNum
        getCheckAllNum();

        checkNum = (TextView)findViewById(R.id.checkedNum);
        allNum = (TextView)findViewById(R.id.allNum);

        Button showchecked = (Button)findViewById(R.id.showchecked);
        Button showWaiting= (Button)findViewById(R.id.showWaiting);

        showWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SummaryActivity.this, WaitingAssetActivity.class);
                startActivity(intent1);
            }
        });
    }

    //get checkNum
    public void getCheckAllNum(){
        GetReportInfo getReportInfo = new GetReportInfo();
        getReportInfo.id = Integer.parseInt(sf.getString("id","1"));
        getReportInfo.user_name = sf.getString("user_name","1");
        getReportInfo.check_court = Integer.parseInt(sf.getString("check_court","1"));
        getReportInfo.group_id = Integer.parseInt(sf.getString("group_id","1"));

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("Authorization","Bearer " + sf.getString("id_token","1"));

        Call<GetReportInfoResult> getReportInfoResultCall = service.getGetReportInfoResult(map, getReportInfo);
        getReportInfoResultCall.enqueue(new Callback<GetReportInfoResult>() {
            @Override
            public void onResponse(Call<GetReportInfoResult> call, Response<GetReportInfoResult> response) {
                if(response.isSuccessful()){
                   // Toast.makeText(SummaryActivity.this, "통신성공", Toast.LENGTH_SHORT).show();
                    if(response.body()!=null){
                       // Toast.makeText(SummaryActivity.this, "통신성공 2", Toast.LENGTH_SHORT).show();
                        //[{"check_flag":"n","FLAG_COUNT":1599},{"check_flag":"y","FLAG_COUNT":10}]
                        checkNum.setText(response.body().result.get(1).flag_count.toString());
                        allNum.setText(response.body().result.get(0).flag_count.toString());

//                        Toast.makeText(SummaryActivity.this, "통신성공"+response.body().status, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(SummaryActivity.this, "통신성공"+response.body().result.get(0).check_flag, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SummaryActivity.this, "통신실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetReportInfoResult> call, Throwable t) {
                Toast.makeText(SummaryActivity.this, "완전 통신실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
/*
* package com.court.admasset.admasset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Network.NetworkService;
import com.court.admasset.admasset.Model.GetReportInfo;
import com.court.admasset.admasset.Model.GetReportInfoResult;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryActivity extends AppCompatActivity {

    private TextView checkNum;
    private TextView allNum;

    NetworkService service;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_report);

        // network
        service = ApplicationController.getInstance().getNetworkService();

        sf = getSharedPreferences("asset",0);

        //get checkNum
        getCheckAllNum();

        checkNum = (TextView)findViewById(R.id.checkedNum);
        allNum = (TextView)findViewById(R.id.allNum);

        Button showchecked = (Button)findViewById(R.id.showchecked);
        Button showWaiting= (Button)findViewById(R.id.showWaiting);

        showWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SummaryActivity.this, WaitingAssetActivity.class);
                startActivity(intent1);
            }
        });
    }

    //get checkNum
    public void getCheckAllNum(){
        GetReportInfo getReportInfo = new GetReportInfo();
        getReportInfo.id = Integer.parseInt(sf.getString("id","1"));
        getReportInfo.user_name = sf.getString("user_name","1");
        getReportInfo.check_court = Integer.parseInt(sf.getString("check_court","1"));
        getReportInfo.group_id = Integer.parseInt(sf.getString("group_id","1"));

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("Authorization","Bearer " + sf.getString("id_token","1"));

        Call<List<GetReportInfoResult>> getReportInfoResultCall = service.getGetReportInfoResult(map, getReportInfo);
        getReportInfoResultCall.enqueue(new Callback<List<GetReportInfoResult>>() {
            @Override
            public void onResponse(Call<List<GetReportInfoResult>> call, Response<List<GetReportInfoResult>> response) {
                if(response.isSuccessful()){
                    Toast.makeText(SummaryActivity.this, "통신성공", Toast.LENGTH_SHORT).show();
                    if(response.body()!=null){
                        Toast.makeText(SummaryActivity.this, "통신성공 2", Toast.LENGTH_SHORT).show();
                        //[{"check_flag":"n","FLAG_COUNT":1599},{"check_flag":"y","FLAG_COUNT":10}]
                        checkNum.setText(response.body().get(1).FLAG_COUNT.toString());
                        allNum.setText(response.body().get(0).FLAG_COUNT.toString());

                        Toast.makeText(SummaryActivity.this, "통신성공", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SummaryActivity.this, "통신실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetReportInfoResult>> call, Throwable t) {
                Toast.makeText(SummaryActivity.this, "완전 통신실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}*/