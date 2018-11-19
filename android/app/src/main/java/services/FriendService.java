package services;

import java.util.List;
import models.FriendRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FriendService {

    @GET("friends/recp/{uid}")
    Call<List<FriendRequest>> getByRecipientID(@Path("uid") String uid);

    @POST("friends/")
    Call<FriendRequest> createRequest(@Body FriendRequest body);

    @POST("friends/add")
    Call<FriendRequest> addFriend(@Body FriendRequest body);

    @POST("friends/remove")
    Call<FriendRequest> deleteFriend(@Body FriendRequest body);

    @DELETE("friends/{id}")
    Call<FriendRequest> deleteRequest(@Path("id") String uid);
}
