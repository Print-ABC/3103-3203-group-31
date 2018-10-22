package services;

import java.util.List;
import models.FriendRequest;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FriendService {
    @GET(".")
    Call<List<FriendRequest>> getAllRelations();

    @POST("relationship/relationship")
    Call<FriendRequest> retrieveFriends(@Query("id") String friend_one_id);
}
