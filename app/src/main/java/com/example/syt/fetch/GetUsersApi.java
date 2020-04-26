package com.example.syt.fetch;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hassanabid on 2/27/16.
 */
public interface GetUsersApi {

    @GET("api/v1/users")
    Call<SearchUsersResponse[]> getUsersList(@Query("format") String format,
                                                        @Query("rtype") String rtype);


}