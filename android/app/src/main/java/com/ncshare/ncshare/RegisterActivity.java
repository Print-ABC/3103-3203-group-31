package com.ncshare.ncshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText e_username, e_email, e_password, e_name, e_contact, e_cfmPassword;
    String username, email, password, name, contact, role, cfmPassword;
    Integer roleNum;
    MaterialBetterSpinner s_role;
    Button btnRegister;
    String[] spinnerRole = {Utils.STUDENT, Utils.ORGANIZATION};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e_username = findViewById(R.id.editTextUsername);
        e_email = findViewById(R.id.editTextEmail);
        e_password = findViewById(R.id.editTextPassword);
        e_name = findViewById(R.id.editTextName);
        e_contact = findViewById(R.id.editTextContact);
        e_cfmPassword = findViewById(R.id.editTextCfmPassword);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, spinnerRole);
        s_role = findViewById(R.id.spinner_role);
        s_role.setAdapter(arrayAdapter);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = e_username.getText().toString();
                email = e_email.getText().toString();
                password = e_password.getText().toString();
                cfmPassword = e_cfmPassword.getText().toString();
                name = e_name.getText().toString();
                contact = e_contact.getText().toString();
                role = s_role.getText().toString();
                Log.i("--- INFO ---", username + ", " + email + ", " + password + ", " + name + ", " + contact + ", " + role);

                if (validateInputs()){
                    //if success, go login page
                    //else stay register page
                    registerUser();
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private boolean validateInputs() {
        int result = 0;
        if (validateUserId()) {
            result++;
        }
        if (validateEmail()){
            result++;
        }
        if (validatePassword()){
            result++;
        }
        if (validateCfmPassword()){
            result++;
        }
        if (validateName()){
            result++;
        }
        if (validateContact()){
            result++;
        }
        if (validateRole()){
            result++;
        }
        if(result == 7){
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
    Role 0 - Organization
    Role 1 - Student
     */
    private Integer getRoleNum(String role) {
        if (role.equals(Utils.ORGANIZATION)){
            return 0;
        } else {
            return 1;
        }
    }

    private boolean validateContact() {
        String regx = "(6|8|9)[0-9]{7}";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(contact);

        if (contact.isEmpty()) {
            e_contact.setError(getString(R.string.empty_contact_error));
            return false;
        }
        if (!matcher.find()) {
            e_contact.setError(getString(R.string.invalid_contact_error));
            return false;
        } else {
            e_contact.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);

        if (name.isEmpty()) {
            e_name.setError(getString(R.string.empty_name_error));
            return false;
        } else if (!matcher.find()) {
            e_name.setError(getString(R.string.invalid_name_error));
            return false;
        } else {
            e_name.setError(null);
            return true;
        }
    }

    private boolean validateCfmPassword() {
        if (!cfmPassword.equals(e_password.getText().toString())) {
            e_cfmPassword.setError(getString(R.string.invalid_confirm_password_error));
            return false;
        } else {
            e_cfmPassword.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String regx = "(^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$)";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(password);

        if (password.isEmpty()) {
            e_password.setError(getString(R.string.empty_password_error));
            return false;
        } else if (!matcher.find()) {
            e_password.setError(getString(R.string.invalid_password_error));
            return false;
        } else {
            e_password.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String regx = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (email.isEmpty()) {
            e_email.setError(getString(R.string.empty_email_error));
            return false;
        } else if (!matcher.find()) {
            e_email.setError(getString(R.string.invalid_email_error));
            return false;
        } else {
            e_email.setError(null);
            return true;
        }

    }

    private boolean validateUserId() {
        String regx = "^[a-zA-Z0-9._-]{8,15}$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(username);
        if (username.isEmpty()){
            e_username.setError(getString(R.string.empty_username_error));
            return false;
        } else if (!matcher.find()){
            e_username.setError(getString(R.string.invalid_username_error));
            return false;
        } else {
            e_username.setError(null);
            return true;
        }
    }

    public void registerUser(){
        //Create and run a background task
        String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, username, email, password, name, contact, role);
    }

}
