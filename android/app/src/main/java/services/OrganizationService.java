package services;

import models.Organization;
import models.Result;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OrganizationService {
    @POST("organizations/create")
    Call<Result> addCard(@Header("Authorization") String token, @Body Organization body);
}
