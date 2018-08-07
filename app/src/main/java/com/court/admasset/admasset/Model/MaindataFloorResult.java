package com.court.admasset.admasset.Model;

import java.util.ArrayList;

public class MaindataFloorResult {
    public String status;
    public ArrayList<MaindataFloor> result;

    public class MaindataFloor{
        public Number floor_id;
        public String floor_name;
    }
}
