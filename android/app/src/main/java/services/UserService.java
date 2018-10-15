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
    @GET("user/.")
    Call<UserList> getUserList();

    @GET
    Call<User> getUser(@Url String url);

    @POST("user/post")
    Call<Result> addUser(@Body User body);

    @POST("user/login")
    Call<User> login(@Body User body);

    @POST("user/retrieveFUID")
    Call<User> retrieveFUID(@Body User body);
}
