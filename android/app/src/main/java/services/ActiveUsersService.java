package services;

import models.Result;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ActiveUsersService {

    @DELETE("activeusers/logout/{uid}")
    Call<Result> logout(@Header("Authorization") String token, @Path("uid") String uid);
}
