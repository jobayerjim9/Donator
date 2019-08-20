package com.teamjhj.donator_247blood.RestApi;

import com.teamjhj.donator_247blood.DataModel.NotificationSender;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAANe0y-bI:APA91bF_6WqRI3l9dL1ezeGwlOBdw7DbzJGtxfsIuTfHG4PUkffnCL95mJ1fKjX4QiIYP0e_1Khl6OdmHviHeUOm6b2QSth7NA_wMAS5_QN5nIZz9DB2bEu7QYY_1-V2VJ6OxBq0C_4S"
    })


    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationSender notificationSender);

}
