package com.example.ru_foody.SendNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAkyi_kJ8:APA91bGqCVF3SutqpISMXQHcHF2m2Z_3auBvwYXgLy_kN0Rw9tBRSWfa8u9QK9amM8r7uM80UlB7PyX3o4PVAx7FrH1AadiDyJ2H6EZiCvC0L6AOrOCDPrGMvSfeyPrLklbZAZKN9a2S"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
