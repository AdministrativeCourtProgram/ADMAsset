package com.court.admasset.admasset.Model;

import java.util.ArrayList;

public class MaindataRoomResult {
    public String status;
    public ArrayList<MaindataRoom> result;

    public class MaindataRoom{
        public Number room_id;
        public String room_name;
    }
}
