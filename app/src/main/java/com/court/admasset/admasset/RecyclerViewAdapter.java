package com.court.admasset.admasset;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.court.admasset.admasset.Model.CheckedListResult;
import com.court.admasset.admasset.MyViewHolders;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolders> {
    private ArrayList<CheckedListResult.CheckedData> itemdatas;
    private View.OnClickListener clickListener;
    private Context context;
    private View view;
    int i = 0;
    int prepositon;

    //    , View.OnClickListener clickListener
    public RecyclerViewAdapter(Context context, ArrayList<CheckedListResult.CheckedData> itemdatas, View.OnClickListener clickListener) {
        this.context = context;
        this.itemdatas = itemdatas;
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        if (itemdatas.get(i).samecourt == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_item_red, parent, false);
        }
        ++i;
        MyViewHolders viewHolder = new MyViewHolders(view);             //커스텀 뷰홀더 객체 생성
        view.setOnClickListener(clickListener);// 정의된 클릭이벤트를 달아준다
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolders holder, final int position) {
        // 뷰홀더에 메모리상에 보유할 항목을 뷰홀더에 바인딩 해주는 함수!!
        // 레이아웃과 아이템 연결
        holder.re_index.setText(itemdatas.get(position).row_number);
        holder.re_no.setText(itemdatas.get(position).asset_no);
        holder.re_name.setText(itemdatas.get(position).asset_name);
        holder.re_organization.setText(itemdatas.get(position).organization);

//        Log.v("TAG","preposition"+prepositon+"11111111"+position);
//        if(prepositon == position) {
//            holder.re_parentLayout.setBackgroundColor(Color.rgb(178, 204, 255));
//        }
//        prepositon +=1;
//        holder.re_parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("TAG","onClick: clicked on"+itemdatas.get(position));
//                Toast.makeText(context, (CharSequence) itemdatas.get(position),Toast.LENGTH_SHORT).show();
//            }
    }


    @Override
    public int getItemCount() {
        if (itemdatas == null)
            Log.v("TAG", "null list");
        else
            Log.d("TAG", "getItemCount: list DATA-----" + itemdatas.size());

        //**Here I'm getting Null Pointer exception**
        return itemdatas == null ? 0 : itemdatas.size();

    }

    public Object getItem(int position) {
        return itemdatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}