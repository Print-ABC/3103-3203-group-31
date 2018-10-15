package services;

import java.util.List;
import models.Relationship;
import models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FriendService {
    @GET(".")
    Call<List<Relationship>> getAllRelations();

    @POST("relationship/relationship")
    Call<Relationship> retrieveFriends(@Query("id") String friend_one_id);
}
