package com.court.admasset.admasset.Model;

import java.util.ArrayList;

public class CheckedListResult {
    public String status;
    public ArrayList<CheckedData> result;

    public class CheckedData {
        public String row_number;
        public String asset_no;
        public String asset_name;
        public String organization;
        public Integer samecourt;

    }
}