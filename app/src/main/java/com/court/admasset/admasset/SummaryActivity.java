package com.court.admasset.admasset;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryActivity extends AppCompatActivity {

    private TextView checkNum;
    private TextView allNum;

    NetworkService service;
    private SharedPreferences sf;

    private AppCompatDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_report);

        // call loading dialog
        progressON(this, null);

        // network
        service = ApplicationController.getInstance().getNetworkService();

        sf = getSharedPreferences("asset",0);

        //get checkNum
        getCheckAllNum();

        checkNum = (TextView)findViewById(R.id.checkedNum);
        allNum = (TextView)findViewById(R.id.allNum);

        Button showchecked = (Button)findViewById(R.id.showchecked);
        Button showWaiting= (Button)findViewById(R.id.showWaiting);

        // showchecked
        showchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SummaryActivity.this, SearchAssetListActivity.class);
                intent1.putExtra("flag","summary");
                startActivity(intent1);
            }
        });
        // showWaiting
        showWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SummaryActivity.this, WaitingAssetActivity.class);
                startActivity(intent1);
            }
        });

    }

    //============ Loading dialog ============//
    public void progressON(Activity activity, String message){
        if(activity == null || activity.isFinishing()){
            return;
        }
        if(progressDialog != null && progressDialog.isShowing()){
            progressSET(message);
        }else{
            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_loading);
            progressDialog.show();
        }
        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }
    }
    public void progressSET(String message) {
        if (progressDialog == null || !progressDialog.isShowing())  {
            return;
        }

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }
    }

    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    //============// Loading dialog ============//


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
                    if(response.body()!=null){
                        progressOFF();

                        checkNum.setText(response.body().result.get(1).flag_count.toString());
                        allNum.setText(response.body().result.get(0).flag_count.toString());
                    }
                }else{
                    Toast.makeText(SummaryActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetReportInfoResult> call, Throwable t) {
                Toast.makeText(SummaryActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}