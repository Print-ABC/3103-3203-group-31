package services;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.*;

public class RetrofitClient {
    //TODO: change base URL
    private static final String BASE_URL = "https://team31.icebear.online/";
    //private static final String BASE_URL = "http://10.0.2.2:3000/";
    private Retrofit retrofit;
    private static RetrofitClient retrofitClient;

    private RetrofitClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BASIC);

        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add("team31.icebear.online", "sha256/2yfmY0ajPifNNNIfCKAepWIcMcjR2jdOQWI/9ZVb8P8=")
                .add("team31.icebear.online","sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=")
                .add("team31.icebear.online","sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=")
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static synchronized RetrofitClient getInstance(){
        if (retrofitClient == null){
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
        }



    public UserService getUserApi() {
        return retrofit.create(UserService.class);
    }
    public FriendService getFriendRequestApi() { return retrofit.create(FriendService.class);}
    public OrganizationService getOrganizationApi() { return retrofit.create(OrganizationService.class);}
    public StudentService getStudentApi() { return retrofit.create(StudentService.class);}
    public ActiveUsersService getActiveUsersApi() { return retrofit.create(ActiveUsersService.class);}
}
