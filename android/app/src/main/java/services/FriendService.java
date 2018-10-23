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
    @GET(".")
    Call<List<FriendRequest>> getAllRequests();

    @GET("req/{uid}")
    Call<List<FriendRequest>> getByRequesterID(@Path("uid") String uid);

    @GET("recp/{uid}")
    Call<List<FriendRequest>> getByRecipientID(@Path("uid") String uid);

    @POST(".")
    Call<FriendRequest> createRequest(@Body FriendRequest body);

    @PATCH("{uid}/add/{friend}")
    Call<FriendRequest> addFriend(@Path("uid") String uid, @Path("friend") String friend);

    @PATCH("{uid}/del/{friend}")
    Call<FriendRequest> deleteFriend(@Path("uid") String uid, @Path("friend") String friend);

    @DELETE("{uid}")
    Call<FriendRequest> deleteRequest(@Path("uid") String uid);

//    @POST("relationship/relationship")
//    Call<FriendRequest> retrieveFriends(@Query("id") String friend_one_id);
}
