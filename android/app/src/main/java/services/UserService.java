package services;

import models.Result;
import models.User;
import models.UserList;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface UserService {
    @GET("users/.")
    Call<UserList> getUserList();

    @GET
    Call<User> getUser(@Url String url);

    @POST("users/register")
    Call<Result> addUser(@Body User body);

    @POST("users/login")
    Call<User> login(@Body User body);

    @POST("users/user")
    Call<User> retrieve(@Query("id") String user_id);

    @POST("users/retrieveFUID")
    Call<User> retrieveFUID(@Query("id") String user_friend_id);

    @POST("users/retrieveByUsername")
    Call<User> retrieveUsername(@Query("id") String user_username);
}
