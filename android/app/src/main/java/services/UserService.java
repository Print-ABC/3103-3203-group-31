package services;

import models.DummyResponse;
import models.Result;
import models.User;
import models.CardList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("name/{uid}")
    Call<User> retrieveName(@Path("uid") String user_id);

    @GET("users/username/{username}")
    Call<User> searchByUsername(@Path("username") String user_username);

    @GET("users/findcards/{uid}/{card}")
        //Call<User> checkForCard(@Query("uid") String user_id, @Query("card") String card_id);
    Call<User> checkForCard(@Path("uid") String user_id, @Path("card") String card_id);

    @POST("users/cards")
    Call<CardList> getCards(@Header("Authorization") String token, @Body CardList cards);

    @POST("users/register")
    Call<Result> addUser(@Body User body);

    @POST("users/login")
    Call<DummyResponse> login(@Body User body);

    @POST("users/user")
    Call<User> retrieve(@Query("id") String user_id);

    @POST("users/retrieveFUID")
    Call<User> retrieveFUID(@Query("id") String user_friend_id);

    @GET("users/check2fa/{fatoken}")
        //Call<User> checkForCard(@Query("uid") String user_id, @Query("card") String card_id);
    Call<User> check2fa(@Path("fatoken") String fatokenInput);
}
