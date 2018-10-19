package services;

import models.Result;
import models.User;
import models.ViewCard;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface UserService {
    @GET("users/username/{uid}")
    Call<ViewCard> getUsername(@Header("Authorization") String token,  @Path("uid") String uid);

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
