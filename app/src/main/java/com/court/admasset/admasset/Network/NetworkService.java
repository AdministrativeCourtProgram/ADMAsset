package com.court.admasset.admasset.Network;

import com.court.admasset.admasset.Model.CheckedListInfo;
import com.court.admasset.admasset.Model.CheckedListResult;
import com.court.admasset.admasset.Model.LoginInfo;
import com.court.admasset.admasset.Model.LoginInfoResult;
import com.court.admasset.admasset.Model.MaindataFloorResult;
import com.court.admasset.admasset.Model.MaindataRoomResult;
import com.court.admasset.admasset.Model.MaindataWorkgroupResult;
import com.court.admasset.admasset.Model.ScanInfo;
import com.court.admasset.admasset.Model.ScanInfoResult;
import com.court.admasset.admasset.Model.GetReportInfo;
import com.court.admasset.admasset.Model.GetReportInfoResult;
import com.court.admasset.admasset.Model.SearchAssetResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Url;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface NetworkService {
    @POST("api/user/login")
    Call<LoginInfoResult> getLoginInfoResult(@Body LoginInfo loginInfo);

    @POST("api/asset/scan")
    Call<ScanInfoResult> getScanInfoResult(
            @HeaderMap Map<String, String> headers,
            @Body ScanInfo scanInfo);

    @POST("api/asset/getReport")
    Call<GetReportInfoResult> getGetReportInfoResult(
            @HeaderMap Map<String, String> headers,
            @Body GetReportInfo getReportInfo);

    @POST("api/asset/getCheckedSessionList")
    Call<CheckedListResult> getCheckedListResult(
            @HeaderMap Map<String, String> headers,
            @Body CheckedListInfo checkedListInfo);

    @GET("api/maindata/floor")
    Call<MaindataFloorResult> getMaindataFloorResult(@HeaderMap Map<String, String> headers);

    @GET("api/maindata/room")
    Call<MaindataRoomResult> getMaindataRoomResult(@HeaderMap Map<String, String> headers);

    @GET
    Call<MaindataWorkgroupResult> getMaindataWorkgroupResult(@Url String check_court,@HeaderMap Map<String, String> headers);

    @POST("api/asset/searchAsset")
    Call<SearchAssetResult> getSearchAssetResult(
            @HeaderMap Map<String, String> headers,
            @Body GetReportInfo getReportInfo);
}