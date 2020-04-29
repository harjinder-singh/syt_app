package com.example.syt.api.service;

import com.example.syt.api.model.Login;
import com.example.syt.api.model.User;
import com.example.syt.fetch.SearchImageResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
    @POST("login")
    Call<User> login(@Body Login login);

    @GET("images")
    Call<SearchImageResponse[]> get_user_images(@Header("Authorization") String authToken);
}
