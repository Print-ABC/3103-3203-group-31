package services;

import java.util.List;
import models.FriendRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface FriendService {
    @GET(".")
    Call<List<FriendRequest>> getAllRequests();

    @GET("req/{uid}")
    Call<List<FriendRequest>> getByRequesterID();

    @GET("recp/{uid}")
    Call<List<FriendRequest>> getByRecipientID();

    @POST(".")
    Call<FriendRequest> createRequest(@Body FriendRequest body);

    @PATCH("{uid}/add/{fuid}")
    Call<FriendRequest> addFriend();

    @PATCH("{uid}/del/{fuid}")
    Call<FriendRequest> deleteFriend();

    @DELETE("{uid}")
    Call<FriendRequest> deleteRequest();

//    @POST("relationship/relationship")
//    Call<FriendRequest> retrieveFriends(@Query("id") String friend_one_id);
}
