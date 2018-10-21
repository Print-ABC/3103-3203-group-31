package services;

import models.Result;
import models.User;
import models.CardList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST("users/cards")
    Call<CardList> getCards(@Header("Authorization") String token, @Body CardList cards);

    @POST("users/register")
    Call<Result> addUser(@Body User body);

    @POST("users/login")
    Call<User> login(@Body User body);

    @POST("users/user")
    Call<User> retrieve(@Query("id") String user_id);

    @POST("users/retrieveFUID")
    Call<User> retrieveFUID(@Query("id") String user_friend_id);

    @POST("users/retrieveByUsername")
    Call<User> retrieveByUsername(@Query("id") String user_username);
}
