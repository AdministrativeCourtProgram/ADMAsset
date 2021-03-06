package com.court.admasset.admasset;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.court.admasset.admasset.Model.GetReportInfo;
import com.court.admasset.admasset.Model.SearchAssetResult;
import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Network.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAssetListActivity extends AppCompatActivity {

    private TextView txtListSize;
    private RecyclerView reView;
    private AppCompatDialog progressDialog;
    private SearchAssetListAdapter adapter;
    private ArrayList<SearchAssetResult.CheckedData> assetList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_asset_list);

        // ====== home Button ====== //
        ImageButton homeBtn = (ImageButton)findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(RecyclerViewActivity.this, MenuActivity.class);
                Intent intent = new Intent(SearchAssetListActivity.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // ===== init ===== //
        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");

        if(flag.equals("waiting")){
            assetList = (ArrayList<SearchAssetResult.CheckedData>)intent.getSerializableExtra("searchAssetList");
            adapterConnect();
        }else if(flag.equals("summary")){
            // call loading dialog
            progressON(this, null);

            // call //
            callAllCheckedList();
        }
    }

    public void adapterConnect(){
        if(assetList != null){
            // ====== RecyclerView setting ======= //
            reView = (RecyclerView)findViewById(R.id.recyclerAssetList);
            reView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);             //리니어레이아웃의 형태이면 방향은 수직
            reView.setLayoutManager(layoutManager);//리사이클러뷰에 레이아웃매니저를 달아준다

            adapter = new SearchAssetListAdapter(getApplicationContext(), assetList);
            reView.setAdapter(adapter);
        }else{
            finish();
        }
    }

    public void callAllCheckedList(){
        NetworkService service = ApplicationController.getInstance().getNetworkService();
        SharedPreferences sf = getSharedPreferences("asset",0);
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("Authorization","Bearer " + sf.getString("id_token","1"));

        GetReportInfo getReportInfo = new GetReportInfo();
        getReportInfo.id = Integer.parseInt(sf.getString("id","1"));
        getReportInfo.user_name = sf.getString("user_name","1");
        getReportInfo.check_court = Integer.parseInt(sf.getString("check_court","1"));
        getReportInfo.group_id = Integer.parseInt(sf.getString("group_id","1"));

        Call<SearchAssetResult> getSearchAssetResultCall = service.getSearchAssetResult2(map,getReportInfo);
        getSearchAssetResultCall.enqueue(new Callback<SearchAssetResult>() {
            @Override
            public void onResponse(Call<SearchAssetResult> call, Response<SearchAssetResult> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        assetList = response.body().result;
                        progressOFF();
                        adapterConnect();
                    }
                }else{
                    Toast.makeText(SearchAssetListActivity.this, "Failed to receive Asset data", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SearchAssetResult> call, Throwable t) {
                Toast.makeText(SearchAssetListActivity.this, "Network communication failure", Toast.LENGTH_LONG).show();
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
}
