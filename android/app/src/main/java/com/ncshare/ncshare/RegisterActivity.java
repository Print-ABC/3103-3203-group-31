package com.ncshare.ncshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class RegisterActivity extends AppCompatActivity {

    EditText e_userId, e_email, e_password, e_name, e_contact, e_organization;
    String userId, email, password, name, contact, organization, role, emailPattern;
    MaterialBetterSpinner s_role;
    Button btnRegister;
    String[] spinnerRole = {"Student", "Organization"};
    TextView tvRegError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e_userId = findViewById(R.id.editTextUserId);
        e_email = findViewById(R.id.editTextEmail);
        e_password = findViewById(R.id.editTextPassword);
        e_name = findViewById(R.id.editTextName);
        e_contact = findViewById(R.id.editTextContact);
        e_organization = findViewById(R.id.editTextOrganization);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, spinnerRole);
        s_role = findViewById(R.id.spinner_role);
        s_role.setAdapter(arrayAdapter);
        tvRegError = findViewById(R.id.tvRegError);
        btnRegister = findViewById(R.id.btnRegister);

        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = e_name.getText().toString();
                email = e_email.getText().toString();
                password = e_password.getText().toString();
                name = e_name.getText().toString();
                contact = e_contact.getText().toString();
                role = s_role.getText().toString();
                organization = e_organization.getText().toString();
                Log.i("--- INFO ---", userId + ", " + email + ", " + password + ", " + name + ", " + contact + ", " + organization + ", " + role);

                //if success, go login page
                //else stay register page
                if (userId.isEmpty() || password.isEmpty()|| email.isEmpty() || name.isEmpty() || contact.isEmpty() || role.isEmpty() || organization.isEmpty()){
                    tvRegError.setText("Fields cannot be empty.");
                }
                else if (!email.matches(emailPattern)){
                    tvRegError.setText("Invalid email format.");
                }
                else if (password.length()<6){
                        tvRegError.setText("Password needs at least 6 characters.");
                    }
                // TODO Validate the fields of each input, if email/username existed

                else {
                    Intent i = new Intent(RegisterActivity.this, Login.class);
                    startActivity(i);
                }
            }
        });
    }

    public void registerUser(View view){
        userId = e_name.getText().toString();
        email = e_email.getText().toString();
        password = e_password.getText().toString();
        name = e_name.getText().toString();
        contact = e_contact.getText().toString();
        role = s_role.getText().toString();
        organization = e_organization.getText().toString();

        //Create and run a background task
        String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, userId, email, password, name, contact, organization, role);
    }

}
