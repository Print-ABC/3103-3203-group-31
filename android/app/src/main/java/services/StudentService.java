package services;

import models.Result;
import models.Student;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface StudentService {
    @POST("students/create")
    Call<Result> addCard(@Header("Authorization") String token, @Body Student body);
}
