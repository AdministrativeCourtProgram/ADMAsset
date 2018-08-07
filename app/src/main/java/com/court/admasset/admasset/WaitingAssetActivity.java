package com.court.admasset.admasset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import com.court.admasset.admasset.Model.GetReportInfo;
import com.court.admasset.admasset.Model.SearchAssetResult;
import com.court.admasset.admasset.Network.NetworkService;
import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Model.MaindataFloorResult;
import com.court.admasset.admasset.Model.MaindataRoomResult;
import com.court.admasset.admasset.Model.MaindataWorkgroupResult;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingAssetActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;

    private Map<String, String> map;
    private String check_court;
    private NetworkService service;
    private SharedPreferences sf;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_asset_list);

        spinner1 = (Spinner) findViewById(R.id.workgroupList);
        spinner2 = (Spinner) findViewById(R.id.floorList);
        spinner3 = (Spinner) findViewById(R.id.roomList);

        sf = getSharedPreferences("asset",0);
        map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("Authorization","Bearer " + sf.getString("id_token","1"));
        check_court = sf.getString("check_court","1");

        service = ApplicationController.getInstance().getNetworkService();

        // 값 받아오기
        initMaindataFloor();
        initMaindataRoom();
        initMaindataWorkgroup();

        Button searchAssetBtn = (Button)findViewById(R.id.searchAssetBtn);
        searchAssetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spinner select
                // spinner1.getSelectedItem().toString(), spinner2.getSelectedItem().toString(), spinner3.getSelectedItem().toString()

                callSearchList();
            }
        });
    }

    public void  callSearchList(){
        GetReportInfo getReportInfo = new GetReportInfo();
        getReportInfo.id = Integer.parseInt(sf.getString("id","1"));
        getReportInfo.user_name = sf.getString("user_name","1");
        getReportInfo.check_court = Integer.parseInt(sf.getString("check_court","1"));
        getReportInfo.group_id = Integer.parseInt(sf.getString("group_id","1"));

        Call<SearchAssetResult> getSearchAssetResultCall = service.getSearchAssetResult(map,getReportInfo);
        getSearchAssetResultCall.enqueue(new Callback<SearchAssetResult>() {
            @Override
            public void onResponse(Call<SearchAssetResult> call, Response<SearchAssetResult> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        Intent intent = new Intent(WaitingAssetActivity.this, SearchAssetListActivity.class);
                        intent.putExtra("searchAssetList", response.body().result);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(WaitingAssetActivity.this, "실패", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchAssetResult> call, Throwable t) {
                Toast.makeText(WaitingAssetActivity.this, "완전실패", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initMaindataFloor(){
        Call<MaindataFloorResult> maindataFloorResultCall = service.getMaindataFloorResult(map);
        maindataFloorResultCall.enqueue(new Callback<MaindataFloorResult>() {
            @Override
            public void onResponse(Call<MaindataFloorResult> call, Response<MaindataFloorResult> response) {
                if(response.isSuccessful()){
                    if(response.body().result!=null){
                        ArrayList<String> aa = new ArrayList<String>();
                        for(int i = 0; i<response.body().result.size(); i++){
                            aa.add(response.body().result.get(i).floor_name);
                        }
                        spinner1.setAdapter(new ArrayAdapter(WaitingAssetActivity.this, R.layout.support_simple_spinner_dropdown_item, aa));
                    }
                }else{
                    Toast.makeText(WaitingAssetActivity.this,"실패",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<MaindataFloorResult> call, Throwable t) {
                Toast.makeText(WaitingAssetActivity.this,"실패2",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initMaindataRoom(){
        Call<MaindataRoomResult> maindataRoomResultCall = service.getMaindataRoomResult(map);
        maindataRoomResultCall.enqueue(new Callback<MaindataRoomResult>() {
            @Override
            public void onResponse(Call<MaindataRoomResult> call, Response<MaindataRoomResult> response) {
                if(response.isSuccessful()){
                    if(response.body().result!=null){
                        ArrayList<String> aa = new ArrayList<String>();
                        for(int i = 0; i<response.body().result.size(); i++){
                            aa.add(response.body().result.get(i).room_name);
                        }
                        spinner2.setAdapter(new ArrayAdapter(WaitingAssetActivity.this, R.layout.support_simple_spinner_dropdown_item, aa));
                    }
                }else{
                    Toast.makeText(WaitingAssetActivity.this,"실패",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<MaindataRoomResult> call, Throwable t) {
                Toast.makeText(WaitingAssetActivity.this,"실패2",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initMaindataWorkgroup(){
        Call<MaindataWorkgroupResult> maindataWorkgroupResultCall = service.getMaindataWorkgroupResult("api/maindata/workgroup/"+check_court,map);
        maindataWorkgroupResultCall.enqueue(new Callback<MaindataWorkgroupResult>() {
            @Override
            public void onResponse(Call<MaindataWorkgroupResult> call, Response<MaindataWorkgroupResult> response) {
                if(response.isSuccessful()){
                    if(response.body().result!=null){
                        ArrayList<String> aa = new ArrayList<String>();
                        for(int i = 0; i<response.body().result.size(); i++){
                            aa.add(response.body().result.get(i).c_name);
                        }
                        spinner3.setAdapter(new ArrayAdapter(WaitingAssetActivity.this, R.layout.support_simple_spinner_dropdown_item, aa));
                    }
                }else{
                    Toast.makeText(WaitingAssetActivity.this,"실패",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<MaindataWorkgroupResult> call, Throwable t) {
                Toast.makeText(WaitingAssetActivity.this,"실패2",Toast.LENGTH_LONG).show();
            }
        });
    }
}
