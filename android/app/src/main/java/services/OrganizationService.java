package services;

import models.Organization;
import models.Result;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrganizationService {
    @POST("organizations/create")
    Call<Result> addCard(@Header("Authorization") String token, @Body Organization body);

    @GET("organizations/{uid}/{card}")
    Call<Organization> getCardInfo(@Header("Authorization") String token, @Path("uid") String uid, @Path("card") String card_id);
}
