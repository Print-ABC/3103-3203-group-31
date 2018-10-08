package com.ncshare.ncshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.FragmentTransaction;

public class Login extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    String username, password;
    TextView tvForgetPW, tvRegister, tvLoginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvForgetPW = findViewById(R.id.tvForgetPW);
        tvRegister = findViewById(R.id.tvRegister);
        tvLoginError = findViewById(R.id.tvLoginError);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                Log.i("LOGIN ------> ", username + ", " + password);

                if (username.isEmpty() || password.isEmpty()){
                    tvLoginError.setText("Fields cannot be blank.");
                }

                //TODO Check with DB for verifying user
                /* if wrong
                else if (){

                    vLoginError.setText("Incorrect username/password.");
                }
                */
                //if correct
                else {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", username);
                    bundle.putString("Password", password);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        tvForgetPW.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Login.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Login.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}