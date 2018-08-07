package com.court.admasset.admasset.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SearchAssetResult {
    public String status;
    public ArrayList<CheckedData> result;

    public static class CheckedData implements Parcelable {
        public String row_number;
        public String asset_no;
        public String asset_name;
        public String organization;

        protected CheckedData(Parcel in) {
            row_number = in.readString();
            asset_no = in.readString();
            asset_name = in.readString();
            organization = in.readString();
        }

        public static final Creator<CheckedData> CREATOR = new Creator<CheckedData>() {
            @Override
            public CheckedData createFromParcel(Parcel in) {
                return new CheckedData(in);
            }

            @Override
            public CheckedData[] newArray(int size) {
                return new CheckedData[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(row_number);
            dest.writeString(asset_no);
            dest.writeString(asset_name);
            dest.writeString(organization);
        }
    }
}