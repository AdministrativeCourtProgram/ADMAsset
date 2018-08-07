package com.court.admasset.admasset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.court.admasset.admasset.Network.NetworkService;
import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Model.MaindataFloorResult;

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

    NetworkService service;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_asset_list);

        Spinner spinner1 = (Spinner) findViewById(R.id.workgroupList);
        Spinner spinner2 = (Spinner) findViewById(R.id.floorList);
        Spinner spinner3 = (Spinner) findViewById(R.id.roomList);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.workgroup_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.floor_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.room_array, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDY2LCJ1c2VyX25hbWUiOiJraXYiLCJ1X25hbWUiOiJLSVYxIiwiY291cnRfdHlwZSI6IjE0MiIsImdyb3VwX2lkIjoxLCJpYXQiOjE1MzM1MjIwMjAsImV4cCI6MTUzMzU0MDAyMH0.Nu4hRThz5-txwcIpdwYdEDCEqd8XjJcrkDx4CTjbiPs");

        service = ApplicationController.getInstance().getNetworkService();

        // 값 받아오기
        initMaindataFloor();

//        spinner1.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
//                parent.getSelectedItem().toString();
//            }
//
//        });

    }

    private void initMaindataFloor(){
        Call<MaindataFloorResult> maindataFloorResultCall = service.getMaindataFloorResult(map);
        maindataFloorResultCall.enqueue(new Callback<MaindataFloorResult>() {
            @Override
            public void onResponse(Call<MaindataFloorResult> call, Response<MaindataFloorResult> response) {
                if(response.isSuccessful()){
                    Toast.makeText(WaitingAssetActivity.this,"성공",Toast.LENGTH_LONG).show();
                    Log.d("AAAAAA","성공"+response.body());
                    if(response.body().result!=null){
                        Toast.makeText(WaitingAssetActivity.this,"성공2",Toast.LENGTH_LONG).show();
                        Log.d("AAAAAA","성공"+response.body().status+", "+response.body().result.get(0).toString());
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
}
