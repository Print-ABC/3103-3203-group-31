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

import com.ncshare.ncshare.R;

import common.SessionHandler;
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
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        // Check if user is logged in
        if (session.isLoggedIn()){
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
//        Call<User> call = RetrofitClient
//                .getInstance()
//                .getUserApi()
//                .login(user);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if(response.body().isSuccess()){
//                    pDialog.dismiss();
//                    tvLoginError.setVisibility(View.INVISIBLE);
//                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    session.loginUser(response.body().getUid(), response.body().getRole());
//                    directToMain();
//                } else {
//                    btnLogin.setEnabled(true);
//                    pDialog.dismiss();
//                    tvLoginError.setText(response.body().getMessage());
//                }
//            }

//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                pDialog.dismiss();
//            }
//        });
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