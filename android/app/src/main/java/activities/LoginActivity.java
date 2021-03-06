package activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ncshare.ncshare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import common.SecurityUtils;
import common.SessionHandler;
import models.DummyResponse;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText etUsername, etPassword;
    Button btnLogin, btnSubmit;
    String username, password;
    TextView tvRegister, tvLoginError;
    EditText etCode;
    private ProgressDialog pDialog;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(this);

        // Check if user is logged in
        if (session.isLoggedIn()) {
            directToMain();
        }
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvLoginError = (TextView) findViewById(R.id.tvLoginError);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    tvLoginError.setText("Fields cannot be blank.");
                } else {
                    login(v);
                }
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

    private void login(final View v) {
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
                pDialog.dismiss();
                btnLogin.setEnabled(true);
                switch (response.code()) {
                    case 200:
                        Toast.makeText(LoginActivity.this, R.string.msg_verification_code_sent, Toast.LENGTH_SHORT).show();
                        tvLoginError.setVisibility(View.INVISIBLE);
                        try {
                            open_dialog(v);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    default:
                        //Toast.makeText(LoginActivity.this, "Code not sent!", Toast.LENGTH_SHORT).show();
                        tvLoginError.setText("Login failed");
                        break;
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
        pDialog.setMessage(getString(R.string.dialog_logging_in));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void open_dialog(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        View row = this.getLayoutInflater().inflate(R.layout.alert_dialog_2fa, null);
        alertDialog.setView(row);
        btnSubmit = (Button) row.findViewById(R.id.btnSubmit);
        etCode = (EditText) row.findViewById(R.id.etCode);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCode.getText().toString().isEmpty() || etCode.getText().toString().length() < 10) {
                    Toast.makeText(LoginActivity.this, R.string.error_empty_input, Toast.LENGTH_SHORT).show();
                } else {
                    //CALL to CHECK 2FA
                    Call<DummyResponse> call = RetrofitClient
                            .getInstance()
                            .getUserApi()
                            .check2fa(etCode.getText().toString());
                    call.enqueue(new Callback<DummyResponse>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onResponse(Call<DummyResponse> call, Response<DummyResponse> response) {
                            switch (response.code()) {
                                case 200:
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    tvLoginError.setVisibility(View.INVISIBLE);
                                    try {
                                        String correctToken = SecurityUtils.manipulateToken(response.body().getToken());
                                        String jsonResponse = SecurityUtils.decoded(correctToken);
                                        Gson g = new Gson();
                                        User user = g.fromJson(jsonResponse, User.class);
                                        session.loginUser(user.getUid(), user.getName(), user.getUsername(), correctToken, user.getRole(),
                                                user.getCardId(), user.getFriendship(), user.getCards());
                                        directToMain();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                default:
                                    try {
                                        try {
                                            JSONObject jsonBody = new JSONObject(response.errorBody().string());
                                            if (jsonBody.has("message")) {
                                                Toast.makeText(getApplicationContext(), jsonBody.getString("message"), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(LoginActivity.this, R.string.error_login_failed, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        tvLoginError.setText(R.string.error_login_failed);
                                        break;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    tvLoginError.setText(R.string.error_login_failed);
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<DummyResponse> call, Throwable t) {
                            pDialog.dismiss();
                            btnLogin.setEnabled(true);
                        }
                    });

                    dialog.dismiss();
                    //directToMain();
                }
            }
        });
    }
}