package services;

import models.Organization;
import models.Result;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrganizationService {
    @POST("company/post")
    Call<Result> addCard(@Body Organization body);
}
