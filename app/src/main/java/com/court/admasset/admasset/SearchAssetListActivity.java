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


        // ===== init ===== //
        txtListSize = (TextView)findViewById(R.id.txtListSize);
        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");

        if(flag.equals("waiting")){
            assetList = (ArrayList<SearchAssetResult.CheckedData>)intent.getSerializableExtra("searchAssetList");
            adapterConnect();
        }else if(flag.equals("summary")){
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
                        adapterConnect();
                    }
                }else{
                    Toast.makeText(SearchAssetListActivity.this, "실패", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchAssetResult> call, Throwable t) {
                Toast.makeText(SearchAssetListActivity.this, "완전실패", Toast.LENGTH_LONG).show();
            }
        });
    }



}
