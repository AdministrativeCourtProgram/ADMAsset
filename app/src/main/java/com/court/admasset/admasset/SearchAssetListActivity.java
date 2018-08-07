package com.court.admasset.admasset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.court.admasset.admasset.Model.SearchAssetResult;
import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Network.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchAssetListActivity extends AppCompatActivity {

    private TextView txtListSize;
    private RecyclerView reView;

    private SearchAssetListAdapter adapter;
    private ArrayList<SearchAssetResult.CheckedData> assetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_asset_list);

        // ===== init ===== //
        txtListSize = (TextView)findViewById(R.id.txtListSize);
        Intent intent = getIntent();
        assetList = (ArrayList<SearchAssetResult.CheckedData>)intent.getSerializableExtra("searchAssetList");

       // check init
        Log.d("AAAAA", assetList.size()+", "+assetList.get(0).row_number);
        Toast.makeText(this, ""+assetList.get(0).toString(), Toast.LENGTH_SHORT).show();

        // ====== RecyclerView setting ======= //
        reView = (RecyclerView)findViewById(R.id.recyclerAssetList);
        reView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);             //리니어레이아웃의 형태이면 방향은 수직
        reView.setLayoutManager(layoutManager);//리사이클러뷰에 레이아웃매니저를 달아준다

        adapter = new SearchAssetListAdapter(getApplicationContext(), assetList);
        reView.setAdapter(adapter);
    }
}
