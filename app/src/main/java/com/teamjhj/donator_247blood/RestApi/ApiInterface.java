package com.teamjhj.donator_247blood.RestApi;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
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
            "Authorization:key=AAAANe0y-bI:APA91bF_6WqRI3l9dL1ezeGwlOBdw7DbzJGtxfsIuTfHG4PUkffnCL95mJ1fKjX4QiIYP0e_1Khl6OdmHviHeUOm6b2QSth7NA_wMAS5_QN5nIZz9DB2bEu7QYY_1-V2VJ6OxBq0C_4S"
    })


    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationSender notificationSender);

    @GET("json?key=AIzaSyBvQWybGviEzL9xutunUkDEf9RHVwcuN6U&rankby=distance")
    Call<NearbyRawData> getNearbyPlace(@Query("location") String location, @Query("keyword") String keyword);

    //json?units=imperial&key=AIzaSyBvQWybGviEzL9xutunUkDEf9RHVwcuN6U
    @GET("json?units=imperial&key=AIzaSyBvQWybGviEzL9xutunUkDEf9RHVwcuN6U")
    Call<DistanceApiData> getDistance(@Query("origins") String source, @Query("destinations") String destination);
}
