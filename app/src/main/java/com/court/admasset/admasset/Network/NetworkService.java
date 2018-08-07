package com.court.admasset.admasset.Network;

import android.support.annotation.Nullable;

import com.court.admasset.admasset.Model.CheckedListInfo;
import com.court.admasset.admasset.Model.CheckedListResult;
import com.court.admasset.admasset.Model.LoginInfo;
import com.court.admasset.admasset.Model.LoginInfoResult;
import com.court.admasset.admasset.Model.MaindataFloorResult;
import com.court.admasset.admasset.Model.ScanInfo;
import com.court.admasset.admasset.Model.ScanInfoResult;
import com.court.admasset.admasset.Model.GetReportInfo;
import com.court.admasset.admasset.Model.GetReportInfoResult;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
}