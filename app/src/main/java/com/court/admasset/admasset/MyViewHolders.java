package com.court.admasset.admasset;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyViewHolders extends RecyclerView.ViewHolder {
    //recycler_viewHolder

    public TextView re_index, re_no, re_name, re_organization;
//    public View re_samecourt;
    public LinearLayout re_parentLayout;

    public MyViewHolders(View itemView) {
        super(itemView);
        re_index = (TextView) itemView.findViewById(R.id.IndexTextView);
        re_no = (TextView) itemView.findViewById(R.id.asset_noTextView);
        re_name = (TextView) itemView.findViewById(R.id.asset_nameTextView);
        re_organization = (TextView) itemView.findViewById(R.id.organizationTextView);
        re_parentLayout = (LinearLayout) itemView.findViewById(R.id.parent_layout);
//        re_samecourt =(View) itemView.findViewById(R.id.samecourtView);
    }
}