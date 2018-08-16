package com.court.admasset.admasset;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.court.admasset.admasset.Model.CheckedListResult;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<CheckedListResult.CheckedData> itemdata;

    public ListViewAdapter(ArrayList<CheckedListResult.CheckedData> itemdata) {
        this.itemdata = itemdata;
    }

    @Override
    public int getCount() {
        return itemdata.size();
    }

    @Override
    public Object getItem(int position) {
        return itemdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_recyclerview_item, parent, false);
        }

//        TextView re_index = (TextView) convertView.findViewById(R.id.IndexTextView);
//        TextView re_no = (TextView) convertView.findViewById(R.id.asset_noTextView);
//        TextView re_name = (TextView) convertView.findViewById(R.id.asset_nameTextView);
//        TextView re_organization = (TextView) convertView.findViewById(R.id.organizationTextView);

        TextView re_index = (TextView) convertView.findViewById(R.id.IndexTextView);
        TextView re_no = (TextView) convertView.findViewById(R.id.asset_noTextView);
        TextView re_name = (TextView) convertView.findViewById(R.id.asset_nameTextView);
        TextView re_organization = (TextView) convertView.findViewById(R.id.organizationTextView);
        re_index.setText(itemdata.get(position).row_number);
        re_no.setText(itemdata.get(position).asset_no);
        re_name.setText(itemdata.get(position).asset_name);
        re_organization.setText(itemdata.get(position).organization);


        if(itemdata.get(position).samecourt ==0){
            convertView.setBackgroundColor(Color.rgb(216,43,43));
            Log.v("TAG","REDDDDDDDD");
        }else
        {
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;

    }
}
