package services;

import java.util.List;
import models.Relationship;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FriendService {
    @GET(".")
    Call<List<Relationship>> getAllRelations();
}
