package activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ncshare.ncshare.R;

import common.SecurityUtils;
import common.SessionHandler;
import models.DummyResponse;
import models.Session;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText etUsername, etPassword;
    Button btnLogin;
    String username, password;
    TextView tvForgetPW, tvRegister, tvLoginError;
    private ProgressDialog pDialog;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = SessionHandler.getSession();

        // Check if user is logged in
        if (SessionHandler.isLoggedIn()){
            directToMain();
        }
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgetPW = (TextView) findViewById(R.id.tvForgetPW);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvLoginError = (TextView) findViewById(R.id.tvLoginError);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                Log.i("LOGIN ------> ", username + ", " + password);

                if (username.isEmpty() || password.isEmpty()) {
                    tvLoginError.setText("Fields cannot be blank.");
                }
                else {
                    login();
                }
            }
        });
        tvForgetPW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Direct user to MainActivity
    private void directToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void login() {
        displayLoader();
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        Call<DummyResponse> call = RetrofitClient
                .getInstance()
                .getUserApi()
                .login(user);
        call.enqueue(new Callback<DummyResponse>() {
            @Override
            public void onResponse(Call<DummyResponse> call, Response<DummyResponse> response) {
                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                btnLogin.setEnabled(true);
                if (response.body().getSuccess()) {
                    tvLoginError.setVisibility(View.INVISIBLE);
                    try {
                        String jsonResponse = SecurityUtils.decoded(SecurityUtils.manipulateToken(response.body().getToken()));
                        Gson g = new Gson();
                        User user = g.fromJson(jsonResponse, User.class);
                        SessionHandler.loginUser(user.getUid(), user.getName(), user.getUsername(), user.getToken(), user.getRole(),
                                user.getCardId(), user.getFriendship(), user.getCards());
                        directToMain();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    tvLoginError.setText(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<DummyResponse> call, Throwable t) {
                pDialog.dismiss();
                btnLogin.setEnabled(true);
            }
        });
    }

    /**
     * Display Progress bar while Logging in
     */
    private void displayLoader() {
        btnLogin.setEnabled(false);
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}