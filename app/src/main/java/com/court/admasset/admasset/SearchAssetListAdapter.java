package com.court.admasset.admasset;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.court.admasset.admasset.Model.CheckedListResult;
import com.court.admasset.admasset.Model.SearchAssetResult;

import java.util.ArrayList;


public class SearchAssetListAdapter extends RecyclerView.Adapter<MyViewHolders> {
    private ArrayList<SearchAssetResult.CheckedData> itemdatas;
    private Context context;
    private View view;

    //    , View.OnClickListener clickListener
    public SearchAssetListAdapter(Context context, ArrayList<SearchAssetResult.CheckedData> itemdatas) {
        this.context = context;
        this.itemdatas = itemdatas;
    }

    @Override
    public MyViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_item, parent, false);
        MyViewHolders viewHolder = new MyViewHolders(view);             //커스텀 뷰홀더 객체 생성
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolders holder, final int position) {
        // 뷰홀더에 메모리상에 보유할 항목을 뷰홀더에 바인딩 해주는 함수!!
        // 레이아웃과 아이템 연결
        //holder.re_index.setText(itemdatas.get(position).row_number);
        holder.re_no.setText(itemdatas.get(position).asset_no);
        holder.re_name.setText(itemdatas.get(position).asset_name);
        holder.re_organization.setText(itemdatas.get(position).organization);
    }

    @Override
    public int getItemCount(){
            if(itemdatas==null)
            Log.v("TAG","null list");
            else
            Log.d("TAG","getItemCount: list DATA-----"+itemdatas.size());

            //**Here I'm getting Null Pointer exception**
            return itemdatas==null?0:itemdatas.size();

            }
    public Object getItem(int position){
            return itemdatas.get(position);
            }

    @Override
    public long getItemId(int position){
            return position;
            }

}