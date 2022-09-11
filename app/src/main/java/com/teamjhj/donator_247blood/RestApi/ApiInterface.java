package com.teamjhj.donator_247blood.RestApi;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.teamjhj.donator_247blood.DataModel.Constants;
import com.teamjhj.donator_247blood.DataModel.DistanceApiData;
import com.teamjhj.donator_247blood.DataModel.LocationData;
import com.teamjhj.donator_247blood.DataModel.NearbyPlaceData;
import com.teamjhj.donator_247blood.DataModel.NearbyRawData;
import com.teamjhj.donator_247blood.DataModel.NotificationSender;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers({
            "Content-Type:application/json",
            Constants.NOTIFICATION_KEY
    })


    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationSender notificationSender);

    @GET("json?key="+Constants.GOOGLE_API_KEY+"&rankby=distance")
    Call<NearbyRawData> getNearbyPlace(@Query("location") String location, @Query("keyword") String keyword);

    @GET("json?units=imperial&key="+Constants.GOOGLE_API_KEY)
    Call<DistanceApiData> getDistance(@Query("origins") String source, @Query("destinations") String destination);
}
