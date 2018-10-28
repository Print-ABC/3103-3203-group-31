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

    @POST("friends/")
    Call<FriendRequest> createRequest(@Header("Authorization") String token, @Body FriendRequest body);

    @PATCH("friends/{uid}/add/{friend}")
    Call<FriendRequest> addFriend(@Header("Authorization") String token, @Path("uid") String uid, @Path("friend") String friend);

    @PATCH("friends/{uid}/del/{friend}")
    Call<FriendRequest> deleteFriend(@Header("Authorization") String token, @Path("uid") String uid, @Path("friend") String friend);

    @DELETE("friends/{id}")
    Call<FriendRequest> deleteRequest(@Header("Authorization") String token, @Path("id") String uid);
}
