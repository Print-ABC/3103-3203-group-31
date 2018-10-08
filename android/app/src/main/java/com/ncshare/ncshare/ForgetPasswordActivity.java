package com.ncshare.ncshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgetPasswordActivity extends AppCompatActivity {

    TextView tvBackLogin, tvResetMsg;
    Button btnResetPW;
    EditText etUsername, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        tvBackLogin = (TextView) findViewById(R.id.tvBackLogin);
        tvResetMsg = (TextView) findViewById(R.id.tvForgetPWError);
        btnResetPW = (Button) findViewById(R.id.btnForgetPW);
        etEmail = (EditText) findViewById(R.id.etUsername);
        etUsername = (EditText) findViewById(R.id.etEmail);

        tvBackLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnResetPW.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                if (username.isEmpty() || email.isEmpty()) {
                    tvResetMsg.setText("Cannot be empty!");
                }
                else{
                    tvResetMsg.setText("Password has been reset.");
                }
            }
        });
    }
}
