package com.court.admasset.admasset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.court.admasset.admasset.Model.GetReportInfo;
import com.court.admasset.admasset.Model.MaindataRoomInfo;
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

    private Spinner workSpinner;
    private Spinner floorSpinner;
    private Spinner roomSpinner;

    private Map<String, String> map, roomMap, floorMap, workMap;
    private String check_court;
    private NetworkService service;
    private SharedPreferences sf;
    private AppCompatDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_asset_list);

        workSpinner = (Spinner) findViewById(R.id.workgroupList);
        floorSpinner = (Spinner) findViewById(R.id.floorList);
        roomSpinner = (Spinner) findViewById(R.id.roomList);

        sf = getSharedPreferences("asset",0);
        map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("Authorization","Bearer " + sf.getString("id_token","1"));
        check_court = sf.getString("check_court","1");

        service = ApplicationController.getInstance().getNetworkService();

        // Get data(Floor, Room, Workgroup)
        initMaindataFloor();
        initMaindataRoom();
        initMaindataWorkgroup();

        // ====== home Button ====== //
        ImageButton homeBtn = (ImageButton)findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(RecyclerViewActivity.this, MenuActivity.class);
                Intent intent = new Intent(WaitingAssetActivity.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        Button searchAssetBtn = (Button)findViewById(R.id.searchAssetBtn);
        searchAssetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spinner select
                // spinner1.getSelectedItem().toString(), spinner2.getSelectedItem().toString(), spinner3.getSelectedItem().toString()

                // call loading dialog
                progressON(WaitingAssetActivity.this, null);

                callSearchList();
            }
        });
    }

    public void  callSearchList(){
        Map<String, String> mapBody = new HashMap<>();
        mapBody.put("id",sf.getString("id","1"));
        mapBody.put("user_name",sf.getString("user_name","1"));
        mapBody.put("check_court",sf.getString("check_court","1"));
        mapBody.put("group_id",sf.getString("group_id","1"));

        if(!workSpinner.getSelectedItem().toString().equals("-")){
            mapBody.put("workgroup_id",workMap.get(workSpinner.getSelectedItem().toString()));
        }
        if(!floorSpinner.getSelectedItem().toString().equals("-")){
            mapBody.put("floor_id",floorMap.get(floorSpinner.getSelectedItem().toString()));
        }
        if(!roomSpinner.getSelectedItem().toString().equals("-")){
            mapBody.put("room_id",roomMap.get(roomSpinner.getSelectedItem().toString()));
        }

        Call<SearchAssetResult> getSearchAssetResultCall = service.getSearchAssetResult(map, mapBody);
        getSearchAssetResultCall.enqueue(new Callback<SearchAssetResult>() {
            @Override
            public void onResponse(Call<SearchAssetResult> call, Response<SearchAssetResult> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().result.size()==0){
                            Toast.makeText(WaitingAssetActivity.this, "No data available", Toast.LENGTH_LONG).show();
                            progressOFF();
                        }else{
                            Intent intent = new Intent(WaitingAssetActivity.this, SearchAssetListActivity.class);
                            intent.putExtra("flag","waiting");
                            intent.putExtra("searchAssetList", response.body().result);
                            startActivity(intent);
                            progressOFF();
                        }
                    }
                }else{
                    Toast.makeText(WaitingAssetActivity.this, "Failed to receive searchAsset data", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SearchAssetResult> call, Throwable t) {
                Toast.makeText(WaitingAssetActivity.this, "Network communication failure", Toast.LENGTH_LONG).show();
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
                        floorMap = new HashMap<>();
                        ArrayList<String> aa = new ArrayList<String>();
                        for(int i = 0; i<response.body().result.size(); i++){
                            floorMap.put(response.body().result.get(i).floor_name,response.body().result.get(i).floor_id+"");
                            aa.add(response.body().result.get(i).floor_name);
                        }
                        floorSpinner.setAdapter(new ArrayAdapter(WaitingAssetActivity.this, R.layout.support_simple_spinner_dropdown_item, aa));
                    }
                }else{
                    Toast.makeText(WaitingAssetActivity.this,"Failed to receive floor data",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<MaindataFloorResult> call, Throwable t) {
                Toast.makeText(WaitingAssetActivity.this,"Network communication failure",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initMaindataRoom(){
        MaindataRoomInfo maindataRoomInfo = new MaindataRoomInfo();
        maindataRoomInfo.check_court = Integer.parseInt(sf.getString("check_court","1"));

        Call<MaindataRoomResult> getMaindataRoomResultCall = service.getMaindataRoomResult(map,maindataRoomInfo);
        getMaindataRoomResultCall.enqueue(new Callback<MaindataRoomResult>() {
            @Override
            public void onResponse(Call<MaindataRoomResult> call, Response<MaindataRoomResult> response) {
                if(response.isSuccessful()){
                    if(response.body().result!=null){
                        roomMap = new HashMap<>();
                        ArrayList<String> aa = new ArrayList<String>();
                        aa.add("-");
                        for(int i = 0; i<response.body().result.size(); i++){
                            roomMap.put(response.body().result.get(i).room_name,response.body().result.get(i).room_id+"");
                            aa.add(response.body().result.get(i).room_name);
                        }
                        roomSpinner.setAdapter(new ArrayAdapter(
                                WaitingAssetActivity.this,
                                R.layout.support_simple_spinner_dropdown_item,
                                aa));
                    }
                }else{
                    Toast.makeText(WaitingAssetActivity.this,"Failed to receive room data",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<MaindataRoomResult> call, Throwable t) {
                Toast.makeText(WaitingAssetActivity.this,"Network communication failure",Toast.LENGTH_LONG).show();
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
                        workMap = new HashMap<>();
                        ArrayList<String> aa = new ArrayList<String>();
                        aa.add("-");
                        for(int i = 0; i<response.body().result.size(); i++){
                            workMap.put(response.body().result.get(i).c_name,response.body().result.get(i).c_code+"");
                            aa.add(response.body().result.get(i).c_name);
                        }
                        workSpinner.setAdapter(new ArrayAdapter(WaitingAssetActivity.this, R.layout.support_simple_spinner_dropdown_item, aa));
                    }
                }else{
                    Toast.makeText(WaitingAssetActivity.this,"Failed to receive Workgroup data",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<MaindataWorkgroupResult> call, Throwable t) {
                Toast.makeText(WaitingAssetActivity.this,"Network communication failure",Toast.LENGTH_LONG).show();
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
