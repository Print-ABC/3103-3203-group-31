package activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ncshare.ncshare.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.Utils;
import models.Result;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText etUsername, etEmail, etPassword, etName, etContact, etCfmPassword;
    private String username, email, password, name, contact, role, cfmPassword;
    private Integer roleNum;
    private MaterialBetterSpinner s_role;
    private Button btnRegister;
    private String[] spinnerRole = {Utils.STUDENT, Utils.ORGANIZATION};
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText) findViewById(R.id.editTextUsername);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        etName = (EditText) findViewById(R.id.editTextName);
        etContact = (EditText) findViewById(R.id.editTextContact);
        etCfmPassword = (EditText) findViewById(R.id.editTextCfmPassword);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, spinnerRole);
        s_role = (MaterialBetterSpinner) findViewById(R.id.spinner_role);
        s_role.setAdapter(arrayAdapter);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                cfmPassword = etCfmPassword.getText().toString();
                name = etName.getText().toString();
                contact = etContact.getText().toString();
                role = s_role.getText().toString();
                Log.i("--- INFO ---", username + ", " + email + ", " + password + ", " + name + ", " + contact + ", " + role);


                if (validateInputs()) {
                    User user = new User();
                    user.setRole(roleNum);
                    user.setPassword(password);
                    user.setEmail(email);
                    user.setContact(contact);
                    user.setUsername(username);
                    user.setName(name);
                    displayLoader();
                    Call<Result> call = RetrofitClient
                            .getInstance()
                            .getUserApi()
                            .addUser(user);
                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            btnRegister.setEnabled(true);
                            pDialog.dismiss();
                            switch (response.code()){
                                case 201:
                                    Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    break;
                                case 409:
                                    Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            btnRegister.setEnabled(true);
                            pDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Error "+ t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean validateInputs() {
        int result = 0;
        if (validateUserId()) {
            result++;
        }
        if (Utils.validateEmail(email, etEmail, this)) {
            result++;
        }
        if (validatePassword()) {
            result++;
        }
        if (validateCfmPassword()) {
            result++;
        }
        if (Utils.validateName(name, etName, this)) {
            result++;
        }
        if (Utils.validateContact(contact,etContact,this)) {
            result++;
        }
        if (validateRole()) {
            result++;
        }
        if (result == 7) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateRole() {
        if (role.isEmpty()) {
            s_role.setError(getString(R.string.empty_role_error));
            return false;
        } else {
            roleNum = getRoleNum(role);
            return true;
        }
    }

    /*
    Role 0 - Student
    Role 1 - Organization
     */
    private Integer getRoleNum(String role) {
        if (role.equals(Utils.ORGANIZATION)) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean validateCfmPassword() {
        if (!cfmPassword.equals(etPassword.getText().toString())) {
            etCfmPassword.setError(getString(R.string.invalid_confirm_password_error));
            return false;
        } else {
            etCfmPassword.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String regx = "(?=^.{16,35}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\s).*$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(password);

        if (password.isEmpty()) {
            etPassword.setError(getString(R.string.empty_password_error));
            return false;
        } else if (!matcher.find()) {
            etPassword.setError(getString(R.string.invalid_password_error));
            return false;
        } else {
            etPassword.setError(null);
            return true;
        }
    }

    private boolean validateUserId() {
        String regx = "^[a-zA-Z0-9._-]{8,25}$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(username);
        if (username.isEmpty()) {
            etUsername.setError(getString(R.string.empty_username_error));
            return false;
        } else if (!matcher.find()) {
            etUsername.setError(getString(R.string.invalid_username_error));
            return false;
        } else {
            etUsername.setError(null);
            return true;
        }
    }

    /**
     * Display Progress bar while Registering
     */
    private void displayLoader() {
        btnRegister.setEnabled(false);
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage(getString(R.string.dialog_register));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}

