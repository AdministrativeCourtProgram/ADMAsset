package com.court.admasset.admasset.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class GetReportInfoResult {

    public String status;
    public ArrayList<reportInfoResult> result;

    public class reportInfoResult{
        public String check_flag;
        public Number flag_count;
    }
}