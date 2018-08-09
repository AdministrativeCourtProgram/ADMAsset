package com.court.admasset.admasset.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ScanInfoResult  {
    public String status;
    public ArrayList<ScanData> result;

    public static class ScanData implements Parcelable{
        public Integer row_number;
        public String asset_no;
        public String asset_name;
        public String organization;
        public Integer samecourt;

        protected ScanData(Parcel in) {
            if (in.readByte() == 0) {
                row_number = null;
            } else {
                row_number = in.readInt();
            }
            asset_no = in.readString();
            asset_name = in.readString();
            organization = in.readString();
            if (in.readByte() == 0) {
                samecourt = null;
            } else {
                samecourt = in.readInt();
            }
        }

        public static final Creator<ScanData> CREATOR = new Creator<ScanData>() {
            @Override
            public ScanData createFromParcel(Parcel in) {
                return new ScanData(in);
            }

            @Override
            public ScanData[] newArray(int size) {
                return new ScanData[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (row_number == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(row_number);
            }
            dest.writeString(asset_no);
            dest.writeString(asset_name);
            dest.writeString(organization);
            if (samecourt == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(samecourt);
            }
        }
    }
}