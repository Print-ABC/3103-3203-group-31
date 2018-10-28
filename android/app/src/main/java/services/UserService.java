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

    @GET("users/username/{username}")
    Call<User> searchByUsername(@Path("username") String user_username);

    @GET("users/findcards/{uid}/{card}")
    Call<User> checkForCard(@Header("Authorization") String token, @Path("uid") String user_id, @Path("card") String card_id);

    @POST("users/cards")
    Call<CardList> getCards(@Header("Authorization") String token, @Body CardList cards);

    @POST("users/register")
    Call<Result> addUser(@Body User body);

    @POST("users/login")
    Call<DummyResponse> login(@Body User body);

    @GET("users/check2fa/{fatoken}")
    Call<DummyResponse> check2fa(@Path("fatoken") String fatokenInput);
}
