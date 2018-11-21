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
    Call<List<FriendRequest>> getByRecipientID(@Header("Authorization") String token, @Path("uid") String uid);

    @POST("friends/{uid}")
    Call<FriendRequest> createRequest(@Header("Authorization") String token, @Path("uid") String uid, @Body FriendRequest body);

    @POST("friends/update/add")
    Call<FriendRequest> addFriend(@Header("Authorization") String token, @Body FriendRequest body);

    @POST("friends/update/remove")
    Call<FriendRequest> deleteFriend(@Header("Authorization") String token, @Body FriendRequest body);

    @DELETE("friends/{uid}/{id}")
    Call<FriendRequest> deleteRequest(@Header("Authorization") String token, @Path("uid") String uid, @Path("id") String id);
}
