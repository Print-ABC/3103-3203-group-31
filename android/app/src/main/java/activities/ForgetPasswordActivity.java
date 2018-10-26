package activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ncshare.ncshare.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    TextView tvBackLogin, tvResetMsg;
    Button btnResetPW, btnGetVeri;
    EditText etVeri, etEmail, etPassword;
    TextInputLayout tvVerify, tvPassword;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        tvBackLogin = (TextView) findViewById(R.id.tvBackLogin);
        tvResetMsg = (TextView) findViewById(R.id.tvForgetPWError);
        btnGetVeri = (Button) findViewById(R.id.btnGetVeri);
        btnResetPW = (Button) findViewById(R.id.btnForgetPW);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etVeri = (EditText) findViewById(R.id.etVerify);
        tvVerify = (TextInputLayout) findViewById(R.id.tvVerify);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvPassword = (TextInputLayout) findViewById(R.id.tvPassword);

        tvBackLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnGetVeri.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                email = etEmail.getText().toString();
                if (email.isEmpty()) {
                    tvResetMsg.setText("Cannot be empty!");
                }
                else{
                    //CALL. If User exist, send verification code and set text box visible,
                    // set current button invisible and set another button visible

                    etEmail.setEnabled(false);
                    tvVerify.setVisibility(View.VISIBLE);
                    etVeri.setVisibility(View.VISIBLE);
                    tvVerify.setVisibility(View.VISIBLE);
                    etVeri.setVisibility(View.VISIBLE);
                    tvPassword.setVisibility(View.VISIBLE);
                    etPassword.setVisibility(View.VISIBLE);
                    btnGetVeri.setVisibility(View.GONE);
                    btnResetPW.setVisibility(View.VISIBLE);
                    btnResetPW.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            //CALL. If verified, update the password with salt of the user into the db
                            tvResetMsg.setText("Reset Password");

                        }
                    });
                }
            }
        });
    }
}
