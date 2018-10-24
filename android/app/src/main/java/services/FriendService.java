package services;

import java.util.List;
import models.FriendRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FriendService {
    @GET("friends/")
    Call<List<FriendRequest>> getAllRequests();

    @GET("friends/req/{uid}")
    Call<List<FriendRequest>> getByRequesterID(@Path("uid") String uid);

    @GET("friends/recp/{uid}")
    Call<List<FriendRequest>> getByRecipientID(@Path("uid") String uid);

    @POST("friends/")
    Call<FriendRequest> createRequest(@Body FriendRequest body);

    @PATCH("friends/{uid}/add/{friend}")
    Call<FriendRequest> addFriend(@Path("uid") String uid, @Path("friend") String friend);

    @PATCH("friends/{uid}/del/{friend}")
    Call<FriendRequest> deleteFriend(@Path("uid") String uid, @Path("friend") String friend);

    @DELETE("friends/{uid}")
    Call<FriendRequest> deleteRequest(@Path("uid") String uid);

//    @POST("relationship/relationship")
//    Call<FriendRequest> retrieveFriends(@Query("id") String friend_one_id);
}
